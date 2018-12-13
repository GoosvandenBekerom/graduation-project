//*************************************************//
//          INTHER LOGISTICS ENGINEERING           //
//*************************************************//

package com.inthergroup.webpoc.resultExtraction;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author gvandenbekerom
 * @since 05-Dec-18
 */
@Component
@Primary
public class DefaultResultExtractor implements ResultExtractor {
    @Override
    public List<Map<String, Object>> extractResult(String query, Object[] filterValues, int[] filterJdbcTypes, int amount, JdbcTemplate db) {
        db.setMaxRows(amount);
        return db.queryForList(query, filterValues, filterJdbcTypes);
    }
}
