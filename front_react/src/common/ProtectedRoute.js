import { Navigate } from "react-router-dom";

function ProtectedRoute({ children }) {
    const token = localStorage.getItem("Token");

    if (!token) {
        alert("로그인이 필요합니다.");
        return <Navigate to="/loginForm" replace />;
    }

    return children;
}

export default ProtectedRoute;