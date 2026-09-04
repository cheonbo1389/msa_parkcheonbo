//package com.example.msa.ordering.domain;
//
//import com.example.msa.common.domain.BaseTimeEntity;
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//@Entity
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//@Getter
//public class Product extends BaseTimeEntity {
//
//    //제품 아이디
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    //제품명
//    private String name;
//
//    //가격
//    private Integer price;
//
//    //수량
//    private Integer stockQuantity;
//
////    //회원 정보
////    @ManyToOne(fetch = FetchType.LAZY) //JPA에서 다대일(N:1) 관계를 매핑할 때, 사용하는 어노테이션
////    @JoinColumn(name = "member_id")
////    private Member member;
//
//
//    public void updateStockQuantity(int stockQuantity){
//        this.stockQuantity = this.stockQuantity - stockQuantity;
//    }
//}
