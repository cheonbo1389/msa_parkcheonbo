package com.example.msa.ordering.service;


import com.example.msa.ordering.domain.Ordering;
import com.example.msa.ordering.dto.OrderCreateDto;
import com.example.msa.ordering.dto.ProductDto;
import com.example.msa.ordering.dto.ProductUpdateStockDto;
import com.example.msa.ordering.repository.OrderingRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.*;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

// 임시용
//import com.example.msa.ordering.domain.Product;
//import com.example.msa.ordering.repository.ProductRepository;
//



@Service
@Transactional //메시지 단에서 에러가 발생하면 롤백되도록 처리
public class OrderingService {
    private final OrderingRepository orderingRepository;

    //5) 의존성 주입
    // 동기화할때 사용
    private final RestTemplate restTemplate;

    //동기방식(동기 방식할거면 추천)
    //ProductFeign 주입
    private final ProductFeign productFeign;

    //비동기방식
    //kafka 주입
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public OrderingService(OrderingRepository orderingRepository, RestTemplate restTemplate, ProductFeign productFeign, KafkaTemplate<String, Object> kafkaTemplate) {
        this.orderingRepository = orderingRepository;
        this.restTemplate = restTemplate;
        this.productFeign = productFeign;
        this.kafkaTemplate = kafkaTemplate;
    }

    //방식1. RestTemplate 방식(동기)
    //주문
    public Ordering orderCreate(OrderCreateDto orderDto, String userId){
        System.out.println("<<< OrderingService - orderCreate >>>");
//        String id = SecurityContextHolder.getContext().getAuthentication().getName();
//        Member member = memberRepository.findById(Long.parseLong(id))
//                .orElseThrow(() -> new EntityNotFoundException("member is not found"));

//        // 1) 삭제
//        // 조화(동기) -> 수량감소(비동기) -> 주문완성
//        // MSA에서는 Product 서버한테 http요청(동기)해서
//        // Product 객체를 조회해야 하는 식으로 코드가 변환되어야함
//        Product product = productRepository.findById(orderDto.getProductId())
//                .orElseThrow(() -> new EntityNotFoundException("product is not found"));

        // 2) 재품재고 조회 product get 요청 - url 패턴으로 요청
        String productGetUrl = "http://product-service/product/" + orderDto.getProductId();

        // 4) RestTemplate이 여러 군데 필요하므로 new로 생성하지 말고 빈으로 만들어서 재사용
        // => 싱글톤으로 생성(common/config/RestTemplateConfig)

        // 6) exchange()는 get,post 공통화시켜서 편하게 사용 가능

        // 8) xxx에 헤더를 세팅할 수 있는데 여기서는 필요없으므로 null로 세팅가능
        // 근데, 학습을 위해서 HttpEntity 세팅했음
        // product에 userID를 넘겨줘야하는 경우(Gateway에서 order로 "x-User-Id"를 넘겼으나 order에서 product로 요청시 전달되지 않음)
        // => "x-User-Id"가 커스텀헤더이믈 order까지만 유지됨.
        // 따라서 order에서 아래처럼 다시 세팅해야함
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("X-User-Id", userId);
        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);

        //이 url로 get 요청을 해서 ProductDto로 돌려받음. xxx => httpEntity로 세팅
        ResponseEntity<ProductDto> response
                = restTemplate.exchange(productGetUrl, HttpMethod.GET, httpEntity, ProductDto.class);

        //ResponseEntity에서 body값을 꺼내 ProductDto를 가져온다.
        ProductDto productDto = response.getBody();

        //주문수량
        int quantity = orderDto.getProductCount();

        // 7) product => productDto로 변경
        if(productDto.getStockQuantity() < quantity){ //재고 > 주문수량
            throw new IllegalArgumentException("재고 부족");
        }else {
            // 사용자 주문 수량만큼 데이터 베이스에서 차감하는 부분도 http 요청으로 변환
            /*
            *  - 주문 상환에서 order 모듈과 product 모듈 간의 통신
            *  - 동기적 요청의 경우) RestTemplate(Restclient) 또는 Feign 클라이언트 사용
            *  - 비동기적 이벤트 기반 요청의 경우) RabbitMQ 또는 kafka를 활용
            *  - 비동기적 : 감소 요청을 하면 카프카에 메시지를 던져넣고,
            *              기다리지 않고 바로 주문 성공시켜서 사용자에게 응답을 보낸다.
            *              차후 Product 모듈이 그 메시지를 가져감, 동기보다 더 빠르다.
            * */
            // 3) product put(제품재고 업데이트) 요청 - url 패터능로 요청
            String productPutUrl = "http://product-service/product/updatestock";
            //받는 쪽, ProductController에서 updateStock(@RequsetBody)로 받으므로 기본적으로 json으로 받게됨
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);

//            // 9) 삭제
//            product.updateStockQuantity(orderDto.getProductCount());

