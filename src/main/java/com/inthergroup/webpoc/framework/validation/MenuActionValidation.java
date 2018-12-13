//*************************************************//
//          INTHER LOGISTICS ENGINEERING           //
//*************************************************//

package com.inthergroup.webpoc.framework.validation;

import com.inthergroup.webpoc.framework.config.Constants;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author gvandenbekerom
 * @since 01-Oct-18
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {MenuActionValidator.class})
public @interface MenuActionValidation {
    String message() default Constants.VALIDATION_MESSAGE_MENU_ACTION;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
