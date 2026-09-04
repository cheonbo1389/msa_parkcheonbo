import { Button, Form, Container} from 'react-bootstrap';
import React,{useState} from 'react';
import {  useNavigate } from 'react-router-dom';


const LoginForm = (props) => {
    
    const navigate = useNavigate();

    const [user, setUser ] = useState({
        email : '',
        password : ''
    })

    const changeValue = (e) => {
        setUser({
            ...user,
            [e.target.name] : e.target.value
        });
    }


    const submitLogin = (e) =>{
        e.preventDefault();
        
        fetch("http://localhost:8081/member/doLogin",
        { 
          method : "POST",
          headers : {
            "Content-Type" : "application/json;charset-utf-8"
          },
          body: JSON.stringify(user)
        })
        .then((res) => {
            if(res.status === 200){
                return res.json();
            }else{
                return null;
            }   
        })
        .then((res) => {
            if (res != null && res.token) {
              // 토큰 저장
              localStorage.setItem("Token", res.token);

              // 리프레시 토큰 저장
              localStorage.setItem("refreshToken", res.refreshToken);
              window.location.href = "/home";
            }else{
                alert("로그인에 실패했습니다.");
            }
        })
        .catch((error) => {
            console.log('실패', error);
            
        })
    }

    const gotoRegister = () => {
      navigate('/joinForm');
    }

    return (
      <Container>
      <br />
      <h3>로그인</h3>
      <Form onSubmit={submitLogin}>
        <Form.Group className="mb-3" controlId="formBasicEmail">
          <Form.Label>이메일</Form.Label>
          <Form.Control type="email" placeholder="이메일을 입력해주세요"  onChange={changeValue} name="email"/>
        </Form.Group>
        <Form.Group className="mb-3" controlId="formBasicPassword">
          <Form.Label>비밀번호</Form.Label>
          <Form.Control type="password" placeholder="비밀번호를 입력해주세요" onChange={changeValue} name="password" />
        </Form.Group>
        <Button variant="success" className="me-3" type="submit">
          로그인
        </Button>
      <Button variant="primary" onClick={gotoRegister}>회원가입</Button>  
      </Form>
      
    </Container>
  );
};

export default LoginForm;