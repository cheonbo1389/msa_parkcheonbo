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

//    //회원 정보
//    @ManyToOne(fetch = FetchType.LAZY) //JPA에서 다대일(N:1) 관계를 매핑할 때, 사용하는 어노테이션
//    @JoinColumn(name = "member_id")
//    private Member member;

    //Member를 자주 조회하게 되면 서버간 통신이 잦아져서 서버 성능 떨어질 수 있음
    //필요에 따라서 일정 부분 의도적으로 반정규화 시킴
    @Column(nullable = false)
    private Long memberId;

    //재고감소
    public void updateStockQuantity(int stockQuantity){ // stockQuantity = 주문갯수
        this.stockQuantity = this.stockQuantity - stockQuantity;
    }
}
