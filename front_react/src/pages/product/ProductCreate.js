import React, { useState } from 'react';
import { Button, Container, Form} from 'react-bootstrap';
import {  useNavigate } from 'react-router-dom';


const ProductCreate = () => {
    const navigate = useNavigate();

    const [product, setProduct ] = useState({
        name : '',
        price : '',
        stockQuantity : ''
    })

    const changeValue = (e) => {
        setProduct({
            ...product,
            [e.target.name] : e.target.value
        });
    }

    const submitProduct = (e) =>{
        e.preventDefault();
        const token = localStorage.getItem("Token");
        
        fetch("http://localhost:8081/product/create", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            },
            body: JSON.stringify(product)
        })
        .then((res) => {
            if (res.status === 201) {
                return res.json();
            } else {
                throw new Error(`상품 등록 실패: ${res.status}`);
            }
        })
        .then((productId) => {
            alert("제품 추가 성공했습니다.");
            navigate("/productlist");
        })
        .catch((error) => {
            console.error("실패:", error);
            alert("제품 추가에 실패했습니다.");
        });
    }


    return (
        <div>
            <Container>
                <br />
                <h3>새로운 제품 추가</h3>
                <Form onSubmit={submitProduct}>
                <Form.Group className="mb-3" controlId="ProductName">
                    <Form.Label>제품명</Form.Label>
                    <Form.Control type="text" placeholder="새로운 제품명 입력" onChange={changeValue} name="name"/>
                </Form.Group>

                <Form.Group className="mb-3" controlId="ProductPrice">
                    <Form.Label>제품 개당 가격</Form.Label>
                    <Form.Control type="number" placeholder="제품 개당 가격 입력" onChange={changeValue} name="price"/>
                </Form.Group>

                <Form.Group className="mb-3" controlId="ProductstockQuantity">
                    <Form.Label>제품 수량</Form.Label>
                    <Form.Control type="number" placeholder="제품 수량 입력" onChange={changeValue} name="stockQuantity" />
                </Form.Group>

                <Button variant="primary" type="submit">
                    추가
                </Button>
                </Form>
            </Container>
        </div>
    );
};

export default ProductCreate;
