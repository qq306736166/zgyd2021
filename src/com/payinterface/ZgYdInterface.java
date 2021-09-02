package com.payinterface;

import jdk.jfr.Description;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Description(value = "vueconfig")
public @interface ZgYdInterface {

    /**
     * 手机号码
     * @return
     */
    String phone_number() default "";

    String amount() default "";

    String chargeMoney() default "";

    String choseMoney() default "";

}
