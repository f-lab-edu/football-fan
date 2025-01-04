package com.footballfan.gateway.application.port.out;

import com.footballfan.gateway.application.domain.User;
import com.footballfan.gateway.application.domain.vo.RoleType;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserFindOutputPortTest {

    private static final Logger log = LoggerFactory.getLogger(UserFindOutputPortTest.class);
    @Autowired
    private UserFindOutputPort findOutputPort;
    @Autowired
    private UserSaveOutputPort saveOutputPort;

    @Test
    public void canFindUserById() {
        // given
        User newUser = User.createUser("test", "test", "test", RoleType.USER, null, null);
        // when
        User user = saveOutputPort.saveUser(newUser);
        // then
        findOutputPort.findUserById(user.getId())
                .ifPresentOrElse(
                        u -> assertEquals(newUser.getId(), u.getId()),
                        () -> fail("User not found")
                );
    }
}