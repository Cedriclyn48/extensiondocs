package com.customext.extrestdocs.annotation;

import com.customext.extrestdocs.configuration.ExtensionApplyConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.annotation.*;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(ExtensionApplyConfig.class)
@ExtendWith(SpringExtension.class)
@BeforeEach
public @interface RestDocsApply {
}
