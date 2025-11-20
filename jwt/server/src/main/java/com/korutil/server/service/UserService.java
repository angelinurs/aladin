package com.korutil.server.service;

import com.korutil.server.domain.user.UserEntity;
import com.korutil.server.dto.common.CommonApiResponse;
import com.korutil.server.dto.jwt.JwtTokenDto;
import com.korutil.server.dto.user.record.MemberResponse;
import com.korutil.server.oauth.OAuthProvider;
import com.korutil.server.dto.user.*;
import com.korutil.server.dto.user.constant.ROLE;
import com.korutil.server.dto.user.record.UserResponse;
import com.korutil.server.repository.dao.user.*;
import com.korutil.server.repository.jpa.user.UserRepository;
import com.korutil.server.service.security.JwtTokenService;
import com.korutil.server.util.PasswordUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
public class UserService {

    private final UserDao userDao;
    private final UserProfileDao userProfileDao;
    private final UserCredentialsDao userCredentialsDao;
    private final UserPasswordHistoryDao userPasswordHistoryDao;
    private final EmailVerificationDao emailVerificationDao;
    private final UserAddressDao userAddressDao;
    private final SocialLoginDao socialLoginDao;

    private final MailService mailService;
    private final JwtTokenService jwtTokenService;
    private final UserRepository userRepository;

    public CommonApiResponse<List<UserResponse>> getUsers() {
        return CommonApiResponse.success(
                userDao.getUsers().stream()
                    .map(UserResponse::fromDto)
                    .toList());
    }

    public UserDto getUser( Long id ) {
        return userDao.getUser(id);
    }

    @Transactional
    public CommonApiResponse<UserResponse> createUser(
            UserDto requestUserDto, UserAddressDto userAddressDto, UserProfileDto userProfileDto, String rawPassword) {

        // 사용자 정보 생성
        UserEntity userEntity = userDao.createUser(requestUserDto);
        UserDto userDto = userEntity.toDto();

        // 비밀번호 암호화 처리
        String encodedPassword = PasswordUtils.generate(rawPassword);

        // 비밀번호 정보 별도의 DTO로 관리
        UserCredentialsDto credentialsDto = UserCredentialsDto.builder()
                .userId(userDto.getId())
                .password(encodedPassword)
                .build();

        // 사용자 비밀번호도 별도로 저장
        userCredentialsDao.createUserCredentials(credentialsDto);

        // 가입 주소 별도 저장
        userAddressDto.setUserId(userDto.getId());
        userAddressDao.save(userAddressDto);

        // 사용자 상세 정보 저장
        userProfileDto.setUser(userEntity);
        userProfileDao.save(userProfileDto);

        return CommonApiResponse.success(UserResponse.fromDto(userDto));
    }

    @Transactional
    public SocialLoginDto createSocialUser(SocialLoginDto socialLoginDto) {

        // 사용자 정보 생성
        String email = socialLoginDto.getEmail();
        String username = email.substring(0, email.indexOf("@"));
        UserDto mockUserDto =
                UserDto.builder()
                        .username(username)
                        .email(email)
                        .emailVerified(true)
                        .role(ROLE.USER)
                        .build();
        UserEntity userEntity = userDao.createUser(mockUserDto);
        UserDto userDto = userEntity.toDto();
        socialLoginDto.setUserId(userDto.getId());

        return socialLoginDao.save(socialLoginDto);
    }

    @Transactional
    public SocialLoginDto findByProviderUserIdAndOAuthProvider(String providerUserId, OAuthProvider oAuthProvider)    {
        return socialLoginDao.findByProviderUserIdAndOAuthProvider(providerUserId, oAuthProvider);
    }

    public CommonApiResponse<MemberResponse> getUserInfo() {
        Long userId = jwtTokenService.getUserIdFromToken();
        UserDto userdto = userDao.getUser(userId);
        UserProfileDto userProfileDto = userProfileDao.findByUserId(userId);
        List<UserAddressDto> userAddressDtos = userAddressDao.findAllUserAddressByUserId(userId);

        MemberDto memberDto = MemberDto.builder()
                .userId(userId)
                .userDto(userdto)
                .userProfileDto(userProfileDto)
                .userAddressDtos(userAddressDtos)
                .build();

        return CommonApiResponse.success(MemberResponse.ofDto(memberDto));
    }

