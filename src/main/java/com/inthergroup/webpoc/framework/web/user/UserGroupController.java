//*************************************************//
//          INTHER LOGISTICS ENGINEERING           //
//*************************************************//

package com.inthergroup.webpoc.framework.web.user;

import com.inthergroup.webpoc.framework.security.user.UserDAO;
import com.inthergroup.webpoc.framework.security.user.UserGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author gvandenbekerom
 * @since 26-Nov-18
 */
@RestController
@RequestMapping("/api/userGroups")
public class UserGroupController {
    private final UserDAO userDAO;

    @Autowired
    public UserGroupController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * @return All userGroups except DEV
     */
    @GetMapping
    @PreAuthorize("hasRole('DEV') or hasRole('ADMINISTRATORS')")
    public List<UserGroup> getAllWithoutDev() {
        return userDAO.findAllUserGroups().stream()
                .filter(ug -> !ug.getId().equals("DEV"))
                .collect(Collectors.toList());
    }
}
