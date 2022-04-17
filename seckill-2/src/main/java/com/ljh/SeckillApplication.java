package com.ljh;

import com.ljh.dao.UserDOMapper;
import com.ljh.entity.UserDO;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 获取 otp 然后注册用户：http://localhost:6002/getotp.html
 * 登录：http://localhost:6002/login.html
 * 创建商品：http://localhost:6002/createitem.html
 */
@SpringBootApplication(scanBasePackages = "com.ljh")
@RestController
@MapperScan("com.ljh.dao")
public class SeckillApplication {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        SpringApplication.run(SeckillApplication.class, args);
    }

    private final UserDOMapper userDOMapper;

    public SeckillApplication(UserDOMapper userDOMapper) {
        this.userDOMapper = userDOMapper;
    }

    @RequestMapping
    public String home() {
        UserDO userDO = userDOMapper.selectByPrimaryKey(1);
        if (userDO == null) {
            return "用户对象不存在";
        } else {
            return userDO.getName();
        }
    }
}
