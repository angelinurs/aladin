import { useState } from 'react';
import { Routes, Route } from 'react-router-dom';
import Home from './component/Home';
import { AuthProvider } from './contexts/AuthContext';
import NavbarCustom from './component/NavbarCustom';
import KakaoCallback from './component/SocialCallback';

const App: React.FC = () => {
  const [showLoginModal, setShowLoginModal] = useState(false);
  const [showSignupModal, setShowSignupModal] = useState(false);
  const [showUserInfoModal, setShowUserInfoModal] = useState(false);

  return (
    <AuthProvider>
      <NavbarCustom 
        onLoginClick={() => setShowLoginModal(true)}
        onSignupClick={() => setShowSignupModal(true)}
        onUserInfoClick={() => setShowUserInfoModal(true)}
      />
      <Routes>
        <Route path="/" element={
          <Home 
            showLoginModal={showLoginModal}
            showSignupModal={showSignupModal}
            showUserInfoModal={showUserInfoModal}
            setShowLoginModal={setShowLoginModal}
            setShowSignupModal={setShowSignupModal}
            setShowUserInfoModal={setShowUserInfoModal}
          />} 
        />
        <Route path="/oauth/callback" element={<KakaoCallback />} />
      </Routes>
    </AuthProvider>
  );
};

export default App;
