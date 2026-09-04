package com.example.msa.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//상품조회 요청이 들어왔을때, 4가지 값으로 리턴
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductResDto {
    private Long id;
    private String name;
    private int price;
    private int stockQuantity;
}
