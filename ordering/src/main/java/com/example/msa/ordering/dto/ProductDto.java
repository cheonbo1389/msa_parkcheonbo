package com.example.msa.ordering.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


//ProductResDto 복사해서 ProductDto로 이름변경
//상품조회 요청을 보낸후, Product 응답을 받을 객체
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductDto {
    private Long id;
    private String name;
    private int price;
    private int stockQuantity;
}