    @Transactional
    public CommonApiResponse<MemberResponse> updateUser(MemberDto memberDto) {
        Long userId = jwtTokenService.getUserIdFromToken();
        UserDto userdto = userDao.getUser(userId);

        UserDto oldUserDto = memberDto.getUserDto();
        oldUserDto.setRole(userdto.getRole());

        UserDto updatedUserDto = userDao.update(userId, oldUserDto );
        UserProfileDto updatedUserProfileDto = userProfileDao.update(userId, memberDto.getUserProfileDto());
        UserAddressDto updatedUserAddressDto = userAddressDao.update(userId, memberDto.getUserAddressDtos().get(0));

        MemberDto updatedMemberDto = MemberDto.builder()
                .userDto(updatedUserDto)
                .userProfileDto(updatedUserProfileDto)
                .userAddressDtos(List.of(updatedUserAddressDto))
                .build();

        return CommonApiResponse.success(MemberResponse.ofDto(updatedMemberDto));
    }

    public void deleteUser() {
        Long userId = jwtTokenService.getUserIdFromToken();
        UserDto userdto = userDao.getUser(userId);

        // 관련 db 정보가 전부 분산되어 있으므로 user 만 deactivate 하면 됨.
        userDao.deleteUser(userdto.getId());
        socialLoginDao.delete(userdto.getId());

        // token 무력화
        JwtTokenDto jwtTokenDto = jwtTokenService.getAuthenticationJwtToken();
        jwtTokenService.deActivateToken( jwtTokenDto.getAccessToken() );
    }

    @Transactional
    public void changePassword(Long userId, String newPassword) {
        // 사용자 정보 가져오기
        UserDto userDto = userDao.getUser(userId);

        // 사용자 인증 정보 가져오기 (현재 비밀번호)
        UserCredentialsDto currentUserCredentialsDto = userCredentialsDao.getUserCredentials(userId);
        UserCredentialsDto newUserCredentialsDto = UserCredentialsDto.newInstance(userId, newPassword);

        // 새 비밀번호가 현재 비밀번호와 동일한지 확인
        if (PasswordUtils.matches(newPassword, currentUserCredentialsDto.getPassword())) {
            // TODO. 새로운 비밀 번호로 변경 요구
            return;  // 비밀번호가 변경되지 않았다면 종료
        }

        // 새 비밀번호가 기존 비밀번호 이력에 이미 존재하는지 체크
        boolean isPasswordInHistory = userPasswordHistoryDao.existsByUserIdAndPassword(userId, newUserCredentialsDto.getPassword());
        if (isPasswordInHistory) {
            // TODO. 새로운 비밀 번호로 변경 요구
            return;  // 새 비밀번호가 이력에 존재하면 종료
        }
        // 변경된 비밀번호 저장
        userCredentialsDao.save(newUserCredentialsDto);
        // 현재 비밀번호를 이력으로 저장
        userPasswordHistoryDao.save(newUserCredentialsDto);
    }

    public CommonApiResponse<String> putEmailVerification( String email ) {
        EmailVerificationDto dto =
                EmailVerificationDto.builder()
                        .email(email)
                        .build();
        dto.setSignupMail();

        mailService.sendMail(dto);
        emailVerificationDao.save(dto);

        return CommonApiResponse.success("Email 인증이 발송되었습니다.");
    }

    public CommonApiResponse<Boolean> isValidEmailVerification( EmailVerificationDto emailVerificationDto ) {
        emailVerificationDao.update(emailVerificationDto);
        return CommonApiResponse.success(true);
    }

    public CommonApiResponse<Boolean> isNotValidUsernameVerification( String username ) {
        return CommonApiResponse.success(userDao.isUserByUsername( username ));
    }
}
