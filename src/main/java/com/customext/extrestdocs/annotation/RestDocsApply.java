package com.customext.extrestdocs.annotation;

import com.customext.extrestdocs.service.RestDocsExtensionApplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.annotation.*;

/**
 * with RestAssured AcceptanceTest Class
 * @SpringBootTest is required
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(RestDocsExtensionApplier.class)
@ExtendWith(SpringExtension.class)
@BeforeEach
public @interface RestDocsApply {
}
