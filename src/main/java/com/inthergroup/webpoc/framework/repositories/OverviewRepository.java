//*************************************************//
//          INTHER LOGISTICS ENGINEERING           //
//*************************************************//

package com.inthergroup.webpoc.framework.repositories;

import com.inthergroup.webpoc.framework.domain.Overview;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author gvandenbekerom
 * @since 03-Oct-18
 *
 * exposes jpa functions for Overview
 */
public interface OverviewRepository extends JpaRepository<Overview, Long> {}
