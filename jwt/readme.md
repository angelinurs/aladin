# jwt-oauth

## development environment

- spring boot 3, java 17, gradle

## 필수 환경 변수

시크릿은 저장소에 두지 않고 실행 환경에서 주입합니다. 값이 없으면 애플리케이션이 시작되지 않습니다.

| 변수 | 용도 | 설정 위치 |
|---|---|---|
| `JASYPT_ENCRYPTOR_PASSWORD` | `ENC(...)` 값 복호화 키 | `common/jasypt.yml` |
| `JWT_SECRET_KEY` | JWT 서명 키 (`openssl rand -base64 32`) | `common/jwt.yml` |
| `KAKAO_CLIENT_ID` / `KAKAO_CLIENT_SECRET` | Kakao OAuth | `secret/secrets.yml` |
| `HUGGINGFACE_API_KEY` | HuggingFace LLM (main 브랜치) | `secret/secrets.yml` |
| `MAIL_USERNAME` / `MAIL_PASSWORD` | SMTP (기존과 동일) | `smtp/*.yml` |
| `H2_CONSOLE_PASSWORD` | 로컬 H2 콘솔 (선택, 기본값 `changeme`) | `datasource/h2.yml` |

```bash
export JASYPT_ENCRYPTOR_PASSWORD=... JWT_SECRET_KEY=... KAKAO_CLIENT_ID=... KAKAO_CLIENT_SECRET=...
./gradlew bootRun
```

IntelliJ에서는 Run Configuration → Environment variables에 넣습니다.
