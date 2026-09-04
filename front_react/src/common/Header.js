import {Button, Container, Form, Nav, Navbar, NavDropdown} from 'react-bootstrap';
import React from 'react';
import { Link } from 'react-router-dom';


const Header = () => {
    const token = localStorage.getItem("Token");
        
    return (
        <div>
        <Navbar bg="dark" variant="dark">
        <Container fluid>
            <Navbar.Brand href="/home">로고</Navbar.Brand>
            <Navbar.Toggle aria-controls="navbarScroll" />
            <Navbar.Collapse id="navbarScroll">
            <Nav
                className="me-auto my-2 my-lg-0"
                style={{ maxHeight: '100px' }}
                navbarScroll
            >
                <Link to="/home" className='nav-link'>Home</Link>

                <NavDropdown title="마이페이지" id="navbarScrollingDropdown">
                    <NavDropdown.Item href="/mypage">내 정보</NavDropdown.Item>
                    <NavDropdown.Item href="/myproductlist">내 제품</NavDropdown.Item>
                    <NavDropdown.Item href="/orderList">주문목록</NavDropdown.Item>
                    <NavDropdown.Divider />
                    <NavDropdown.Item href="/productcreate">제품 추가</NavDropdown.Item>
                </NavDropdown>
            </Nav>
            <Form className="d-flex">
                {/* 로그인 안 되어있을 경우 */}
                {!token && (<>
                    <Button variant="outline-success" className="me-3" href="/loginForm">로그인</Button>
                    <Button variant="outline-primary" href="/joinForm">회원가입</Button>
                </>)}

                {/* 로그인 되어있을 경우 */}
                {token && (<><Button variant="outline-warning" href="/logout">로그아웃</Button></>)}
                
            </Form>
            </Navbar.Collapse>
        </Container>
        </Navbar>
        </div>
    );
};

export default Header;