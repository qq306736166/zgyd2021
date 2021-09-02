package com.payinterface;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
public @interface ZgYdInter {

    String orderID() default "";
}
