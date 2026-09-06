import { Card } from 'react-bootstrap';
import React, { useEffect, useState } from 'react';

const OrderItem = (props) => {
    const  { id, quantity, productId  } = props.order
    // const productname = props.order.product.name
    const token = localStorage.getItem("Token");
    const [product, setProduct ] = useState({})
    const price = (product.price)*quantity;

    useEffect(() => {
            fetch("http://localhost:8081/product-service/product/detail/"+productId,{
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            }
            }) 
            .then((res) => res.json())
            .then((res) => {
                console.log(res);
                
                setProduct(res);

            })
    }, []);


    return (
        <div>
            <Card>
                <Card.Body>               
                    <Card.Title>주문번호 : {id}</Card.Title>
                    <Card.Title>제품명 : {product.name}</Card.Title>
                    <Card.Title>주문한 상품 개수 : {quantity}</Card.Title>
                    <Card.Title>주문 금액 : {price}</Card.Title>
                </Card.Body>
            </Card>
            <br />
        </div>
    );
};

export default OrderItem;