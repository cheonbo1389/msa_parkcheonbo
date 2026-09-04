package com.example.msa.product.controller;


import com.example.msa.product.domain.Product;
import com.example.msa.product.dto.ProductRegisterDto;
import com.example.msa.product.dto.ProductResDto;
import com.example.msa.product.dto.ProductUpdateStockDto;
import com.example.msa.product.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    //매개변수 추가 =>  @RequestHeader("X-User-Id") String userId) => ApiGateway에서 헤더로 넘긴 X-user-Id임
    //dto에 상품명, 상품수량등이 들어있고, userId는 apigateway에서 넘어온 값을 가지고
    //누가 어떤 상품을 등록했는지 간단하게 save시킬수 있다.
    @PostMapping("/create")
    public ResponseEntity<?> productCreate(@RequestBody ProductRegisterDto dto, @RequestHeader("X-User-Id") String userId){
        System.out.println("<<< ProductController - /create >>>");

        Product product = productService.productCreate(dto, userId);
        return new ResponseEntity<>(product.getId(), HttpStatus.CREATED);
    }

    //재고조회 API
    @GetMapping("{id}")
    public ResponseEntity<?> productDetail(@PathVariable Long id , @RequestHeader("X-User-Id") String userId) throws InterruptedException {
        System.out.println("<<< ProductController - /productDetail >>>");
        Thread.sleep(3000L); //3초지연

        ProductResDto productResDto = productService.productDetail(id);
        return new ResponseEntity<>(productResDto, HttpStatus.OK);
    }

    //상품재고 업데이트 API
    @PutMapping("/updatestock")
    public ResponseEntity<?> updateStock(@RequestBody ProductUpdateStockDto productUpdateStockDto){
        System.out.println("<<< ProductController - /updatestock >>>");

        Product product = productService.updateStockQuantity(productUpdateStockDto);

        return new ResponseEntity<>(product.getId(), HttpStatus.OK);
    }
}
