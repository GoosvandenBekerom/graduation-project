//*************************************************//
//          INTHER LOGISTICS ENGINEERING           //
//*************************************************//

package com.inthergroup.webpoc.framework.security.user;

import lombok.Data;

/**
 * @author gvandenbekerom
 * @since 16-Nov-18
 *
 *  Data class representing user in inther_lc schema of dbint database
 */
public @Data class User {
    private String id;
    private String username; // logical_id
    private String password;

    private UserGroup group;

    private String name;
    private String lastName;

    private String countryId;
    private String languageId;
    private String variantId;
}
