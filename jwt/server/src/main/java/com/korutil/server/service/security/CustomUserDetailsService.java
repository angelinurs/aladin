package com.korutil.server.service.security;

import com.korutil.server.dao.user.UserCredentialsDao;
import com.korutil.server.dao.user.UserDao;
import com.korutil.server.dto.user.UserCredentialsDto;
import com.korutil.server.dto.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserDao userDao;
    private final UserCredentialsDao userCredentialsDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDto userDto = userDao.getUserByUsername(username);
        UserCredentialsDto userCredentialsDto = userCredentialsDao.getUserCredentials(userDto.getId());
        return
                User.builder()
                        .username(userDto.getUsername())
                        .password( userCredentialsDto.getPassword() )
                        .authorities(userDto.getAuthorities())
                        .build();
    }
}
