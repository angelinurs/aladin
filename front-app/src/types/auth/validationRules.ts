// validationRules.ts
import { UseFormRegister, FieldValues } from "react-hook-form";
import { SignupFormValues } from "./SignupTypes";

export const ERROR_MESSAGES = {
  REQUIRED_ID: "아이디를 8자 이상 입력하세요",
  MIN_ID: "아이디는 최소 8자 이상이어야 합니다",
  MIN_NAME: "이름은 최소 2자 이상이어야 합니다.",
  REQUIRED_NAME: "이름을 2자 이상 입력해주세요",
  REQUIRED_EMAIL: "이메일을 입력해주세요",
  INVALID_EMAIL: "유효한 이메일 주소를 입력해 주세요",
  REQUIRED_PASSWORD: "비밀번호를 입력해주세요",
  MIN_PASSWORD: "비밀번호는 최소 8자 이상이어야 합니다.",
  INVALID_PASSWORD: "비밀번호는 대문자, 소문자, 숫자, 특수문자를 각각 하나 이상 포함해야 합니다",
  REQUIRED_CONFIRM_PASSWORD: "비밀번호 확인을 입력해주세요",
  NOT_MATCH_PASSWORD: "비밀번호가 맞지 않습니다",
  REQUIRED_PHONE: "전화번호를 입력해주세요",
  NOT_MATCH_PHONE: "숫자만 입력해 주세요",
  INVALID_PHONE: "유효한 전화번호 형식이 아닙니다",
  REQUIRED_BIRTH: "생년월일을 입력해주세요",
  FUTURE_BIRTH: "미래 날짜는 선택할 수 없습니다",
  REQUIRED_ADDRESS: "주소를 입력해주세요",
  REQUIRED_ZIPCODE: "우편번호를 입력해주세요",
  INVALID_ZIPCODE: "5자리 숫자로 입력해주세요",
  EMAIL_VERIFICATION_REQUIRED: "이메일 인증이 필요합니다",
};

type FieldType = "text" | "email" | "password" | "date" | "checkbox";

type FieldConfig<T extends FieldValues> = {
  [K in keyof T]: {
    name: K;
    label: string;
    type: FieldType;
    placeholder: string;
    rules: (register: UseFormRegister<T>, extra?: any) => ReturnType<UseFormRegister<T>>;
  }
};

