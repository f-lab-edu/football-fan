package com.footballfan.application.port.out;

import com.footballfan.application.domain.User;
import com.footballfan.application.domain.vo.RoleType;
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
    public void canFindUserById(){
        User user = saveOutputPort.saveUser(User.createUser("test", "test", "test", RoleType.USER, null, null));
        findOutputPort.findUserById(user.getId()).ifPresentOrElse(
            u -> {
                assertEquals(user.getId(), u.getId());
                log.info("User found: {}", user);
            },
            () -> fail("User not found")
        );
    }
}