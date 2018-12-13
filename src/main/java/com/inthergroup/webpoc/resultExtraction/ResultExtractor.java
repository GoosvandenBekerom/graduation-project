//*************************************************//
//          INTHER LOGISTICS ENGINEERING           //
//*************************************************//

package com.inthergroup.webpoc.resultExtraction;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

/**
 * @author gvandenbekerom
 * @since 05-Dec-18
 */
public interface ResultExtractor {
    /**
     * Extract a given amount of results from a native query with JdbcTemplate
     * @param query
     * @param filterValues
     * @param filterJdbcTypes
     * @return A collection of the results
     */
    List<Map<String, Object>> extractResult(String query, Object[] filterValues, int[] filterJdbcTypes, int amount, JdbcTemplate db);
}
