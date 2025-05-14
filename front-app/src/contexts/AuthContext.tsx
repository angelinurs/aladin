import React, { createContext, useContext, useState, useEffect, ReactNode} from "react";
import { useNavigate } from "react-router-dom";
import { jwtDecode } from 'jwt-decode';
import { login as apiLogin, signup as apiSignup, logout as apiLogout, LogoutCallbackUrlRequest } from '../api/auth';
import { LoginRequest } from "../types/auth/LoginTypes";

import { User } from "../types/auth/User";
import { SignupRequest } from "../types/auth/SignupTypes";

interface AuthContextType {
    isAuthenticated: boolean;
    loading: boolean;
    user: User | null;
    login: (data: LoginRequest) => Promise<void>;
    signup: (data: SignupRequest) => Promise<void>;
    logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const useAuth = () => {
    const context = useContext(AuthContext);
    if ( !context) {
        throw new Error('useAuth 는 AuthProvider 내부에서만 사용할 수 있습니다.');
    }
    return context;
};

interface AuthProviderProps {
    children: ReactNode;
}

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
    const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);
    const [loading, setLoading] = useState<boolean>(true);
    const [user, setUser] = useState<User | null>(null);
    const navigate = useNavigate();

    // 토큰 만료 시간 계산 함수
    const getTokenExpirationTime = (token: string): number | null => {
        try {
            const decodedToken: any = jwtDecode(token);
            if (!decodedToken.exp) return null;
            return decodedToken.exp * 1000 - Date.now();
        } catch (error) {
            return null;
        }
    };

    // 초기 로딩 시 토큰 확인
    useEffect(() => {
        const token = localStorage.getItem('accessToken');
        const userStr = localStorage.getItem('user');
        setIsAuthenticated(!!token);
        setUser( userStr ? JSON.parse(userStr) : null );
        setLoading(false);
    }, []);

    // 토큰 만료 감지 로직 (추가된 부분)
    useEffect(() => {
        if (!isAuthenticated) return;

        const token = localStorage.getItem('accessToken');
        if (!token) return;

        const expiresIn = getTokenExpirationTime(token);
        if (!expiresIn || expiresIn <= 0) return;

        // 만료 1분 전 경고
        const warningTimer = setTimeout(() => {
            alert('로그인 세션이 곧 만료됩니다. 계속 사용하시려면 로그인을 다시 해주세요.');
        }, expiresIn - 60000);

        // 만료 시 로그아웃
        const logoutTimer = setTimeout(() => {
            logout();
            navigate('/login');
        }, expiresIn);

        return () => {
            clearTimeout(warningTimer);
            clearTimeout(logoutTimer);
        };
    }, [isAuthenticated, navigate]); // 의존성 배열 추가

    // 로그인 함수
    const login = async (data: LoginRequest) => {
        try {
            const response = await apiLogin(data);
            const { accessToken, refreshToken, user } = response.data;
            localStorage.setItem('authType', 'jwt');
            localStorage.setItem('accessToken', accessToken);
            localStorage.setItem('refreshToken', refreshToken);
            localStorage.setItem('user', JSON.stringify(user));

            setIsAuthenticated(true);
            setUser(user);

            return response;
        } catch (error) {
            throw error;
        }
    };

    // 회원가입 함수
    const signup = async (data: SignupRequest) => {
        try {
            return await apiSignup(data);
        } catch (error) {
            throw error;
        }
    };

    // 로그아웃 함수
    const logout = async () => {
        const authType = localStorage.getItem('authType');
        const oAuthProvider = localStorage.getItem('authProvider');
        
        if (authType === 'oauth' && !!oAuthProvider) {
            try {
                const response = await LogoutCallbackUrlRequest(oAuthProvider);
                const logoutCallbackUrl = response.data;
                window.location.href = logoutCallbackUrl;
            } catch (error) {
                console.error('Social 로그아웃 요청 중 오류:', error);
            } finally {
                localStorage.removeItem('authType');
                localStorage.removeItem('oAuthProvider');
                localStorage.removeItem('accessToken');
                localStorage.removeItem('refreshToken');
                localStorage.removeItem('user');
                setIsAuthenticated(false);
                setUser(null);
            }
        }
        else {
            try {
                await apiLogout();
            } catch (error) {
                console.error('로그아웃 요청 중 오류:', error);
            } finally {
                localStorage.removeItem('authType');
                localStorage.removeItem('oAuthProvider');
                localStorage.removeItem('accessToken');
                localStorage.removeItem('refreshToken');
                localStorage.removeItem('user');
                setIsAuthenticated(false);
                setUser(null);
            }

        }
    }

    const value = {
        isAuthenticated,
        loading,
        user,
        login,
        signup,
        logout
    };

    return (
        <AuthContext.Provider value={value}>
            {children}
        </AuthContext.Provider>
    );
};

