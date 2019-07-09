package com.mycompany.annotations;

import com.mycompany.dao.AttributeConverter;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, TYPE})
@Retention(RUNTIME)
public @interface Convert {

    Class<? extends AttributeConverter> converter();

}
