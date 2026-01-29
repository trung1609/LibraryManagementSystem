import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Login from './pages/Login';
import ForgotPassword from './pages/ForgotPassword';
import ResetPassword from './pages/ResetPassword';
import './App.css';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/forgot-password" element={<ForgotPassword />} />
        <Route path="/reset-password" element={<ResetPassword />} />
      </Routes>
    </BrowserRouter>
  );
}

function Home() {
  return (
    <div style={{ textAlign: 'center', padding: '50px' }}>
      <h1>Library Management System</h1>
      <p>Welcome to the Library Management System</p>
      <a href="/login" style={{ color: '#3182ce' }}>Login</a>
    </div>
  );
}

export default App;
