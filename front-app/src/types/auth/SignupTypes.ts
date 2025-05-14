// types/auth/SignupFormValues.ts

interface SignupRequest {
  username: string;
  email: string;
  password: string;
  confirmPassword: string;
  name: string;
  phone: string;
  birth: Date; // 예: "1990-01-01"
  address: AddressFields;
  emailVerified: boolean;
  // role?: string; // 기본값 'USER'로 설정할 수 있음
}

interface SignupFormValues {
  username: string;
  name: string;
  email: string;
  password: string;
  confirmPassword: string;
  phone: string;
  birth: string; // date input의 값은 string
  address: AddressFields;
  emailVerified: boolean;
}

interface AddressFields {
  postcode: string;
  roadAddress: string;
  jibunAddress: string;
  detailAddress: string;
  extraAddress: string;
}

export type { SignupRequest, SignupFormValues, AddressFields };