            // 10) update 요청 보낼때, body 값에 update 객체값을 조립해서 넣어줘야한다.
            // updateEntity는 요청 보낼 객체를 json형식으로 만들어서 넣어준다.
            // json 형식으로 자동 변환되므로 변환할 필요는 없다.
            // 10-1) ProductUpdateStockDto 형식으로 넣어줘야 하므로
            // Order 쪽에서 이 dto를 조립해서 던져준다.
            HttpEntity<ProductUpdateStockDto> updateEntity = new HttpEntity(
                    //body 부분
                    ProductUpdateStockDto
                        .builder()
                        .productId(orderDto.getProductId())
                        .productQuantity(orderDto.getProductCount())
                        .build()
                    //header 부분
                    , httpHeaders //필요하면 추가
            );

            //productPutUrl로 put 요청시 updateEntity가 body에 세팅되어나감
            //update 요청보낼 entity, Void.class는 응답받을 클래스 없음
            restTemplate.exchange(productPutUrl, HttpMethod.PUT, updateEntity, Void.class);
        }

        //주문완성
        // .memberId(매개변수 userId)
        // .productId(매개변수 orderDto.getProductId())
        Ordering ordering = Ordering.builder()
                .memberId(Long.parseLong(userId)) //id가 있어야 누가 주문했는지
                .productId(orderDto.getProductId())
                .quantity(orderDto.getProductCount())
                .build();

        orderingRepository.save(ordering);

        return ordering;
    }


    //방식2. openFeign 방식(동기)
    //방식3. kafka 방식(비동기)
    // 2) circuitBreaker가 발동되어서 아래 메서드 요청이 들어오면 바로 product-service에 대한 요청을 차단해야 하고
    //name = "productService"은 깃허브에 올린 orderingservice의 productService와 일치해야함
    //주문
    @CircuitBreaker(name = "productService", fallbackMethod = "fallbackProductService")
    public Ordering orderFeignKafkaCreate(OrderCreateDto orderDto, String userId){
        System.out.println("<<< OrderingService - orderFeignKafkaCreate >>>");

        //1. 제품 조회 => 지연이 일어날 예정
        ProductDto productDto = productFeign.getProductbyId(orderDto.getProductId(), userId);

        //주문수량
        int quantity = orderDto.getProductCount();


        if(productDto.getStockQuantity() < quantity){ //재고 > 주문수량
            throw new IllegalArgumentException("재고 부족");
        }else {
            //2. product put 요청
            //사용자가 주문한 내역을 dto로 다시 조립
            ProductUpdateStockDto dto = ProductUpdateStockDto
                                        .builder()
                                        .productId(orderDto.getProductId())
                                        .productQuantity(orderDto.getProductCount())
                                        .build();

//            //(1). openFeign 방식
//            productFeign.updateProductStock(dto);


            //(2). kafka 방식
            // topic : kafka의 논리적인 공간
            // dto. 즉, 객체를 JacksonJsonSerializer가 동작하면서 json으로 변환해서
            // "update-stock-topic"에 메시지 발행
            kafkaTemplate.send("update-stock-topic", dto);

        }

        //3. 주문완성
        Ordering ordering = Ordering.builder()
                .memberId(Long.parseLong(userId)) //id가 있어야 누가 주문했는지
                .productId(orderDto.getProductId())
                .quantity(orderDto.getProductCount())
                .build();

        orderingRepository.save(ordering);

        return ordering;
    }


    //fallbackProductService를 호출하는 CircuitBreaker 메서드. 즉, orderFeignKafkaCreate의 리턴타입, 매개변수를 맞춘다.
    //깃허브에 올린 ordering-service.yaml 참고
    // 4번 정상요청, 5번 요청 -> 2번을 지연
    // 3) 사용자에게 예외응답을 주어야 함, 이때 fallbackProductService를 호출
    public Ordering fallbackProductService(OrderCreateDto orderDto, String userId, Throwable throwable){
        throw new RuntimeException("상품 서비스가 응답이 없어, 에러가 발생했습니다. 나중에 다시 시도해주세요.");
    }

}
