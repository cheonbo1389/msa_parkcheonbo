package com.example.msa.ordering.service;


import com.example.msa.ordering.dto.ProductDto;
import com.example.msa.ordering.dto.ProductUpdateStockDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

//interface 안에 정의된 메서드가 FeignClient 라이브러리를 사용한 http 요청을 할 수 있는 객체가 된다.
//이렁게 선언한 순간 Bean 객체가 되므로 OrderingService에서 주입받을 수 있다.
//OrderingService에서 ProductService롤 요청하는 상황임. 그래서 요청하는 상대방 객체단위로 FeignClient가 만들어진다.
//http://product-service/product/{productId}를 세팅해서 eureka에게 질의해서 product-service로 get요청이 나가게된다.
//@FeignClient => MSA 아키텍처에서 서버간 통신하기 위해 최적화된 라이브러리
@FeignClient(name = "product-service") //name은 eureka에 등록된 호출할 서비스의 이름
public interface ProductFeign {

    //제품 재고 조회
    @GetMapping("/product/{productId}")
    ProductDto getProductbyId(@PathVariable Long productId, @RequestHeader("X-User-Id") String userId); //productId 두 개가 일채해야함

    //제품 수량 업데이트
    @PutMapping("/product/updatestock")
    void updateProductStock(@RequestBody ProductUpdateStockDto productUpdateStockDto);
}
