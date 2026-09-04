import { Button, Container, Form} from 'react-bootstrap';
import React,{useState} from 'react';
import {  useNavigate } from 'react-router-dom';


const JoinForm = (props) => {
    
    const navigate = useNavigate();

    const [user, setUser ] = useState({
        email : '',
        password : '',
        name : ''
    })

    const changeValue = (e) => {
        setUser({
            ...user,
            [e.target.name] : e.target.value
        });
    }

    const submitRegister = (e) =>{
        e.preventDefault();

        fetch("http://localhost:8081/member/create",
        { 
          method : "POST",
          headers : {
            "Content-Type" : "application/json;charset-utf-8"
          },
          body: JSON.stringify(user)
        })
        .then((res) => {
            if(res.status === 201){
                return res.json();
            }else{
                return null;
            }   
        })
        .then((res) => {
            if(res != null){
              alert("회원가입에 성공했습니다.");
              navigate('/home');
            }else{
              alert("회원가입에 실패했습니다.");
            }
        })
        .catch((error) => {
            console.log('실패', error);
            
        })
    }

  return (
    <Container>
      <br />
      <h3>회원가입</h3>
      <Form onSubmit={submitRegister}>
        <Form.Group className="mb-3" controlId="formBasicEmail">
          <Form.Label>이메일</Form.Label>
          <Form.Control type="email" placeholder="이메일을 입력해주세요"  onChange={changeValue} name="email"/>
        </Form.Group>

        <Form.Group className="mb-3" controlId="formBasicPassword">
          <Form.Label>비밀번호</Form.Label>
          <Form.Control type="password" placeholder="비밀번호를 입력해주세요" onChange={changeValue} name="password" />
        </Form.Group>
        
        <Form.Group className="mb-3" controlId="formBasicNickname">
          <Form.Label>이름</Form.Label>
          <Form.Control type="text" placeholder="이름를 입력해주세요" onChange={changeValue} name="name" />
        </Form.Group>

        <Button variant="primary" type="submit">
          회원가입
        </Button>
      </Form>
    </Container>
  );
};

export default JoinForm;