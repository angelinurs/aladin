import { Col, Form, Row } from "react-bootstrap";
import { useAuth } from "../contexts/AuthContext"

const UserInfo: React.FC = () => {
    const { user } = useAuth();

    if(!user) return <div>회원 정보를 불러올 수 없습니다.</div>;

    return (
        <Form>
            {/* 등급 */}
            {/* 
            <Form.Group as={Row} className="mb=2">
                <Form.Label column sm={3}>등급</Form.Label>
                <Col sm={9}>
                    <Form.Control value={user.role} readOnly plaintext />
                </Col>
            </Form.Group>
             */}
            {/* 아이디 */}
            <Form.Group as={Row} className="mb=2">
                <Form.Label column sm={3}>아이디</Form.Label>
                <Col sm={9}>
                    <Form.Control value={user.username} readOnly plaintext />
                </Col>
            </Form.Group>
            {/* E-mail */}
            <Form.Group as={Row} className="mb=2">
                <Form.Label column sm={3}>E-mail</Form.Label>
                <Col sm={9}>
                    <Form.Control value={user.email} readOnly plaintext />
                </Col>
            </Form.Group>
        </Form>
    );
};

export default UserInfo;