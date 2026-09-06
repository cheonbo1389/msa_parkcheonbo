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

import java.util.ArrayList;


@Service
@Transactional
public class OrderingService {
    private final OrderingRepository orderingRepository;

    //의존성 주입
    private final RestTemplate restTemplate;

    //ProductFeign 주입
    private final ProductFeign productFeign;

    //kafka 주입
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public OrderingService(OrderingRepository orderingRepository, RestTemplate restTemplate, ProductFeign productFeign, KafkaTemplate<String, Object> kafkaTemplate) {
        this.orderingRepository = orderingRepository;
        this.restTemplate = restTemplate;
        this.productFeign = productFeign;
        this.kafkaTemplate = kafkaTemplate;
    }


    //주문
    @CircuitBreaker(name = "productService", fallbackMethod = "fallbackProductService")
    public Ordering orderFeignKafkaCreate(OrderCreateDto orderDto, String userId){
        System.out.println("<<< OrderingService - orderFeignKafkaCreate >>>");


        ProductDto productDto = productFeign.getProductbyId(orderDto.getProductId(), userId);

        //주문수량
        int quantity = orderDto.getProductCount();


        if(productDto.getStockQuantity() < quantity){
            throw new IllegalArgumentException("재고 부족");
        }else {

            ProductUpdateStockDto dto = ProductUpdateStockDto
                                        .builder()
                                        .productId(orderDto.getProductId())
                                        .productQuantity(orderDto.getProductCount())
                                        .build();

            kafkaTemplate.send("update-stock-topic", dto);

        }


        Ordering ordering = Ordering.builder()
                .memberId(Long.parseLong(userId))
                .productId(orderDto.getProductId())
                .quantity(orderDto.getProductCount())
                .build();

        orderingRepository.save(ordering);

        return ordering;
    }


    //fallbackProductService를 호출하는 CircuitBreaker 메서드. 즉, orderFeignKafkaCreate의 리턴타입, 매개변수를 맞춘다.
    public Ordering fallbackProductService(OrderCreateDto orderDto, String userId, Throwable throwable){
        throw new RuntimeException("상품 서비스가 응답이 없어, 에러가 발생했습니다. 나중에 다시 시도해주세요.");
    }


    //유저 본인이 주문한 목록 리스트 조회
    public ArrayList<Ordering> orderList (String userId){
        System.out.println("<<< OrderingService - orderList >>>");

        return orderingRepository.findByMemberId(Long.parseLong(userId));
    }
}
