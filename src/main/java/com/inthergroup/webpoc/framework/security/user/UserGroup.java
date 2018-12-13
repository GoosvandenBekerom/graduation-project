//*************************************************//
//          INTHER LOGISTICS ENGINEERING           //
//*************************************************//

package com.inthergroup.webpoc.framework.security.user;

import lombok.Data;

/**
 * @author gvandenbekerom
 * @since 16-Nov-18
 *
 * Data class representing usergroup in inther_lc schema of dbint database
 */
public @Data class UserGroup {
    private String id;
    private String logicalId;
    private int hierarchy;
    private String description;
}
