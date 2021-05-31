package com.miaosha.validator;


import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

//实现一个接口类的bean,在类扫描的时候可以发现，使用
//这里继承的是
@Component
public class ValidatorImpl implements InitializingBean {

    //使用的是javax的validate工具类
    private Validator validator;

    //实现校验并通过标准的返回格式进行返回
    public ValidationResult validate(Object bean){
        //标准格式
        ValidationResult result = new ValidationResult();
        //只要bean中有违背validator的规则，constraintViolationSet就会有值
        Set<ConstraintViolation<Object>> constraintViolationSet = validator.validate(bean);

        //先是要判空
        if(constraintViolationSet.size() > 0){
            //有错的情况
            //haserror==true
            result.setHasErrors(true);
            //java8特有的foreach方法遍历
            //有错误的话，就要挨个的提取错误的信息
            constraintViolationSet.forEach(constraintViolation->{
                    //错误信息
                    String errMsg = constraintViolation.getMessage();
                    //发生错误的字段的名字
                    String propertyName = constraintViolation.getPropertyPath().toString();
                    //存放进result的map
                   result.getErrorMsgMap().put(propertyName, errMsg);
            });
        }

        //返回结果
        return result;

    }

    //InitializingBean创建完成后，会回调这个方法
    @Override
    public void afterPropertiesSet() throws Exception {

        //将Hibernate validator通过工厂化的初始化方法使实例化
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();

    }
}
