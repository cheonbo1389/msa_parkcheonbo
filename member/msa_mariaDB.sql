-- MariaDB
-- db 생성
create database monodb character set utf8; -- 데이터베이스 생성
show grants;

-- 테이블 목록 확인
show tables;

-- 멤버 테이블 확인
select * from member;

-- 제품 테이블 확인
select * from product;
DESC product;
SHOW COLUMNS FROM product;

-- 주문 테이블 확인
select * from ordering;

--  테이블 삭제
drop table ordering;

drop table product;

drop table member;