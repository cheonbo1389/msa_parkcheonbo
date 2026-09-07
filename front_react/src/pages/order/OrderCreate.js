import React, { useEffect, useState } from 'react';
import { useNavigate,useParams } from 'react-router-dom';
import { Button, Container, Form, Card} from 'react-bootstrap';

const OrderCreate = () => {
    const propsParam = useParams(); 
    const navigate = useNavigate(); 
    const token = localStorage.getItem("Token");

    const [seller, setSeller] = useState('');
    const [product, setProduct ] = useState({
        name : '',
        price : '',
        stockQuantity : '',
        
    })


    const [order, setOrder ] = useState({
        productId : propsParam.id,
        productCount : ''
    })

    const changeValue = (e) => {
        setOrder({
            ...order,
            [e.target.name] : e.target.value
        });
    }

    useEffect(() => {
            fetch("http://localhost:8081/product-service/product/detail/"+order.productId,{
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
                
            fetch("http://localhost:8081/member-service/member/mypage",{
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            }
            }) 
            .then(res2 => res2.json())
            .then(res2 => {
                console.log(res2);
                
                setSeller(res2); 
            });
            })
    }, []);

    const submitOrder = (e) =>{
        e.preventDefault();
        const token = localStorage.getItem("Token");

        fetch("http://localhost:8081/ordering-service/ordering/create", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            },
            body: JSON.stringify(order)
        })
        .then((res) => {
            if (res.status === 201) {
                return res.json();
            } else {
                throw new Error(`주문 실패: ${res.status}`);
            }
        })
        .then((orderId) => {
            alert("주문에 성공했습니다.");

            navigate("/productlist");
        })
        .catch((error) => {
            console.error("실패:", error);
            alert("주문에 실패했습니다.");
        });
    }


    return (
        <div>
            <Container>
            {product && seller && order && (
                    <>
                <br />
                <h3>제품 주문</h3>
                <Form onSubmit={submitOrder}>
                    <Card>
                        <Container>
                            <br />
                            <Form.Group className="mb-3" controlId="ProductName">
                                <Form.Label>제품명 : {product.name}</Form.Label>
                            </Form.Group>

                            <Form.Group className="mb-3" controlId="ProductSeller">
                                <Form.Label>판매자 : {seller.name}</Form.Label>
                            </Form.Group>

                            <Form.Group className="mb-3" controlId="ProductPrice">
                                <Form.Label>개당 가격 : {product.price} </Form.Label>
                            </Form.Group>

                            <Form.Group className="mb-3" controlId="ProductstockQuantity">
                                <Form.Label>남은 재고 : {product.stockQuantity} </Form.Label>
                            </Form.Group>
                        </Container>
                    </Card>
                            
                    <br />
                    <Form.Group className="mb-3" controlId="ProductCount">
                        <Form.Label><h3>구매할 제품 수량</h3></Form.Label>
                        <Form.Control type="number" placeholder="구매할 제품 수량 입력" onChange={changeValue} name="productCount" />
                    </Form.Group>
                    <Button variant="primary" type="submit">
                        주문
                    </Button>
                </Form> 
                </>)}
            </Container>
        </div>
    );
};

export default OrderCreate;
