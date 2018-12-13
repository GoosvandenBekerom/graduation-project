//*************************************************//
//          INTHER LOGISTICS ENGINEERING           //
//*************************************************//

package com.inthergroup.webpoc.framework.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author gvandenbekerom
 * @since 26-Nov-18
 *
 * Used to restrict BaseEntities for usergroups in ither_lc@dbint
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
public @Data class SecurityGroup {
    @Id
    private String id;
}
