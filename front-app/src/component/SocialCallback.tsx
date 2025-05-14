import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

const SocialLoginCallback = () => {
  const navigate = useNavigate();

  useEffect(() => {
    const params = new URLSearchParams(window.location.search);
    localStorage.setItem('authType', 'oauth');
    localStorage.setItem('authProvider', 'kakao');

    const accessToken = params.get("accessToken");
    const refreshToken = params.get("refreshToken");

    if (accessToken && refreshToken) {
      localStorage.setItem("accessToken", accessToken);
      localStorage.setItem("refreshToken", refreshToken);
      navigate("/"); // 메인 페이지로 이동
    } else {
      navigate("/login"); // 에러 시 로그인 페이지로
    }
  }, [navigate]);

  return <div>소셜 로그인 처리 중...</div>;
};


export default SocialLoginCallback;
