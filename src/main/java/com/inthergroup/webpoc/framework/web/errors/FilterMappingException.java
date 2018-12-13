//*************************************************//
//          INTHER LOGISTICS ENGINEERING           //
//*************************************************//

package com.inthergroup.webpoc.framework.web.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author gvandenbekerom
 * @since 13-Nov-18
 *
 * Throw this exception when processing a {@link com.inthergroup.webpoc.framework.helper.FilterStructure} fails
 */
@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class FilterMappingException extends RuntimeException {
    public FilterMappingException() {
        super("An error occurred during the processing of a filter.");
    }
}
