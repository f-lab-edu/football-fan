package com.footballfan.gateway.application.port.out;

import com.footballfan.gateway.application.domain.User;

import java.util.Optional;

public interface UserFindOutputPort {
    Optional<User> findUserById(Long id);
    Optional<User> findUserByEmail(String email);
}
