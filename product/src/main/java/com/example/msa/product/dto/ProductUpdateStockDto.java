package com.example.msa.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//어떤 상품, 몇 개를 수정할것인지
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductUpdateStockDto {
    private Long productId;
    private Integer productQuantity;
}
