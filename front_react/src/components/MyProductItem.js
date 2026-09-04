import React from 'react';
import { Link } from 'react-router-dom';
import {Card} from 'react-bootstrap';


const MyProductItem = (props) => {
    const  { id, name, price, stockQuantity } = props.product

    return (
        <div>
            <Card>
                <Card.Body>               
                    <Card.Title>제품번호 : {id}</Card.Title>
                    <Card.Title>제품명 : {name}</Card.Title>
                    <Card.Title>제품가격 : {price}</Card.Title>
                    <Card.Title>재고 : {stockQuantity}</Card.Title>
                    <Link to={"/updateProduct/"+id} className="btn btn-warning">수정</Link>
                    </Card.Body>
            </Card>
            <br />
        </div>
    );
};

export default MyProductItem;