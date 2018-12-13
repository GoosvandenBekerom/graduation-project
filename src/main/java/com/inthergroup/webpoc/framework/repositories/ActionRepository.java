//*************************************************//
//          INTHER LOGISTICS ENGINEERING           //
//*************************************************//

package com.inthergroup.webpoc.framework.repositories;

import com.inthergroup.webpoc.framework.domain.Action;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author gvandenbekerom
 * @since 27-Sep-18
 *
 * exposes jpa functions for Action
 */
public interface ActionRepository extends JpaRepository<Action, Long> {}
