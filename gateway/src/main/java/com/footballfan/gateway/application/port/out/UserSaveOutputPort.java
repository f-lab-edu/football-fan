package com.footballfan.gateway.application.port.out;

import com.footballfan.gateway.application.domain.User;

public interface UserSaveOutputPort {
    User saveUser(User user);
}
