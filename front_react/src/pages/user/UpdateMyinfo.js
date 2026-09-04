import { Button, Form, Container} from 'react-bootstrap';
import { React, useState, useEffect} from 'react';
import {  useNavigate } from 'react-router-dom';


const UpdateMyinfo = () => {
    const token = localStorage.getItem("Token");

    const navigate = useNavigate();

    const [myinfo, setMyinfo] = useState({});


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

    const changeValue = (e) => {
        setMyinfo({
            ...myinfo,
            [e.target.name] : e.target.value
        });
    }

    const myinfoUpdate = (e) => {
        e.preventDefault(); 

        fetch("http://localhost:8081/member/updatemyinfo",
        { 
            method : "PUT", 
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            },
            body: JSON.stringify(myinfo) 
        })
        .then((res) => { 
            if(res.status === 200){
                return res.json();
            }else{
                return null;
            }
            
        })
        .then((res) => {
            console.log("정상",res);
            if(res != null){
                alert("내정보 수정에 성공했습니다.");
                navigate("/mypage"); 
            }else{
                alert("제품 수정에 실패했습니다.");
            }
        })
        .catch((error) => {
            console.log('실패', error);
            
        })
    }

    return (
        <Container>
                <br />
                <h3>내 정보 수정</h3>
                <Form onSubmit={myinfoUpdate}>
                    <Form.Group className="mb-3" controlId="formName">
                            <Form.Label>이름</Form.Label>
                            <Form.Control type="text" placeholder="제품명 입력" onChange={changeValue} name="name" value={myinfo.name} />
                    </Form.Group>
                    <Form.Group className="mb-3" controlId="formEmail">
                            <Form.Label>이메일</Form.Label>
                            <Form.Control type="text" placeholder="제품가격 입력" onChange={changeValue} name="email" value={myinfo.email} disabled/>
                    </Form.Group>
                    <Button variant="primary" type="submit">
                        수정
                    </Button>
                </Form>
            </Container>
    );
};

export default UpdateMyinfo;