//*************************************************//
//          INTHER LOGISTICS ENGINEERING           //
//*************************************************//

package com.inthergroup.webpoc.framework.web.errors;

import com.inthergroup.webpoc.framework.config.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author gvandenbekerom
 * @since 18-Sep-18
 *
 * Throw this exception when some kind of update to a {@link com.inthergroup.webpoc.framework.domain.MenuItem} violates the maximum menu depth
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class MaxMenuDepthViolationException extends RuntimeException {
    public MaxMenuDepthViolationException() {
        super(String.format("Operation would violate max menu depth of %s", Constants.MAX_MENU_DEPTH));
    }
}
