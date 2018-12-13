//*************************************************//
//          INTHER LOGISTICS ENGINEERING           //
//*************************************************//

package com.inthergroup.webpoc.framework.repositories;

import com.inthergroup.webpoc.framework.domain.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author gvandenbekerom
 * @since 17-Sep-18
 *
 * exposes jpa functions for MenuItem
 */
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {}
