import { React, useState, useEffect} from 'react';
import {  useNavigate } from 'react-router-dom';


const Logout = () => {
    const token = localStorage.getItem("Token");
    const refreshToken = localStorage.getItem("refreshToken");
    
    const navigate = useNavigate();

    useEffect(() =>{
        fetch("http://localhost:8081/member/logout", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            },
            body: JSON.stringify({
                refreshToken: refreshToken
            })
        })
        .then((res) => {
            if(res.status === 200){ 
                //토큰 삭제
                localStorage.removeItem("Token");
                localStorage.removeItem("refreshToken");

                alert("로그아웃 되었습니다.");
                window.location.href = "/home";
            }
        })
    }, [])


    return (
        <div>로그아웃 중</div>
    );
};

export default Logout;