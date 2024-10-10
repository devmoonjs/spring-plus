package org.example.expert;

import ch.qos.logback.core.testUtil.RandomUtil;
import org.example.expert.domain.auth.service.AuthService;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
public class UserDummyDataTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @Rollback(value = false)
    void 더미_유저_데이터_1개_생성() {

        //given
        String password = passwordEncoder.encode("1234");
        User user = new User(
                1L, "dummy@gmail.com", password, UserRole.USER,"dummy", "dummy.com");

        //when
        User savedUser = userRepository.save(user);

        //then
        assertNotNull(savedUser);
    }

    @Test
    @Rollback(value = false)
    void 더미_유저_데이터_1_000_000개_생성() {

        //given when
        for (int i = 1; i <= 1_000_000; i++) {
            int leftLimit = 97; // letter 'a'
            int rightLimit = 122; // letter 'z'
            int targetStringLength = 10;
            Random random = new Random();
            String rdName = random.ints(leftLimit, rightLimit + 1)
                    .limit(targetStringLength)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();

            String email = rdName + "@dummy.com";
            String password = passwordEncoder.encode("1234");
            String imgUrl = rdName + ".com";

            User user = new User((long) i, email, password, UserRole.USER, rdName, imgUrl);
            userRepository.save(user);
        }

        //then
        long count = userRepository.count();
        assertEquals(count, 1_000_000);
    }


    @Test
    @Rollback(value = false)
    void 더미_유저_데이터_1_000_000개_생성_v2() {

        int batchSize = 5_000;
        long totalSave = 1_000_000;

        //given when
        List<User> users = new ArrayList<>(batchSize);
        Random random = new Random();

        LongStream.rangeClosed(1, totalSave).parallel().forEach(i -> {
            int leftLimit = 97; // letter 'a'
            int rightLimit = 122; // letter 'z'
            int targetStringLength = 10;
            String rdName = random.ints(leftLimit, rightLimit + 1)
                    .limit(targetStringLength)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();

            String email = rdName + "@dummy.com";
            String password = passwordEncoder.encode("1234");
            String imgUrl = rdName + ".com";
            User user = new User(i, email, password, UserRole.USER, rdName, imgUrl);
            synchronized (users) {
                users.add(user);
            }

            if (users.size() == batchSize) {
                synchronized (users) {
                    userRepository.saveAll(users);
                    users.clear();
                }
            }
        });

        if (!users.isEmpty()) {
            userRepository.saveAll(users);
//            userRepository.flush();
            users.clear();
        }

        //then
        long count = userRepository.count();
        assertEquals(count, totalSave);
    }

    @Test
    @Rollback(value = false)
    void 더미_유저_데이터_1_000_000개_생성_v3() {

        int batchSize = 2500;

        //given when
        List<User> userList = new ArrayList<>(batchSize);

        for (int i = 1_000_001; i <= 1_005_000; i++) {
            String rdName = String.valueOf(i);
            String email = rdName + "@dummy.com";
            String imgUrl = rdName + ".com";
            String password = "1234";

            User user = new User((long) i, email, password, UserRole.USER, rdName, imgUrl);
            userList.add(user);

            if (userList.size() == batchSize) {
                userRepository.saveAll(userList);
                userList.clear();
            }
        }

        if (!userList.isEmpty()) {
            userRepository.saveAll(userList);
            userList.clear();
        }

        //then
        long count = userRepository.count();
        assertEquals(count, 1_000_000);
    }
}
