import React from 'react';
import { useAuth } from '../contexts/AuthContext';
import { Modal, Button } from "react-bootstrap";
import Login from "./Login";
import Signup from "./Signup";
import UserInfo from './UserInfo';

interface HomeProps {
  showLoginModal: boolean;
  setShowLoginModal: (show: boolean) => void;
  showSignupModal: boolean;
  setShowSignupModal: (show: boolean) => void;
  showUserInfoModal: boolean;
  setShowUserInfoModal: (show: boolean) => void;

}

const Home: React.FC<HomeProps> = ({
  showLoginModal,
  setShowLoginModal,
  showSignupModal,
  setShowSignupModal,
  showUserInfoModal,
  setShowUserInfoModal,
}) => {
  const { isAuthenticated, user } = useAuth();

  // 로그인 성공 시 모달 닫기
  const handleLoginSuccess = () => setShowLoginModal(false);
  // 회원가입 성공 시 모달 닫기
  const handleSignupSuccess = () => {
    setShowSignupModal(false);
    setShowLoginModal(true);
  };

  return (
    <>
      {/* 로그인 모달 */}
      <Modal show={showLoginModal} onHide={() => setShowLoginModal(false)} centered>
        <Modal.Header closeButton>
          <Modal.Title>로그인</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Login onSuccess={handleLoginSuccess} />
          <div className="text-center mt-3">
            <Button variant="link" size="sm" onClick={() => {
              setShowLoginModal(false);
              setShowSignupModal(true);
            }}>
              아직 회원이 아니신가요? 회원가입
            </Button>
          </div>
        </Modal.Body>
      </Modal>

      {/* 회원가입 모달 */}
      <Modal show={showSignupModal} onHide={() => setShowSignupModal(false)} centered>
        <Modal.Header closeButton>
          <Modal.Title>회원가입</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Signup onSuccess={handleSignupSuccess} />
          <div className="text-center mt-3">
            <Button variant="link" size="sm" onClick={() => {
              setShowSignupModal(false);
              setShowLoginModal(true);
            }}>
              이미 계정이 있으신가요? 로그인
            </Button>
          </div>
        </Modal.Body>
      </Modal>

      {/* 회원정보 모달 */}
      <Modal show={showUserInfoModal} onHide={() => setShowUserInfoModal(false)} centered>
        <Modal.Header closeButton>
          <Modal.Title>{user?.username} 님</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <UserInfo />
        </Modal.Body>
      </Modal>


      {/* 본문 내용 */}
      <div className="content">
        <h1>홈 화면</h1>
        {isAuthenticated && <p>안녕하세요, {user?.username}님!</p>}
      </div>
    </>
  );
};

export default Home;
