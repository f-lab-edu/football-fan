package com.footballfan.adapter.out.persistence.user;

import com.footballfan.adapter.out.persistence.jpa.DataJpaUserRepository;
import com.footballfan.application.domain.User;
import com.footballfan.application.port.out.UserFindOutputPort;
import com.footballfan.application.port.out.UserSaveOutputPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserRepository implements UserFindOutputPort, UserSaveOutputPort {
    private final DataJpaUserRepository userRepository;

    @Override
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
