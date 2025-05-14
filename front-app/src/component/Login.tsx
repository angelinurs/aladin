import React, { useState } from "react";
import { useAuth } from "../contexts/AuthContext";

import { LoginRequest } from "../types/auth/LoginTypes"

import { Button, Form, Alert } from "react-bootstrap";
import KakaoLoginButton from "./SocialLoginButtonKakao";

interface LoginProps {
    onSuccess?: () => void;
}

const Login: React.FC<LoginProps> = ({onSuccess}) => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');

    const { login } = useAuth();

    const handleSubmit = async ( e: React.FormEvent ) => {
        e.preventDefault();
        setError('');

        try {
            const loginData: LoginRequest = {username, password};
            await login(loginData);
            if (onSuccess) onSuccess();

        } catch (error: any) {
            setError(error?.response?.data?.message || '로그인에 실패했습니다.');
        }
    };

    return (
        <Form onSubmit={handleSubmit}>
          <Form.Group className="mb-3">
            <Form.Label>아이디</Form.Label>
            <Form.Control
              type="text"
              value={username}
              onChange={e => setUsername(e.target.value)}
              required
            />
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>비밀번호</Form.Label>
            <Form.Control
              type="password"
              value={password}
              onChange={e => setPassword(e.target.value)}
              required
            />
          </Form.Group>
          {error && <Alert variant="danger">{error}</Alert>}
          <Button type="submit" className="w-100">로그인</Button>
          <div className="my-3 text-center">
            <KakaoLoginButton />
          </div>
        </Form>
    );
};

export default Login;
