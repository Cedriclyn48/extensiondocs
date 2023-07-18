package com.customext.extrestdocs.annotation;

import java.lang.annotation.*;

@Inherited
@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RestDocsApply {

}
