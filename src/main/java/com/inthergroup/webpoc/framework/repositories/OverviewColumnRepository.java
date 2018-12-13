//*************************************************//
//          INTHER LOGISTICS ENGINEERING           //
//*************************************************//

package com.inthergroup.webpoc.framework.repositories;

import com.inthergroup.webpoc.framework.domain.OverviewColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author gvandenbekerom
 * @since 24-Oct-18
 *
 * exposes jpa functions for OverviewColumn
 */
public interface OverviewColumnRepository extends JpaRepository<OverviewColumn, Long> {
    @Query(value = "DELETE FROM OverviewColumn o WHERE o.overview IS NULL")
    @Modifying
    @Transactional
    void deleteWhereOverviewIsNull();
}
