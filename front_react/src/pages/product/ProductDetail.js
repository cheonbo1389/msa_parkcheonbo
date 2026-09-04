import React, { useEffect, useState } from 'react';
import {Button, Card, Container} from 'react-bootstrap';
import { useNavigate,useParams } from 'react-router-dom';


const ProductDetail = () => {
    const propsParam = useParams(); 
    const id = propsParam.id;
    const navigate = useNavigate();
    const token = localStorage.getItem("Token");

    const [productList, setProductList] = useState([]);
    const [member, setMember] = useState([]);


    useEffect(() => {
        fetch("http://localhost:8081/product/detail/"+id,{
            method: "GET" ,
            headers: {
                "Content-Type": "application/json",
            }
        }) 
        .then(res => res.json())
        .then(res => {
            console.log(1,res);
            setProductList(res); 
            setMember(res.member);
        });
    }, [])


    const createOrder = () => {
        navigate("/ordercreate/"+productList.id);
    }

    //이전 페이지로
    const backpage = () => {
        navigate(-1);
    }

    
    return (
        <div>
            <Container>
                <br />
                <h3>{productList.name}의 상세 내용</h3>
                <Card>
                    <Card.Body>               
                        <Card.Title>제품번호 : {productList.id}</Card.Title>
                        <Card.Title>제품명 : {productList.name}</Card.Title>
                        <Card.Title>제품가격 : {productList.price}</Card.Title>
                        <Card.Title>제품재고 : {productList.stockQuantity}</Card.Title>
                        <Card.Title>판매자 : {member.name}</Card.Title>
                        <Button variant="warning" className="me-3" onClick={backpage}>이전페이지로</Button>
                        <Button variant="primary" onClick={createOrder}>주문하기</Button>

                    </Card.Body>
                </Card>
            </Container>
        </div>
    );
};

export default ProductDetail;