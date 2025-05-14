import React, { useRef } from "react";
import { InputGroup, Form, Button } from "react-bootstrap";
import { useDaumPostcodeScript } from "../hooks/daumPostcode";
import { inputGroupTextWidth } from "../types/style/ComponentStyle";
import { AddressFields } from "../types/auth/SignupTypes";

interface Props {
  address: AddressFields;
  setAddress: (addr: AddressFields) => void;
}

const AddressInput: React.FC<Props> = ({ address, setAddress }) => {
  useDaumPostcodeScript();
  const detailRef = useRef<HTMLInputElement>(null);

  const handleFindPostcode = () => {
    // @ts-ignore
    new window.daum.Postcode({
      oncomplete: (data: any) => {
        let roadAddr = data.roadAddress;
        let extraRoadAddr = "";

        if (data.bname && /[동|로|가]$/g.test(data.bname)) {
          extraRoadAddr += data.bname;
        }
        if (data.buildingName && data.apartment === "Y") {
          extraRoadAddr += (extraRoadAddr ? ", " + data.buildingName : data.buildingName);
        }
        if (extraRoadAddr) extraRoadAddr = ` (${extraRoadAddr})`;

        setAddress({
          postcode: data.zonecode,
          roadAddress: roadAddr,
          jibunAddress: data.jibunAddress,
          detailAddress: "",
          extraAddress: roadAddr ? extraRoadAddr : "",
          // guide: data.autoRoadAddress
          //   ? `(예상 도로명 주소 : ${data.autoRoadAddress}${extraRoadAddr})`
          //   : data.autoJibunAddress
          //   ? `(예상 지번 주소 : ${data.autoJibunAddress})`
          //   : "",
        });

        setTimeout(() => detailRef.current?.focus(), 100);
      },
    }).open();
  };

  return (
    <>
      <InputGroup className="mb-2">
        <InputGroup.Text style={inputGroupTextWidth}>우편번호</InputGroup.Text>
        <Form.Control
          type="text"
          value={address.postcode}
          placeholder="우편번호"
          readOnly
          disabled
        />
        <Button variant="outline-primary" onClick={handleFindPostcode}>
          우편번호 찾기
        </Button>
      </InputGroup>
      <InputGroup className="mb-2">
        <InputGroup.Text style={inputGroupTextWidth}>도로명주소</InputGroup.Text>
        <Form.Control
          type="text"
          value={address.roadAddress}
          placeholder="도로명주소"
          readOnly
          disabled
        />
      </InputGroup>
      <InputGroup className="mb-2">
        <InputGroup.Text style={inputGroupTextWidth}>지번주소</InputGroup.Text>
        <Form.Control
          type="text"
          value={address.jibunAddress}
          placeholder="지번주소"
          readOnly
          disabled
        />
      </InputGroup>
      <InputGroup className="mb-2">
        <InputGroup.Text style={inputGroupTextWidth}>상세주소</InputGroup.Text>
        <Form.Control
          type="text"
          value={address.detailAddress}
          placeholder="상세주소"
          onChange={e =>
            setAddress({ ...address, detailAddress: e.target.value })
          }
          ref={detailRef}
        />
      </InputGroup>
      {/* 특별히 필요가 없는 항목 
      <InputGroup className="mb-2">
        <InputGroup.Text style={inputGroupTextWidth}>참고항목</InputGroup.Text>
        <Form.Control
          type="text"
          value={address.extraAddress}
          placeholder="참고항목"
          readOnly
          disabled
        />
      </InputGroup>
      {address.guide && (
        <div style={{ color: "#999" }}>{address.guide}</div>
      )}
       */}
    </>
  );
};

export default AddressInput;
