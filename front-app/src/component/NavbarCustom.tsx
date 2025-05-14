import React from "react";
import { useAuth } from "../contexts/AuthContext";
import { Button, Dropdown, Navbar } from "react-bootstrap";
import { Link } from "react-router-dom";
import { MdAccountCircle } from "react-icons/md";
import { LiaFirstdraft } from "react-icons/lia";

interface NavBarProps {
    onLoginClick: () => void;
    onSignupClick: () => void;
    onUserInfoClick: () => void;
}

const NavbarCustom: React.FC<NavBarProps> = ({ onLoginClick, onSignupClick, onUserInfoClick }) => {
  const { isAuthenticated, logout, user } = useAuth();

return (
    <Navbar bg="light" data-bs-theme="light" className="d-flex p-3 fw-bolder">
      <Link to="/" className="navbar-brand">
        <LiaFirstdraft color="blue" size={40} aria-label="my" className="me-1"/>
        KorUtil.com
      </Link>
      <Navbar.Collapse className="justify-content-end">
        {!isAuthenticated ? (
          <>
            <Button onClick={onLoginClick} className="me-2 fw-bold">로그인</Button>
            <Button variant="warning" onClick={onSignupClick} className="fw-bold">회원가입</Button>
          </>
        ) : (
           <>
           <Dropdown align="end" drop="down">
            <Dropdown.Toggle 
              variant="secondary" 
              id="dropdown-my"
              className="badge d-flex align-items-center p-1 pe-2 text-primary-emphasis bg-primary-subtle border border-primary-subtle rounded-pill"
              style={{ fontSize: "20px", padding: "10px 20px" }}
            >
              <MdAccountCircle color="green" aria-label="my" className="me-1"/>
              {(user?.username || "My")}
            </Dropdown.Toggle>
            <Dropdown.Menu>
                <Dropdown.Item onClick={onUserInfoClick}>{user?.username} 정보</Dropdown.Item>
                <Dropdown.Divider />
                <Dropdown.Item onClick={logout}>로그아웃</Dropdown.Item>
            </Dropdown.Menu>

           </Dropdown>
           </>
        )}
      </Navbar.Collapse>
    </Navbar>
  );
};

export default NavbarCustom;