package com.example.msa.ordering.repository;

import com.example.msa.ordering.domain.Ordering;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface OrderingRepository extends JpaRepository<Ordering, Long> {

    //유저 본인이 주문한 목록 리스트 조회
    ArrayList<Ordering> findByMemberId(long userId);
}
