//*************************************************//
//          INTHER LOGISTICS ENGINEERING           //
//*************************************************//

package com.inthergroup.webpoc.framework.web.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author gvandenbekerom
 * @since 17-Sep-18
 *
 * Throw this (404) Exception whenever a requested Framework Entity is not found
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class FrameworkEntityNotFoundException extends RuntimeException {
    public FrameworkEntityNotFoundException(Class type, Object identifier) {
        super(String.format("Framework entity of type %s with identifier %s was not found", type.getSimpleName(), identifier));
    }
}
