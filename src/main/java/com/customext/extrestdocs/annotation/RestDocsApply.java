package com.customext.extrestdocs.annotation;

import com.customext.extrestdocs.configuration.ExtensionApplyConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.*;

@Target({ ElementType.ANNOTATION_TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(ExtensionApplyConfig.class)
//@BeforeEach
public @interface RestDocsApply {

}
