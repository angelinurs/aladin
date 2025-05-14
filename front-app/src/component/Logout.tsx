// src/component/Logout.tsx
import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { logout } from "../api/auth";

const Logout: React.FC = () => {
  const navigate = useNavigate();

  useEffect(() => {
    (async () => {
      try {
        await logout();
      } catch (e) {
        // 필요시 에러 핸들링
        console.error(e);
      } finally {
        navigate("/login", { replace: true });
      }
    })();
  }, [navigate]);

  return null; // 혹은 <div>로그아웃 중...</div>
};

export default Logout;
