package com.shaofei;

import com.shaofei.service.UserService;
import com.spring.ShaoFeiApplicationContext;

/**
 * @author xsf
 */
public class Test {

    public static void main(String[] args) {
        ShaoFeiApplicationContext shaoFeiApplicationContext = new ShaoFeiApplicationContext(AppConfig.class);
        UserService userService = (UserService)shaoFeiApplicationContext.getBean("userService");
        System.out.println(userService);
    }

}
