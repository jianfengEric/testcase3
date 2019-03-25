/**
 * 
 */
package com.tng.portal.sms.server.util;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author Nick_Xiong
 *
 */
@Retention(value=RetentionPolicy.RUNTIME)
@Target(value=ElementType.FIELD)
public @interface UrlParameter {
       
    String name();// Parameter name 
    
    int pos() default 0;// Specified column location 
    
    String dateFormat() default "";// Formatting strings, such as dates 
    
    String map() default "";// Field value transformation mapping 
    
    String condition() default "";// Prerequisites for display 
    
    String formatterMethod() default "";// formatting method 
    
}
