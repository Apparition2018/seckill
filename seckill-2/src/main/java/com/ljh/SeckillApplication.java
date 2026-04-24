package com.ljh;

import com.ljh.dao.UserDOMapper;
import com.ljh.entity.UserDO;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <a href="http://localhost:6001/getotp.html">获取 otp 然后注册用户</a><br/>
 * <a href="http://localhost:6001/login.html">登录</a><br/>
 * <a href="http://localhost:6001/createitem.html">创建商品</a>
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
