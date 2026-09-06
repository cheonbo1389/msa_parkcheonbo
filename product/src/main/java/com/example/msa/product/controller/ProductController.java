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

    //제품 추가
    @PostMapping("/create")
    public ResponseEntity<?> productCreate(@RequestBody ProductRegisterDto dto, @RequestHeader("X-User-Id") String userId){
        System.out.println("<<< ProductController - /create >>>");

        Product product = productService.productCreate(dto, userId);
        return new ResponseEntity<>(product.getId(), HttpStatus.CREATED);
    }


    //제품 전체 조회
    @GetMapping("/list")
    public ResponseEntity<?> productList(){
        System.out.println("<<< ProductController - /list >>>");

        return new ResponseEntity<>(productService.productAllList(),HttpStatus.OK);
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


    //제품 상세 조회
    @GetMapping("/detail/{id}")
    public ResponseEntity<?> productDetail(@PathVariable Long id){
        System.out.println("<<< ProductController - /detail >>>");

        return new ResponseEntity<>(productService.productDeatail(id),HttpStatus.OK);
    }

    //제품 수정
    @PutMapping("/update/{id}")
    public ResponseEntity<?> productUpdate(@RequestBody Product dto){
        System.out.println("<<< ProductController - /update >>>");

        Product product = productService.productUpdate(dto);
        return new ResponseEntity<>(product.getId(), HttpStatus.OK);
    }


    //유저가 추가한 제품 조회
    @GetMapping("/mylist")
    public ResponseEntity<?> myproductList(@RequestHeader("X-User-Id") String userId){
        System.out.println("<<< ProductController - /mylist >>>");

        return new ResponseEntity<>(productService.myproductList(userId),HttpStatus.OK);
    }
}
