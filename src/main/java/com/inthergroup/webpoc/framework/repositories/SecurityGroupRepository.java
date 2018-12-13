//*************************************************//
//          INTHER LOGISTICS ENGINEERING           //
//*************************************************//

package com.inthergroup.webpoc.framework.repositories;

import com.inthergroup.webpoc.framework.domain.SecurityGroup;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author gvandenbekerom
 * @since 26-Nov-18
 */
public interface SecurityGroupRepository extends JpaRepository<SecurityGroup, String> {}
