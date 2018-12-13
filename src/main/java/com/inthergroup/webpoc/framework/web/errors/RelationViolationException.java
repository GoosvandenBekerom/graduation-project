//*************************************************//
//          INTHER LOGISTICS ENGINEERING           //
//*************************************************//

package com.inthergroup.webpoc.framework.web.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author gvandenbekerom
 * @since 19-Sep-18
 *
 * Throw this exception when a relation of a given object is violated
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class RelationViolationException extends RuntimeException {
    public RelationViolationException() {
        super("Operation violates a relation of the given object");
    }
}
