import React, { useEffect, useState } from "react";
import { useForm, useWatch } from "react-hook-form";

import { useAuth } from "../contexts/AuthContext";

import { AddressFields, SignupFormValues, SignupRequest } from "../types/auth/SignupTypes";
import { signupFieldConfig, ERROR_MESSAGES, handleNameChange, handleUsernameChange, handleEmailChange, handlePhoneChange } from "../types/auth/validationRules";
import { Alert, Button, Col, Form, Row, InputGroup, Spinner } from "react-bootstrap";
import { MdCancel, MdCheckCircle } from "react-icons/md";

import { sendEmailVerification, checkEmailVerification, checkUsernameDuplicate } from "../api/userVerification";
import AddressInput from "./Postcode";
import { inputGroupTextWidth } from "../types/style/ComponentStyle"

interface SignupProp {
  onSuccess?: () => void;
}

const Signup: React.FC<SignupProp> = ({onSuccess}) => {
  const { signup } = useAuth();

  const [usernameError, setUsernameError] = useState<string | null>(null);
  const [emailError, setEmailError] = useState<string | null>(null);
  const [signupError, setSignupError] = useState<string | null>(null);

  const [emailVerified, setEmailVerified] = useState(false);
  const [verificationCode, setVerificationCode] = useState('');
  const [emailSent, setEmailSent] = useState(false);
  const [loading, setLoading] = useState(false);

  const [usernameChecked, setUsernameChecked] = useState(false);
  const [checkingUsername, setCheckingUsername] = useState(false);

  const [address, setAddress] = useState<AddressFields>({
    postcode: "",
    roadAddress: "",
    jibunAddress: "",
    detailAddress: "",
    extraAddress: "",
    // guide: "",
  });

  const {
    register,
    handleSubmit,
    setValue,
    setError,
    clearErrors,
    // watch,
    control,
    formState: { errors }
  } = useForm<SignupFormValues>({
    defaultValues: {
      emailVerified: false,
      address: {
        postcode: "",
        roadAddress: "",
        jibunAddress: "",
        detailAddress: "",
        extraAddress: ""
      }
    }
  });

  useEffect(() => {
    setValue("address", address);
  }, [address, setValue]);

  const username = useWatch({ name: "username", control });
  const name = useWatch({ name: "name", control });
  const password = useWatch({ name: "password", control });
  const confirmPassword = useWatch({ name: "confirmPassword", control });
  const email = useWatch({ name: "email", control });
  const phone = useWatch({ name: "phone", control });

  const overUsernameLength = username && username.length < 8; 
  const oveNameLength = name && name.length < 2; 
  const isPasswordMatch = !!confirmPassword && password === confirmPassword;
  const validEmail = email && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
  const validPassword = password && !/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])/.test(password);
  const overPasswordLength = password && password.length < 8; 
  const validPhoneNumber = phone && !/^[0-9]{10,11}$/.test(phone) && phone.length === 8;

  useEffect(() => {
    // username이 변경될 때마다 중복 확인을 다시 하도록 설정
    if (username) {
      setUsernameChecked(false); // 아이디가 바뀌면 중복 확인 상태를 리셋
    }

    // id 검증
    if (overUsernameLength) {
      setError("username", {
        type: "text",
        message: ERROR_MESSAGES.MIN_ID,
      });
      setUsernameChecked(false);
    } else {
      setUsernameChecked(true);
      clearErrors("username");
    }

  }, [username, overUsernameLength, setError, clearErrors]);

  useEffect(() => {

    // 이메일 값이 변경될 때 인증 상태를 초기화합니다.
    if (email) {
      setEmailVerified(false);
      setEmailSent(false);
      setVerificationCode('');
    }

    // email 검증
    if (validEmail) {
      setError('email', {
        type: 'text',
        message: ERROR_MESSAGES.INVALID_EMAIL,
      });
    } else {
      clearErrors('email');
    }

  }, [email, validEmail, setError, clearErrors]);
  
  useEffect(() => {

    // id 검증
    if (oveNameLength) {
      setError("name", {
        type: "text",
        message: ERROR_MESSAGES.MIN_NAME,
      });
    } else {
      clearErrors("name");
    }

    // 전화번호 검증
    if (validPhoneNumber) {
      setError("phone", {
        type: "manual",
        message: ERROR_MESSAGES.NOT_MATCH_PHONE,
      });
    } else {
      clearErrors("phone");
    }
    
    // 비밀번호 검증
    if (overPasswordLength) {
      setError("password", {
        type: "manual",
        message: ERROR_MESSAGES.MIN_PASSWORD,
      });
    } else {
      clearErrors("password");
    }

    if (validPassword) {
      setError("password", {
        type: "manual",
        message: ERROR_MESSAGES.INVALID_PASSWORD,
      });
    } else {
      clearErrors("password");
    }

  }, [phone, password, confirmPassword, overUsernameLength, oveNameLength, validEmail, validPhoneNumber, overPasswordLength, validPassword, setError, clearErrors]);

  const handleCheckUsername = async () => {
    if (!username) {
      setUsernameError("아이디를 입력하세요");
      setUsernameChecked(false);
      return;
    }
    setCheckingUsername(true);
    setUsernameError(null);

    try {
      const isValid = await checkUsernameDuplicate(username);
      if (!isValid) {
        setUsernameChecked(true);
        setUsernameError(null);
      } else {
        setUsernameChecked(false);
        setUsernameError("이미 사용 중인 아이디 입니다.");
      }
    } catch {
      setUsernameChecked(false);
      setUsernameError("중복 확인중 오류가 발생했습니다.")
    } finally {
      setCheckingUsername(false);
    }
  };

  // 이메일 인증 요청
  const handleEmailVerify = async () => {
    try {
      setLoading(true);
      const res = await sendEmailVerification(email);
      if (res.data.data) {
        setEmailSent(true);
        setEmailError(null);
        clearErrors('email');
      } else {
        setError('email', {
          type: 'manual',
          message: '유효하지 않은 인증 코드입니다.',
        });
      }
    } catch ( error: any) {
      setEmailError(error.response?.data?.message || "이메일 인증 실패");
    } finally {
      setLoading(false);
    }
  };

  // 인증 코드 확인
  const handleCodeVerification = async () => {
    try {
      setLoading(true);
      const res = await checkEmailVerification(email, verificationCode);
      if (res.data.data) {
        setEmailVerified(true);
        setEmailError(null);
      } else {
        setEmailError("유효하지 않은 인증코드 입니다.");
      }
    } catch (error: any) {
      setEmailError(error?.response?.data?.message || "인증 확인 실패");
    } finally {
      setLoading(false);
    }
  };

  const onSubmit = async (data: SignupFormValues) => {
    setSignupError(null);

    if (!usernameChecked) {
      setSignupError("아이디 중복 확인을 해주세요.");
      return;
    }
    if (!emailVerified) {
      setSignupError("이메일 인증을 완료해주세요");
      return;
    }

    try {
      const requestData: SignupRequest = {
        ...data,
        birth: new Date(data.birth),
        emailVerified: true
      };
      await signup(requestData);
      if (onSuccess) onSuccess();

    } catch (error: any) {
      setSignupError(error?.data?.message || "회원가입 실패");
    }
  };

  return (
    <Form onSubmit={handleSubmit(onSubmit)}>
      {/* 아이디 */}
      <Form.Group as={Row} className="mb-2" controlId={signupFieldConfig.username.name}>

        <Col sm={12}>
          <InputGroup>
            <InputGroup.Text style={inputGroupTextWidth}>{signupFieldConfig.username.label}</InputGroup.Text>
            <Form.Control
              type={signupFieldConfig.username.type}
              placeholder={signupFieldConfig.username.placeholder}
              {...signupFieldConfig.username.rules(register)}
              onInput={handleUsernameChange}
              isInvalid={!!errors.username || !!usernameError}
              disabled={checkingUsername && usernameChecked}
            />
            <Button
              variant={usernameChecked? "success" : "outline-primary"}
              onClick={handleCheckUsername}
              disabled={checkingUsername && usernameChecked}
            >
              {(checkingUsername && usernameChecked)? "사용가능" : "중복확인"}
            </Button>
          </InputGroup>
        </Col>
        <Col sm={12}>
          {checkingUsername && <div className="text-secondary">중복 확인 중...</div>}
          {usernameError && <div className="text-danger">{usernameError}</div>}
          {errors.username && <div className="text-danger">{errors.username.message}</div>}
        </Col>
      </Form.Group>
  
      {/* 이름 */}
      <Form.Group as={Row} className="mb-2" controlId={signupFieldConfig.name.name}>
        <Col sm={12}>
          <InputGroup>
            <InputGroup.Text style={inputGroupTextWidth}>{signupFieldConfig.name.label}</InputGroup.Text>
            <Form.Control
              type={signupFieldConfig.name.type}
              placeholder={signupFieldConfig.name.placeholder}
              {...signupFieldConfig.name.rules(register)}
              onInput={handleNameChange}
            />
          </InputGroup>
          {errors.name && <div className="text-danger">{errors.name.message}</div>}
        </Col>
      </Form.Group>
  
      {/* 이메일 */}
      <Form.Group as={Row} className="mb-2" controlId={signupFieldConfig.email.name}>
        <InputGroup>
          <InputGroup.Text style={inputGroupTextWidth}>{signupFieldConfig.email.label}</InputGroup.Text>
          <Form.Control
            type={signupFieldConfig.email.type}
            placeholder={signupFieldConfig.email.placeholder}
            {...signupFieldConfig.email.rules(register)}
            onInput={handleEmailChange}
            disabled={emailVerified}
          />
          {/* 이메일 인증 체크박스 (가로 배치) */}
          <Button
            variant={emailVerified ? "success" : "outline-success"}
            onClick={handleEmailVerify}
            disabled={emailVerified || loading || !email || !!validEmail}
          >
            {emailVerified ? "인증완료" : "인증요청"}
          </Button>
          {emailSent && !emailVerified && (
            <InputGroup>
              <Form.Control
                type="text"
                value={verificationCode}
                onChange={e => setVerificationCode(e.target.value)}
                placeholder="이메일 인증 코드"
              />
              <Button
                variant="outline-secondarty"
                onClick={handleCodeVerification}
                disabled={loading || !verificationCode}
              >
                인증확인
              </Button>
            </InputGroup>
          )}
        </InputGroup>
        {emailError && <div className="text-danger">{emailError}</div>}
        {errors.email && <div className="text-danger">{errors.email.message}</div>}
      </Form.Group>
  
      {/* 비밀번호 */}
      <Form.Group as={Row} className="mb-2" controlId={signupFieldConfig.password.name}>
        <InputGroup>
          <InputGroup.Text style={inputGroupTextWidth}>{signupFieldConfig.password.label}</InputGroup.Text>
          <Form.Control
            type={signupFieldConfig.password.type}
            placeholder={signupFieldConfig.password.placeholder}
            {...signupFieldConfig.password.rules(register)}
          />
        </InputGroup>
        {errors.password && <div className="text-danger">{errors.password.message}</div>}
      </Form.Group>
  
      {/* 비밀번호 확인 */}
      <Form.Group as={Row} className="mb-2" controlId={signupFieldConfig.confirmPassword.name}>
        <InputGroup>
          <InputGroup.Text style={inputGroupTextWidth}>{signupFieldConfig.confirmPassword.label}</InputGroup.Text>
          <Form.Control
            type={signupFieldConfig.confirmPassword.type}
            placeholder={signupFieldConfig.confirmPassword.placeholder}
            {...signupFieldConfig.confirmPassword.rules(register, password)}
            isInvalid={!!errors.confirmPassword || (!!confirmPassword && !isPasswordMatch)}
            isValid={!!confirmPassword && isPasswordMatch}
          />
          {confirmPassword && (
            <InputGroup.Text>
              {isPasswordMatch ? (
                <MdCheckCircle color="green" aria-label="비밀번호 일치" />
              ) : (
                <MdCancel color="red" aria-label="비밀번호 불일치" />
              )}
            </InputGroup.Text>
          )}
        </InputGroup>
        {errors.confirmPassword && <div className="text-danger">{errors.confirmPassword.message}</div>}
        {/* 추가로 불일치 시 실시간 안내 */}
        {!errors.confirmPassword && confirmPassword && !isPasswordMatch && (
          <div className="text-danger">비밀번호가 일치하지 않습니다.</div>
        )}
        {confirmPassword && isPasswordMatch && (
          <div className="text-success">비밀번호가 일치합니다.</div>
        )}
      </Form.Group>
  
      {/* 전화번호 */}
      <Form.Group as={Row} className="mb-2" controlId={signupFieldConfig.phone.name}>
        <InputGroup>
          <InputGroup.Text style={inputGroupTextWidth}>{signupFieldConfig.phone.label}</InputGroup.Text>
          <Form.Control
            type={signupFieldConfig.phone.type}
            placeholder={signupFieldConfig.phone.placeholder}
            {...signupFieldConfig.phone.rules(register)}
            onInput={handlePhoneChange}
          />
          {errors.phone && <div className="text-danger">{errors.phone.message}</div>}
        </InputGroup>
      </Form.Group>
  
      {/* 생년월일 */}
      <Form.Group as={Row} className="mb-2" controlId={signupFieldConfig.birth.name}>
        <InputGroup>
          <InputGroup.Text style={inputGroupTextWidth}>{signupFieldConfig.birth.label}</InputGroup.Text>
          <Form.Control
            type={signupFieldConfig.birth.type}
            placeholder={signupFieldConfig.birth.placeholder}
            {...signupFieldConfig.birth.rules(register)}
          />
          {errors.birth && <div className="text-danger">{errors.birth.message}</div>}
        </InputGroup>
      </Form.Group>
  
      {/* 주소 */}
      <AddressInput address={address} setAddress={setAddress} />

      {signupError && <Alert variant="danger">{signupError}</Alert>}
      <Button 
        type="submit" 
        className="w-100 mt-2"
        disabled={!emailVerified || loading}
        >
          {loading ? (
            <>
              <Spinner
                as="span"
                animation="border"
                size="sm"
                role="status"
                aria-hidden="true"
              /> 
              처리중...
            </> 
          ) : "가입하기"}
        </Button>
    </Form>
  );
};

export default Signup;