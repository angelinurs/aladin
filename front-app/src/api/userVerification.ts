import axios from "axios";
import { API_URL } from './config';

export const sendEmailVerification = async (email: string) => {
    return axios.post(`${API_URL}/api/valid/email/send?email=${ email }`);
};

export const checkEmailVerification = async (email: string, code: string) => {
    return axios.post(`${API_URL}/api/valid/email/check`, {email, code});
};

export const checkUsernameDuplicate = async (username: string) => {
    const res = await axios.post(`${API_URL}/api/valid/username`, null, {
        params: { username }
    });
    return res.data.data;
};