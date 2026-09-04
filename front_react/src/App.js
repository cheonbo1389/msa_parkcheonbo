import './App.css';
import Header from './common/Header';
import { BrowserRouter,Route,Routes } from 'react-router-dom';
import JoinForm from './pages/user/JoinForm';
import LoginForm from './pages/user/LoginForm';
import ProductList from './pages/product/ProductList';
import ProductCreate from './pages/product/ProductCreate';
import OrderCreate from './pages/order/OrderCreate';
import OrderList from './pages/order/OrderList';
import Mypage from './pages/user/Mypage';
import ProductDetail from './pages/product/ProductDetail';
import ProductUpdate from './pages/product/ProductUpdate';
import MyProductList from './pages/product/MyProductList';
import UpdateMyinfo from './pages/user/UpdateMyinfo';
import ProtectedRoute from './common/ProtectedRoute';
import Logout from './pages/user/Logout';

function App() {
  return (
    <div className="App">
      <BrowserRouter>
        <Header />

        <Routes>
          {/* 토큰 필수 아닌 페이지 */}
          {/* home화면 ProductList로 이동 */} 
          <Route path='/home' exact={true} element={<ProductList />} /> 

          {/* 회원가입 */} 
          <Route path='/joinForm' exact={true} element={<JoinForm />} /> 

          {/* 로그인 */} 
          <Route path='/loginForm' exact={true} element={<LoginForm />} /> 

          {/* 제품 리스트 */}
          <Route path='/productlist' exact={true} element={<ProductList />} /> 

          {/* 제품 상세 페이지*/}
          <Route path='/product/:id' exact={true} element={<ProductDetail />} /> 



          {/* 토큰 필수 페이지 */}
          {/* 로그아웃 */}
          <Route path='/logout' exact={true} element={<ProtectedRoute><Logout /></ProtectedRoute>} /> 

          {/* 마이페이지 - 내 정보 */}
          <Route path='/mypage' exact={true} element={ <ProtectedRoute><Mypage /></ProtectedRoute>} /> 

          {/* 내정보 수정 페이지 */}
          <Route path='/updatemyinfo' exact={true} element={ <ProtectedRoute><UpdateMyinfo /></ProtectedRoute>} /> 

          {/* 제품 추가 */}
          <Route path='/productcreate' exact={true} element={ <ProtectedRoute><ProductCreate /></ProtectedRoute>} /> 

          {/* 제품 수정 페이지 */}
          <Route path='/updateProduct/:id' exact={true} element={ <ProtectedRoute><ProductUpdate /></ProtectedRoute>} />

          {/* 내가 추가한 제품 페이지 */}
          <Route path='/myproductlist' exact={true} element={ <ProtectedRoute><MyProductList /></ProtectedRoute>} /> 

          {/* 주문 추가 */}
          <Route path='/ordercreate/:id' exact={true} element={ <ProtectedRoute><OrderCreate /></ProtectedRoute>} />

          {/* 주문 목록 */}
          <Route path='/orderList' exact={true} element={ <ProtectedRoute><OrderList /></ProtectedRoute>} /> 
          
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;
