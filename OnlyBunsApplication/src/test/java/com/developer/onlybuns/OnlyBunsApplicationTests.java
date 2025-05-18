package com.developer.onlybuns;

import com.developer.onlybuns.controller.RegisteredUserController;
import com.developer.onlybuns.entity.RegisteredUser;
import com.developer.onlybuns.enums.Role;
import com.developer.onlybuns.repository.RegisteredUserRepository;
import com.developer.onlybuns.service.RegisteredUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class OnlyBunsApplicationTests {

}
