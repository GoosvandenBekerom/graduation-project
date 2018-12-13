//*************************************************//
//          INTHER LOGISTICS ENGINEERING           //
//*************************************************//

package com.inthergroup.webpoc.framework.web.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author gvandenbekerom
 * @since 18-Sep-18
 *
 * Throw this exception when some kind of update to a {@link com.inthergroup.webpoc.framework.domain.MenuItem} violates parent/child rights
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class ParentChildViolationException extends RuntimeException {
    public ParentChildViolationException() {
        super("A MenuItem can't be it's own parent or the parent of one of it's children.");
    }
}
