import React, { useEffect, useState } from "react";

import { KAKAO_BUTTON_IMG } from "../api/config";
import { Link } from "react-router-dom";
import { Button, Image } from "react-bootstrap";
import { LoginCallbackUrlRequest } from "../api/auth";

const KakaoLoginButton: React.FC = () => {
    const [kakaoAuthUrl, setKakaoAuthUrl] = useState<string | null>(null);

    useEffect(() => {
        const fetchKakaoAuthUrl = async () => {
            try {
                const response = await LoginCallbackUrlRequest("kakao");
                setKakaoAuthUrl(response.data); // URL을 상태로 업데이트
            } catch (error) {
                console.error("Social 로그아웃 요청 중 오류:", error);
            }
        };

        fetchKakaoAuthUrl(); // 컴포넌트 마운트 시 URL을 비동기로 가져옵니다.
    }, []); // 빈 배열을 의존성으로 설정하여 최초 한 번만 실행되게 합니다.
    
    return (
        <Link to={!!kakaoAuthUrl? kakaoAuthUrl : '' }>
            <Button 
                style={{ 
                    background: "#FEE500", 
                    borderRadius: 8, 
                    padding: "10px 20px", 
                    border: "none" }}
                    disabled={!kakaoAuthUrl}
                    >
                        <Image 
                            src={KAKAO_BUTTON_IMG}
                            alt="KAKAO_LOGIN_BUTTON"
                            style={{ width: 20, marginRight: 8 }} />
                    </Button>
        </Link>
    );
};

export default KakaoLoginButton;