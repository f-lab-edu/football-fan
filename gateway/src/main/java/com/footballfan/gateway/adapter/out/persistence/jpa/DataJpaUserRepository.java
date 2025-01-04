package com.footballfan.gateway.adapter.out.persistence.jpa;

import com.footballfan.gateway.application.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface DataJpaUserRepository extends CrudRepository<User, Long> {
    Optional<User> findUserByUsername(String username);
    Optional<User> findUserByEmail(String email);
}
