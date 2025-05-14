import axios from "axios";

import { LoginRequest } from "../types/auth/LoginTypes";
import { API_URL } from './config';
import { SignupRequest } from "../types/auth/SignupTypes";

const axiosInstance = axios.create({
    baseURL: API_URL,
    headers: {
        'Content-Type': 'application/json',
    },
});

// Axios 인터셉터를 사용하여 모든 요청에 User-Agent를 자동으로 추가
axiosInstance.interceptors.request.use(
    (config) => {
        // const userAgent = navigator.userAgent; // 브라우저의 User-Agent 값을 가져옴
        // if (config.headers) {
        //     config.headers['User-Agent'] = userAgent; // 모든 요청에 User-Agent를 추가
        // }
        return config;  // 요청이 진행되도록 설정
    },
    (error) => {
        return Promise.reject(error);  // 오류가 있으면 오류 처리
    }
);

export const login = async (data: LoginRequest) => {
    try {

        const response = await axiosInstance.post('/users/login', data);

        const { accessToken, refreshToken } = response.data.data;  // 응답에서 토큰 받기
        localStorage.setItem('accessToken', accessToken);  // accessToken 저장
        localStorage.setItem('refreshToken', refreshToken);  // refreshToken 저장

        return response.data;
    } catch (error: any) {
        console.error('Login Error:', error);
        throw error?.response?.data?.message || '로그인 실패';
    }
};

export const signup = async (data: SignupRequest ) => {
    try {
        const response = await axiosInstance.post('/users/signup', data);
        return response.data;
    } catch (error: any) {
      if (error?.response) {
          throw error?.response?.data?.message || '회원가입 실패';
      } else {
          console.error('Error with request:', error);
          // eslint-disable-next-line no-throw-literal
          throw '회원가입 실패';
      }
    }
};

// 인증된 요청 보내기 (JWT 포함)
export const apiRequest = async <T>(endpoint: string, method: 'GET' | 'POST' = 'GET', data?: any): Promise<T> => {
    const token = localStorage.getItem('accessToken'); 
    const headers = token ? { Authorization: `Bearer ${token}` } : {};
  
    try {
        const response = await axiosInstance({
          url: endpoint,
          method,
          headers: {
              ...headers,  // JWT 포함 헤더
          },
          data,
        });
      
        return response.data;
    } catch (error: any) {
        console.error('API Request Error:', error);
        throw error?.response?.data?.message || 'API 요청 실패';
    }
};

export const logout = async () => {
    const accessToken = localStorage.getItem('accessToken');
  
    try {
      await axiosInstance.post(
        '/users/logout',
        {},
        {
          headers: {
            Authorization: `Bearer ${accessToken}`,
          },
        }
      );
    } catch (error: any) {
      // 에러 상황별로 분기 처리 가능
      if (error.response) {
        // 서버가 4xx, 5xx 응답을 반환한 경우
        console.error('Logout error response:', error.response);
      } else if (error.request) {
        // 요청은 되었으나 응답을 받지 못한 경우
        console.error('Logout network error:', error.request);
      } else {
        // 요청 설정 중에 오류 발생
        console.error('Logout error:', error.message);
      }
    } finally {
      // 토큰은 항상 삭제 (성공/실패와 무관)
      localStorage.removeItem('accessToken');
      localStorage.removeItem('refreshToken');
      localStorage.removeItem('authProvider');
      localStorage.removeItem('user');
    }
  };

export const LoginCallbackUrlRequest = async (provider: string) => {

  try {
    const response = await axiosInstance.post(`/api/auth/provider/login/${provider}`);
    return response.data;
  } catch (error: any) {
    if (error?.response) {
        throw error?.response?.data?.message || `OAuth ${provider} 없음`;
    } else {
        console.error('Error with request:', error);
        // eslint-disable-next-line no-throw-literal
        throw 'OAuth 로그인 실패';
    }
  }
};

export const LogoutCallbackUrlRequest = async (provider: string) => {

  try {
    const response = await axiosInstance.post(`/api/auth/provider/logout/${provider}`);
    return response.data;
  } catch (error: any) {
    if (error?.response) {
        throw error?.response?.data?.message || `OAuth ${provider} 없음`;
    } else {
        console.error('Error with request:', error);
        // eslint-disable-next-line no-throw-literal
        throw 'OAuth 로그아웃 실패';
    }
  }
};