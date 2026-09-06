package com.example.msa.ordering.controller;

import com.example.msa.ordering.domain.Ordering;
import com.example.msa.ordering.dto.OrderCreateDto;
import com.example.msa.ordering.service.OrderingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/ordering")
public class OrderingController {
    private final OrderingService orderingService;

    public OrderingController(OrderingService orderingService) {
        this.orderingService = orderingService;
    }

    //매개변수 추가 =>  @RequestHeader("X-User-Id") String userId) => ApiGateway에서 헤더로 넘긴 X-user-Id임
    @PostMapping("/create")
    public ResponseEntity<?> orderCreate(@RequestBody OrderCreateDto dtos, @RequestHeader("X-User-Id") String userId){
        System.out.println("<<< OrderingController - /create >>>");

//        //방법1. RestTemplate
//        Ordering ordering = orderingService.orderCreate(dtos,userId);

        //방법2. FeignClient
        Ordering ordering = orderingService.orderFeignKafkaCreate(dtos,userId);

        return new ResponseEntity<>(ordering.getId(), HttpStatus.CREATED);
    }

    //유저 본인이 주문한 목록 리스트 조회
    @GetMapping("/orderingList")
    public ResponseEntity<?> orderingList(@RequestHeader("X-User-Id") String userId){
        System.out.println("<<< OrderingController - /orderingList >>>");

        ArrayList<Ordering> orderingList =  orderingService.orderList(userId);

        return new ResponseEntity<>(orderingList, HttpStatus.OK);
    }

}
