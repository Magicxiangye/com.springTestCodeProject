package com.miaosha;

import com.miaosha.dao.UserDOMapper;
import com.miaosha.dataobject.UserDO;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Hello world!
 *
 */
/*先要自动化的配置这个类*/
    /* 依次扫描的目录下的包*/
@SpringBootApplication(scanBasePackages = {"com.miaosha"})
@RestController
@MapperScan("com.miaosha.dao")
public class App
{

    @Autowired
    private UserDOMapper userDOMapper;

    @RequestMapping("/")
    public String home(){
       UserDO userDO= userDOMapper.selectByPrimaryKey(1);
        if (userDO == null){
            return "不存在";
        }else{
            return userDO.getName();
        }

    }


    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        SpringApplication.run(App.class, args);
    }
}
