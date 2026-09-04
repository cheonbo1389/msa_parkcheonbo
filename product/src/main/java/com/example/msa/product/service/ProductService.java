package com.example.msa.product.service;

import com.example.msa.product.domain.Product;
import com.example.msa.product.dto.ProductRegisterDto;
import com.example.msa.product.dto.ProductResDto;
import com.example.msa.product.dto.ProductUpdateStockDto;
import com.example.msa.product.repository.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;}

    //제품 등록
    public Product productCreate(ProductRegisterDto dto, String userId){
        System.out.println("<<< ProductService - 제품등록 >>>");

//        Product product = productRepository.save(dto.toEntity(member));
        //dto를 Member로 변환
        Product product = productRepository.save(dto.toEntity(Long.parseLong(userId)));

        return product;
    }

    //재고조회 API
    public ProductResDto productDetail(Long id){
        System.out.println("<<< ProductService - 재고조회 >>>");


        //Optional 객체 형태로 return을 하기위해 orElseThrow를 던진다. 없으면 예외처리
        Product product = productRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("없는 상품입니다.")
        );

        ProductResDto productResDto = ProductResDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .build();

        return productResDto;
    }

    //상품재고 업데이트 API
    //productUpdateStockDto에 id, 감소시킬 수량을 넣어, 요청이 들어오면 product 객체를 찾아서
    //감소시키고자 하는 수량을 뺀 값을 세팅한다.
    public Product updateStockQuantity(ProductUpdateStockDto productUpdateStockDto){
        System.out.println("<<< ProductService - 상품재고 업데이트 >>>");
        Product product = productRepository.findById(productUpdateStockDto.getProductId())
                //상품이 없는 경우
                .orElseThrow(() -> new EntityNotFoundException("없는 상품입니다."));

        //감소시킬 수량을 가지고 수정하러 감
        product.updateStockQuantity(productUpdateStockDto.getProductQuantity());

        return product;
    }

    // listener 객체가 실시간으로 update-stock-topic을 바라보고 있다가
    // 메시지가 들어오면 아래 매개변수 message에 주입된다.
    @KafkaListener(topics = "update-stock-topic", containerFactory = "kafkaListener")
    public void stockConsumer(String message){
        System.out.println(message); //{"productId : 1, productQuantity : 1"}

        ObjectMapper objectMapper = new ObjectMapper();
        ProductUpdateStockDto dto = null;

        try{
            // ObjectMapper라는 라이브러리를 통해서 message를 객체로 형변환
            dto = objectMapper.readValue(message, ProductUpdateStockDto.class);
        }catch (JsonProcessingException e){

            throw new RuntimeException(e);
        }

        //보상트랜잭션 적용시 아래문장은 try문으로 들어간다.
        //상품재고 업데이트 API 호출.
        //즉, order 호출할때마다 product 재고가 1씩 감소
        this.updateStockQuantity(dto);
    }
}
