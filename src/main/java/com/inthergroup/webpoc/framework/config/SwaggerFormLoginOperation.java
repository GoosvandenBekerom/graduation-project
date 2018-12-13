//*************************************************//
//          INTHER LOGISTICS ENGINEERING           //
//*************************************************//

package com.inthergroup.webpoc.framework.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author gvandenbekerom
 * @since 05-Dec-18
 */
@Controller
public class SwaggerFormLoginOperation {
    @PostMapping("login")
    public void login(@RequestParam String username, @RequestParam String password) {
        throw new IllegalStateException("This method shouldn't be called. It's implemented by Spring Security filters.");
    }
}
