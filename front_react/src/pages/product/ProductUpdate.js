import React,{useEffect, useState} from 'react';
import { Button, Container, Form} from 'react-bootstrap';
import {  useNavigate, useParams } from 'react-router-dom';



const ProductUpdate = (props) => {

    const propsParam = useParams(); 
    const navigate = useNavigate();
    const token = localStorage.getItem("Token");
    const id = propsParam.id;
    const [product, setProduct] = useState({
    });


    useEffect(() => {
        fetch("http://localhost:8081/product/detail/"+id,{
            method: "GET" ,
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            }
        }) 
        .then(res => res.json())
        .then(res => {
            setProduct(res);             
        });
    }, [])

    const changeValue = (e) => {
        setProduct({
            ...product,
            [e.target.name] : e.target.value
        });
    }


    const submitProductUpdate = (e) =>{
        e.preventDefault(); 

        fetch("http://localhost:8081/product/update/"+id,
        { 
            method : "PUT", 
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            },
            body: JSON.stringify(product) 
        })
        .then((res) => { 
            if(res.status === 200){
                return res.json();
            }else{
                return null;
            }
            
        })
        .then((res) => {
            if(res != null){
                alert("제품 수정에 성공했습니다.");
                navigate("/product/"+id); 
            }else{
                alert("제품 수정에 실패했습니다.");
            }
        })
        .catch((error) => {
            console.log('실패', error);
            
        })
    };

    return (
        <div>
            <Container>
                <br />
                <h3>제품 수정</h3>
                <Form onSubmit={submitProductUpdate}>
                    <Form.Group className="mb-3" controlId="formId">
                            <Form.Label>제품번호</Form.Label>
                            <Form.Control type="text" placeholder="제품 번호" onChange={changeValue} name="id" value={id} disabled/>
                    </Form.Group>
                    <Form.Group className="mb-3" controlId="formName">
                            <Form.Label>제품명</Form.Label>
                            <Form.Control type="text" placeholder="제품명 입력" onChange={changeValue} name="name" value={product.name} />
                    </Form.Group>
                    <Form.Group className="mb-3" controlId="formPrice">
                            <Form.Label>제품가격</Form.Label>
                            <Form.Control type="text" placeholder="제품가격 입력" onChange={changeValue} name="price" value={product.price} />
                    </Form.Group>
                    <Form.Group className="mb-3" controlId="formStockQuantity">
                            <Form.Label>제품재고</Form.Label>
                            <Form.Control type="text" placeholder="제품재고 입력" onChange={changeValue} name="stockQuantity" value={product.stockQuantity} />
                    </Form.Group>
                    <Button variant="primary" type="submit">
                        수정
                    </Button>
                </Form>
            </Container>
        </div>
    );
};

export default ProductUpdate;