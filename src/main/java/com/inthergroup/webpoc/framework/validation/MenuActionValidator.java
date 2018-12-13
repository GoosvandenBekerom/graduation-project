//*************************************************//
//          INTHER LOGISTICS ENGINEERING           //
//*************************************************//

package com.inthergroup.webpoc.framework.validation;

import com.inthergroup.webpoc.framework.config.Constants;
import com.inthergroup.webpoc.framework.domain.Action;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gvandenbekerom
 * @since 01-Oct-18
 *
 * Custom validation for different MenuAction types
 */
public class MenuActionValidator implements ConstraintValidator<MenuActionValidation, Action> {
    @Override
    public boolean isValid(Action action, ConstraintValidatorContext context) {

        if (action.getType() == null) return false;

        List<String> errors = new ArrayList<>();
        switch (action.getType()) {
            case External_Url:
                if (StringUtils.isEmpty(action.getUrl())) {
                    errors.add(String.format("Field url required when creating/updating action of type %s.", action.getType().name()));
                }
                break;
            case Overview:
                if (action.getOverviewId() == null) {
                    errors.add(String.format("Field overviewId required when creating/updating action of type %s.", action.getType().name()));
                }
                break;
            case Path:
                if (StringUtils.isEmpty(action.getPath())){
                    errors.add(String.format("Field path required when creating/updating action of type %s.", action.getType().name()));
                }
                break;
        }

        if (!errors.isEmpty()) {
            context.disableDefaultConstraintViolation();
            for (String error : errors) {
                context.buildConstraintViolationWithTemplate(error).addConstraintViolation();
            }
            return false;
        }

        return true;
    }
}
