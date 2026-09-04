import React, { useEffect, useState } from 'react';
import { Container } from 'react-bootstrap';
import OrderItem from '../../components/OrderItem';


const OrderList = () => {
    const token = localStorage.getItem("Token");
    const [orderList, setOrderList] = useState([]);

    useEffect(() => {
        fetch("http://localhost:8081/ordering/orderingList",{
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            }
        }) 
        .then(res => res.json())
        .then(res => {
            setOrderList(res); 
        });
    }, [])

    return (
        <div>
            <Container> 
                <br />
                <h3>주문 목록</h3>
                <br />
                {orderList.map(order => 
                    <OrderItem key={order.id} order={order} /> )
                }
            </Container>
        </div>
    );
};

export default OrderList;