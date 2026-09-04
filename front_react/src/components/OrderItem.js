import React from 'react';
import { Card } from 'react-bootstrap';


const OrderItem = (props) => {
    const  { id, quantity  } = props.order
    const productname = props.order.product.name
    const price = props.order.product.price*quantity

    return (
        <div>
            <Card>
                <Card.Body>               
                    <Card.Title>주문번호 : {id}</Card.Title>
                    <Card.Title>제품명 : {productname}</Card.Title>
                    <Card.Title>주문한 상품 개수 : {quantity}</Card.Title>
                    <Card.Title>주문 금액 : {price}</Card.Title>
                </Card.Body>
            </Card>
            <br />
        </div>
    );
};

export default OrderItem;