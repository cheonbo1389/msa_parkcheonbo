import React, { useEffect, useState } from 'react';
import { Container } from 'react-bootstrap';
import MyProductItem from '../../components/MyProductItem';


const MyProductList = () => {
    const token = localStorage.getItem("Token");

    const [productList, setProductList] = useState([]);

    useEffect(() => {
        fetch("http://localhost:8081/product/mylist",{
            method: "GET" ,
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            }
        }) 
        .then(res => res.json())
        .then(res => {
            setProductList(res); 
        });
    }, [])

    return (
        <div>
            <Container> 
                <br />
                <h3>내 제품 리스트</h3>
                <br />
                {productList.map(product => 
                    <MyProductItem key={product.id} product={product} /> )
                }
            </Container>
        </div>
    );
};

export default MyProductList;