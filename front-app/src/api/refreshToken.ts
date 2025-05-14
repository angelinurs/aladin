import axios from 'axios';
import { API_URL } from './config';

export const refreshAccessToken = async () => {
    const refreshToken = localStorage.getItem('refreshToken');
    if (!refreshToken) throw new Error('리프레시 토큰이 없습니다');

    const response = await axios.post(`${API_URL}/auth/reissue`, { refreshToken });
    const { accessToken, refreshToken: newRefreshToken } = response.data.data;
    localStorage.setItem('accessToken', accessToken);
    localStorage.setItem('refreshToken', newRefreshToken);
    return accessToken;
};