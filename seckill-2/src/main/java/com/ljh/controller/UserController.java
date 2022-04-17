package com.ljh.controller;

import com.alibaba.druid.util.StringUtils;
import com.ljh.controller.vo.UserVO;
import com.ljh.error.BusinessErrorEnum;
import com.ljh.error.BusinessException;
import com.ljh.response.CommonReturnType;
import com.ljh.service.UserService;
import com.ljh.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

@RestController("user")
@RequestMapping("/user")
public class UserController extends BaseController {

    private final UserService userService;
    private final HttpServletRequest httpServletRequest;

    public UserController(UserService userService, HttpServletRequest httpServletRequest) {
        this.userService = userService;
        this.httpServletRequest = httpServletRequest;
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public CommonReturnType login(@RequestParam(name = "telephone") String telephone,
                                  @RequestParam(name = "password") String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        if (StringUtils.isEmpty(telephone) || StringUtils.isEmpty(password))
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR);

        // 校验用户登录是否合法
        UserModel userModel = userService.validateLogin(telephone, this.encodeByMd5(password));

        httpServletRequest.getSession().setAttribute("IS_LOGIN", true);
        httpServletRequest.getSession().setAttribute("LOGIN_USER", userModel);
        return CommonReturnType.create(null);
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public CommonReturnType register(@RequestParam(name = "telephone") String telephone,
                                     @RequestParam(name = "otpCode") String otpCode,
                                     @RequestParam(name = "name") String name,
                                     @RequestParam(name = "gender") Integer gender,
                                     @RequestParam(name = "age") Integer age,
                                     @RequestParam(name = "password") String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        // 验证码手机号和对应的 otpCode 相符合
        String inSessionOtpCode = (String) httpServletRequest.getSession().getAttribute(telephone);
        if (!StringUtils.equals(otpCode, inSessionOtpCode))
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "短信验证码不符合");

        // 用户注册
        UserModel userModel = new UserModel();
        userModel.setName(name);
        userModel.setGender(new Byte(String.valueOf(gender.intValue())));
        userModel.setAge(age);
        userModel.setTelephone(telephone);
        userModel.setRegisterMode("phone");
        userModel.setEncryptPassword(this.encodeByMd5(password));
        userService.register(userModel);

        return CommonReturnType.create(null);
    }

    private String encodeByMd5(String str) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder = new BASE64Encoder();
        return base64Encoder.encode(md5.digest(str.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * 获取 otp 短信
     */
    @PostMapping("/getOtp")
    public CommonReturnType getOtp(@RequestParam(name = "telephone") String telephone) {
        /* 按一定规则生成 OTP 验证码 */
        Random random = new Random();
        // [0, 99999)
        int randomInt = random.nextInt(99999);
        // [10000, 109999)
        randomInt += 10000;
        String otpCode = String.valueOf(randomInt);

        /* 将 OTP 验证码同对应用户的手机号关使用 */
        // 建议使用 redis 实现，这里使用 Httpsession 的方式实现
        httpServletRequest.getSession().setAttribute(telephone, otpCode);

        /* 将 OTP 验证码通过短信通道发送给用户（省略） */

        System.out.println("telephone = " + telephone + " & otpCode = " + otpCode);

        return CommonReturnType.create(null);
    }

    /**
     * http://localhost:6002/user/get?id=1
     */
    @GetMapping("/get")
    public CommonReturnType getUser(@RequestParam(name = "id") Integer id) throws BusinessException {
        UserModel userModel = userService.getUserById(id);
        if (userModel == null) throw new BusinessException(BusinessErrorEnum.USER_NOT_EXIST);
        UserVO userVO = this.convertFromModel(userModel);
        return CommonReturnType.create(userVO);
    }

    private UserVO convertFromModel(UserModel userModel) {
        if (userModel == null) return null;
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel, userVO);
        return userVO;
    }
}