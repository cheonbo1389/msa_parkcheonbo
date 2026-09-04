import { Button, Container} from 'react-bootstrap';
import { React, useState, useEffect} from 'react';
import {  useNavigate } from 'react-router-dom';
import { Card } from 'react-bootstrap';

const Mypage = () => {
    const token = localStorage.getItem("Token");
    const navigate = useNavigate();

    const [myinfo, setMyinfo] = useState({
            name : '',
            email : ''
        });


    useEffect(() => {
        fetch("http://localhost:8081/member/mypage",{
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            }
        }) 
        .then(res => res.json())
        .then(res => {
            setMyinfo(res); 
        });
    }, [])


    const updateMyinfo = () => {
      navigate('/updatemyinfo');
    }

    return (
        <Container>
            <div>
                <br />
                <h3>내 정보</h3>
                <br />
                <Card>
                    <Card.Body>               
                        <Card.Title>이름 : {myinfo.name}</Card.Title>
                        <Card.Title>이메일 : {myinfo.email}</Card.Title>
                        <Button variant="primary" onClick={updateMyinfo}>수정</Button>  
                    </Card.Body>
                </Card>
            </div>
        </Container>
    );
};

export default Mypage;