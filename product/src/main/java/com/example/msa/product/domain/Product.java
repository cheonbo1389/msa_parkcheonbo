package com.example.msa.product.domain;

import com.example.msa.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Product extends BaseTimeEntity {

    //제품 아이디
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //제품명
    private String name;

    //가격
    private Integer price;

    //수량
    private Integer stockQuantity;

    @Column(nullable = false)
    private Long memberId;

    //재고감소
    public void updateStockQuantity(int stockQuantity){ // stockQuantity = 주문갯수
        this.stockQuantity = this.stockQuantity - stockQuantity;
    }
}
