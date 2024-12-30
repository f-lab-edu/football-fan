package com.footballfan.application.port.out;

import com.footballfan.application.domain.User;

public interface UserSaveOutputPort {
    User saveUser(User user);
}
