package com.example.msa.product.dto;

import com.example.msa.product.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductRegisterDto {
    //제품명
    private String name;

    //제품 카테고리
    private String category;

    //제품 가격
    private int price;

    //제품 수량
    private int stockQuantity;

//    //Member 객체를 매개변수로 받아서 Product.build()에 세팅..
//    //Product 객체를 만들어서 리턴해주는 메서드
//    public Product toEntity(Member member){
//        return Product.builder()
//                .name(this.name)
//                .price(this.price)
//                .stockQuantity(this.stockQuantity)
//                .member(member)
//                .build();
//    }

    public Product toEntity(Long userId){
        return Product.builder()
                .name(this.name)
                .price(this.price)
                .stockQuantity(this.stockQuantity)
                .memberId(userId)
                .build();
    }
}
