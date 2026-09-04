package com.example.msa.ordering.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//어떤 상품, 몇 개를 수정할것인지
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductUpdateStockDto {
    private Long productId;
    private Integer productQuantity;
}