export const signupFieldConfig: FieldConfig<SignupFormValues> = {
  username: {
    name: "username",
    label: "아이디",
    type: "text",
    placeholder: ERROR_MESSAGES.REQUIRED_ID,
    rules: (register) => register("username", {
      required: ERROR_MESSAGES.REQUIRED_ID,
      minLength: { value: 8, message: ERROR_MESSAGES.MIN_ID }
    })
  },
  name: {
    name: "name",
    label: "이름",
    type: "text",
    placeholder: ERROR_MESSAGES.MIN_NAME,
    rules: (register) => register("name", {
      required: ERROR_MESSAGES.REQUIRED_NAME,
      minLength: { value: 4, message: ERROR_MESSAGES.MIN_NAME }
    })
  },
  email: {
    name: "email",
    label: "이메일",
    type: "email",
    placeholder: "예: example@email.com",
    rules: (register) => register("email", {
      required: ERROR_MESSAGES.REQUIRED_EMAIL,
      pattern: {
        value: /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
        message: ERROR_MESSAGES.INVALID_EMAIL
      }
    })
  },
  password: {
    name: "password",
    label: "비밀번호",
    type: "password",
    placeholder: "영문, 숫자 포함 8자 이상",
    rules: (register) => register("password", {
      required: ERROR_MESSAGES.REQUIRED_PASSWORD,
      minLength: { value: 8, message: ERROR_MESSAGES.MIN_PASSWORD },
      pattern: {
        value: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])/,
        message: ERROR_MESSAGES.INVALID_PASSWORD
      }
    })
  },
  confirmPassword: {
    name: "confirmPassword",
    label: "비밀번호 확인",
    type: "password",
    placeholder: "비밀번호를 한 번 더 입력하세요",
    rules: (register, password: string) => register("confirmPassword", {
      required: ERROR_MESSAGES.REQUIRED_CONFIRM_PASSWORD,
      validate: (value) =>
        value === password || ERROR_MESSAGES.NOT_MATCH_PASSWORD
    })
  },
  phone: {
    name: "phone",
    label: "전화번호",
    type: "text",
    placeholder: "예: 01012345678",
    rules: (register) => register("phone", {
      required: ERROR_MESSAGES.REQUIRED_PHONE,
      pattern: {
        value: /^[0-9]{10,11}$/,
        message: ERROR_MESSAGES.INVALID_PHONE
      }
    })
  },
  birth: {
    name: "birth",
    label: "생년월일",
    type: "date",
    placeholder: "YYYY-MM-DD",
    rules: (register) => register("birth", {
      required: ERROR_MESSAGES.REQUIRED_BIRTH,
      validate: (value) => {
        if (!value) return ERROR_MESSAGES.REQUIRED_BIRTH;
        const date = new Date(value);
        if (isNaN(date.getTime())) return ERROR_MESSAGES.REQUIRED_BIRTH;
        if (date > new Date()) return ERROR_MESSAGES.FUTURE_BIRTH;
        return true;
      }
    })
  },
  address: {
    name: "address",
    label: "주소",
    type: "text",
    placeholder: ERROR_MESSAGES.REQUIRED_ADDRESS,
    rules: (register) => register("address", {
      required: ERROR_MESSAGES.REQUIRED_ADDRESS
    })
  },
  // zipcode: {
  //   name: "zipcode",
  //   label: "우편번호",
  //   type: "text",
  //   placeholder: "예: 12345",
  //   rules: (register) => register("zipcode", {
  //     required: ERROR_MESSAGES.REQUIRED_ZIPCODE,
  //     pattern: {
  //       value: /^\d{5}$/,
  //       message: ERROR_MESSAGES.INVALID_ZIPCODE
  //     }
  //   })
  // },
  emailVerified: {
    name: "emailVerified",
    label: "이메일 인증 완료",
    type: "checkbox" as const,
    placeholder: "",
    rules: (register) => register("emailVerified", {
      required: ERROR_MESSAGES.EMAIL_VERIFICATION_REQUIRED
    })
  }
};

/* 입력 문자 제한 */
const handleUsernameChange = (e: React.ChangeEvent<HTMLInputElement>) => {
  const input = e.target as HTMLInputElement;
  input.value = input.value.toLowerCase() 
                            .replace(/[^a-z0-9@$!%*?&\\.\\-\\_]/g, ''); // 영어 소문자, 대문자, 숫자, 특수문자만 허용
};
const handleNameChange = (e: React.ChangeEvent<HTMLInputElement>) => {
  const input = e.target as HTMLInputElement;
  input.value = input.value.replace(/[^a-zA-Z0-9.\uac00-\ud7af\u1100-\u11ff\u3130-\u318f]/g, '');
};
const handleEmailChange = (e: React.ChangeEvent<HTMLInputElement>) => {
  const input = e.target as HTMLInputElement;
  input.value = input.value.toLowerCase() 
                            .replace(/[^a-z0-9@.\-_]/g, '') // 영어 소문자, 대문자, 숫자, 
                            .replace(/[\uac00-\ud7af\u1100-\u11ff\u3130-\u318f]/g, ''); // 영어 소문자, 대문자, 숫자, 
};
const handlePhoneChange = (e: React.ChangeEvent<HTMLInputElement>) => {
  const input = e.target as HTMLInputElement;
  input.value = input.value.replace(/[^0-9]/g, '');
};

export {handleUsernameChange, handleNameChange, handleEmailChange, handlePhoneChange};