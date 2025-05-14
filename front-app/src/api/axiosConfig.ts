import axios, { AxiosError, AxiosRequestConfig }  from 'axios';
import { refreshAccessToken } from './refreshToken';
import { API_URL } from './config';

const axiosInstance = axios.create({
    baseURL: API_URL,
    headers: {
        'Content-Type': 'application/json',
    },
});

// 요청 인터셉터: 모든 요청에 토큰 추가
axiosInstance.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('accessToken');
        if ( token && config.headers) {
            config.headers['Authorization'] = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// 응답 인터셉터: 토큰 만료 처리
axiosInstance.interceptors.response.use(
    (response) => response,
    async (error:AxiosError) => {
        const originalRequest = error.config as AxiosRequestConfig & { _retry?: boolean };
        
        // 401 에러이고 재시도하지 않은 요청인 경우
        if (error.response?.status === 401 && originalRequest._retry) {
            originalRequest._retry = true;

            try {
                // refreshAccessToken 유틸을 사용해서 토큰 재발급
                const newAccessToken = await refreshAccessToken();
                if (originalRequest.headers) {
                    originalRequest.headers['Authorization'] = `Bearer ${newAccessToken}`;
                }
                return axiosInstance(originalRequest);
            } catch (refreshError) {
                // 리프레시 토큰도 만료된 경우 로그아웃 처리
                localStorage.removeItem('accessToken');
                localStorage.removeItem('refreshToken');
                window.location.href = '/login';
                return Promise.reject(refreshError);
            }
        }
        return Promise.reject(error);
    }
);

export default axiosInstance;