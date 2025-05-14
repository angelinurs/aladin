package com.korutil.server.controller;

import com.korutil.server.dto.common.CommonApiResponse;
import com.korutil.server.dto.user.record.MemberResponse;
import com.korutil.server.dto.user.record.*;
import com.korutil.server.service.AuthService;
import com.korutil.server.service.TokenUserService;
import com.korutil.server.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final AuthService authService;
    private final TokenUserService tokenUserService;

    @GetMapping("/list")
    public CommonApiResponse<List<UserResponse>> getUsers() {
        return userService.getUsers();
    }

    @PostMapping("/signup")
    public CommonApiResponse<UserResponse> signup(@RequestBody SignUpRequest request) {
        return userService.createUser(request.toUserDto(), request.toUserAddressDto(), request.toUserProfileDto(), request.password());
    }

    @PostMapping("/login")
    public CommonApiResponse<LoginSuccessResponse> login(@RequestBody LoginRequest request, @RequestHeader Map<String, String> headers) {
        return authService.login( LogInOutCommonRecord.from( request, headers));
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "로그아웃", description = "로그아웃 처리 (Authorization 헤더 필요)")
    public ResponseEntity<Void> logout(@RequestHeader Map<String, String> headers) {
        authService.logout( headers );
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    @Operation(summary = "이용자 정보 요청", description = "이용자 정보 반환 (Authorization 헤더 필요)")
    public CommonApiResponse<MemberResponse> getUser() {
        return userService.getUserInfo();
    }

    @PutMapping("/me")
    @Operation(summary = "이용자 정보 수정", description = "이용자 정보 수정 (Authorization 헤더 필요)")
    public CommonApiResponse<MemberResponse> updateUser(@RequestBody MemberRequest request) {
        return userService.updateUser(request.toMemberDto());
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "이용자 탈퇴", description = "이용자 탈퇴 & 로그아웃 (Authorization 헤더 필요)")
    public ResponseEntity<Void> deleteUser() {
        userService.deleteUser();
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "사용자 주소 전체 조회", description = "액세스 토큰으로 사용자의 모든 주소를 조회합니다.")
    @PostMapping("/address/user")
    public CommonApiResponse<List<UserAddressResponse>> findAllAddress() {

        return tokenUserService.findAllUserAddress();
    }
}
