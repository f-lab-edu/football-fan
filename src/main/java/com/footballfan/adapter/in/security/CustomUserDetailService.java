package com.footballfan.adapter.in.security;

import com.footballfan.application.domain.User;
import com.footballfan.application.port.out.UserFindOutputPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static org.springframework.security.core.userdetails.User.builder;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserFindOutputPort userFindOutputPort;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userFindOutputPort.findUserById(Long.parseLong(userId))
                .orElseThrow(() -> new UsernameNotFoundException(String.format("유저 id %s이(가) 존재하지 않습니다.", userId)));
        return builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().toString())
                .build();
    }
}
