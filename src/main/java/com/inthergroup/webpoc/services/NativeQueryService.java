//*************************************************//
//          INTHER LOGISTICS ENGINEERING           //
//*************************************************//

package com.inthergroup.webpoc.services;

import com.inthergroup.webpoc.framework.config.Constants;
import com.inthergroup.webpoc.framework.domain.Overview;
import com.inthergroup.webpoc.framework.domain.OverviewColumn;
import com.inthergroup.webpoc.framework.domain.paging.OverviewPage;
import com.inthergroup.webpoc.framework.helper.Filter;
import com.inthergroup.webpoc.framework.helper.FilterStructure;
import com.inthergroup.webpoc.framework.web.errors.BadRequestException;
import com.inthergroup.webpoc.resultExtraction.ResultExtractor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * @author gvandenbekerom
 * @since 03-Oct-18
 */
@Service
@Log
public class NativeQueryService {
    private final JdbcTemplate db;
    private final ResultExtractor resultExtractor;

    @Autowired
    public NativeQueryService(@Qualifier("data") JdbcTemplate db, ResultExtractor resultExtractor) {
        this.db = db;
        this.resultExtractor = resultExtractor;
    }

    /**
     * Checks if a given sql string is a valid SELECT statement for the configured database
     * @param sql to check
     * @return true if query is executable SELECT query on configured database, false in any other case.
     */
    public boolean isValidSelectSyntax(String sql) {
        try {
            db.execute(String.format("SELECT * FROM ( %s ) as result WHERE false", sql));
        } catch (Exception e) {
            log.warning(e.getMessage());
            return false;
        }
        log.log(Level.INFO, String.format("Query [ %s ] marked as valid select query", sql));
        return true;
    }

    /**
     * Execute a select query
     * @param sql select query
     * @return Map containing values returned by select query
     */
    public List<Map<String, Object>> executeSelectQuery(String sql) {
        return db.queryForList(sql);
    }

    /**
     * Execute the given overview's query with sorting and pagination support based on its OverviewColumn's
     * @param overview to execute query for
     * @param page which page should be returned?
     * @param filters what columns should be filtered and by what value
     * @return Requested overview data page
     */
    public OverviewPage executeSortedPagedOverviewQuery(Overview overview, int page, Map<String, FilterStructure> filters) {

        // Get total count of filters
        int filterCount = (int) filters.values().stream()
                .flatMap(fs -> fs.getFilters().stream())
                .filter(filter -> !StringUtils.isEmpty(filter.getValue().toString()))
                .count();
        Object[] filterValues = new Object[filterCount];
        int[] filterTypes = new int[filterCount];
        // Get WHERE (AND) clause and fill array with ordered filter types
        String where = getWhereClauseAndFilterTypes(filters, overview.getColumns(), filterValues, filterTypes);

        // Get ORDER BY clause
        String orderBy = getOrderByClause(overview.getColumns());

        String query;
        if (StringUtils.isEmpty(overview.getCountColumn())) {
            // Create query to execute, also add total count window function
            query = String.format(
                    "SELECT *, count(*) OVER() AS %s FROM ( %s ) as x %s %s",
                    Constants.QUERY_TOTAL_COUNT_KEY, overview.getQuery(), where, orderBy
            );
        } else {
            // Create query without count window function
            query = String.format(
                    "SELECT * FROM ( %s ) as x %s %s", overview.getQuery(), where, orderBy
            );
        }

        // Add OFFSET clause
        int offset = (page - 1) * overview.getPageSize();
        String pageQuery = String.format("%s OFFSET %s", query, offset);

        log.log(Level.INFO, String.format("Executing Query [ %s ]", pageQuery));

        // Execute the query and and limit the resultSet to the set page size
        List<Map<String, Object>> content = resultExtractor.extractResult(pageQuery, filterValues, filterTypes, overview.getPageSize(), db);

        // Extract and remove the total count from the result
        String countColumn = StringUtils.isEmpty(overview.getCountColumn())
                ? Constants.QUERY_TOTAL_COUNT_KEY
                : overview.getCountColumn();

        int totalCount = content.isEmpty()
                ? 0
                : Integer.parseInt(content.get(0).get(countColumn).toString());

        for (Map<String, Object> row : content) {
            row.remove(countColumn);
        }

        return new OverviewPage(content, page, overview.getPageSize(), totalCount);
    }

    public ResultSetMetaData getMetaData(String sql) {
        return db.query(sql, ResultSet::getMetaData);
    }

    /**
     * Create WHERE clause for a query based on {@link com.inthergroup.webpoc.framework.helper.FilterStructure}'s per column
     *
     * @param filters containing columns and values of query to filter on (value contains FilterStructure with information about compare type and values)
     * @param columns of the overview this query is for
     * @param outParamValues reference to empty array of same size as filter values, will be filled with the parameter types in the correct order
     * @param outParamTypes reference to empty array of same size as filters (with values), will be filled with the parameter types in the correct order
     * @return WHERE clause based on column names as named parameters
     */
    private String getWhereClauseAndFilterTypes(Map<String, FilterStructure> filters, List<OverviewColumn> columns, Object[] outParamValues, int[] outParamTypes) {
        if (filters.isEmpty())
            return "";

        final StringBuilder where = new StringBuilder("WHERE ");

        int i = 0;
        boolean first = true;
        for (Map.Entry<String, FilterStructure> filter : filters.entrySet()) {
            if (!first) where.append("AND ");
            first = false;

            String key = filter.getKey();

            OverviewColumn column = columns.stream()
                    .filter(c -> c.getQueryColumn().equals(key))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Trying to filter unknown column"));

            if (column.isDropdownFilter()) {

                // Dropdown filtering
                boolean ddFirst = true;
                for(Filter entry : filter.getValue().getFilters()) {
                    if (!ddFirst) {
                        where.append(filter.getValue().getComparisonType().equals("AND") ? "AND " : "OR ");
                    }

                    switch (entry.getType()) {
                        case "DROPDOWN":
                            where.append(column.getQueryColumn()).append(" = ? ");
                            break;
                        default:
                            throw new BadRequestException("Filter type not supported.");
                    }

                    outParamValues[i] = entry.getValue();
                    outParamTypes[i] = column.getJdbcType();
                    i++;
                    ddFirst = false;
                }
            }
            else if (column.getJdbcType() == Types.VARCHAR || column.getJdbcType() == Types.CHAR) {

                // String (varchar/char) filtering
                boolean vFirst = true;
                for(Filter entry : filter.getValue().getFilters()) {
                    if (!vFirst) {
                        where.append(filter.getValue().getComparisonType().equals("AND") ? "AND " : "OR ");
                    }

                    String value = entry.getValue().toString();
                    boolean checkValue = true;

                    switch (entry.getType()) {
                        case "LIKE":
                            where.append("lower(").append(column.getQueryColumn()).append(") LIKE ? ");
                            value = "%" + value.toLowerCase() + "%";
                            break;

                        case "NOT LIKE":
                            where.append("lower(").append(column.getQueryColumn()).append(") NOT LIKE ? ");
                            value = "%" + value.toLowerCase() + "%";
                            break;

                        case "=":
                            where.append("lower(").append(column.getQueryColumn()).append(") = ? ");
                            value = value.toLowerCase();
                            break;

                        case "IS NULL":
                            where.append(column.getQueryColumn()).append(" IS NULL ");
                            checkValue = false;
                            break;

                        case "IS NOT NULL":
                            where.append(column.getQueryColumn()).append(" IS NOT NULL ");
                            checkValue = false;
                            break;

//                    case "REGEX":
//                        where.append(column.getQueryColumn()).append(" ~* ? ");
//                        break;

                        default:
                            throw new BadRequestException("Filter type not supported.");
                    }

                    if (checkValue) {
                        outParamValues[i] = value;
                        outParamTypes[i] = column.getJdbcType();
                        i++;
                    }
                    vFirst = false;
                }
            }
            else if (column.getJdbcType() == Types.NUMERIC || column.getJdbcType() == Types.INTEGER ||
                    column.getJdbcType() == Types.SMALLINT || column.getJdbcType() == Types.BIGINT) {

                // Whole number filtering
                boolean nFirst = true;
                for(Filter entry : filter.getValue().getFilters()) {
                    if (!nFirst) {
                        where.append(filter.getValue().getComparisonType().equals("AND") ? "AND " : "OR ");
                    }

                    switch (entry.getType()) {
                        case "=":
                            where.append(column.getQueryColumn()).append(" = ? ");
                            break;

                        case "<":
                            where.append(column.getQueryColumn()).append(" < ? ");
                            break;

                        case ">":
                            where.append(column.getQueryColumn()).append(" > ? ");
                            break;

                        case "<=":
                            where.append(column.getQueryColumn()).append(" <= ? ");
                            break;

                        case ">=":
                            where.append(column.getQueryColumn()).append(" >= ? ");
                            break;

                        case "!=":
                            where.append(column.getQueryColumn()).append(" <> ? ");
                            break;

                        default:
                            throw new BadRequestException("Filter type not supported.");
                    }

                    outParamValues[i] = entry.getValue();
                    outParamTypes[i] = column.getJdbcType();
                    i++;
                    nFirst = false;
                }
            }
            else if (column.getJdbcType() == Types.BOOLEAN || column.getJdbcType() == Types.BIT) {

                // Boolean filtering
                boolean bFirst = true;
                for(Filter entry : filter.getValue().getFilters()) {
                    if (!bFirst) {
                        where.append(filter.getValue().getComparisonType().equals("AND") ? "AND " : "OR ");
                    }

                    switch (entry.getType()) {
                        case "BOOLEAN":
                            where.append(column.getQueryColumn()).append(" = ? ");
                            break;

                        default:
                            throw new BadRequestException("Filter type not supported.");
                    }

                    outParamValues[i] = entry.getValue();
                    outParamTypes[i] = column.getJdbcType();
                    i++;
                    bFirst = false;
                }
            }
            else {
                // Anything unsupported, just check for equality with the given value
                boolean oFirst = true;
                for(Filter entry : filter.getValue().getFilters()) {
                    if (!oFirst) {
                        where.append(filter.getValue().getComparisonType().equals("AND") ? "AND " : "OR ");
                    }

                    where.append(column.getQueryColumn()).append(" = ? ");
                    outParamValues[i] = entry.getValue();
                    outParamTypes[i] = column.getJdbcType();
                    i++;
                    oFirst = false;
                }
            }
        }

        return where.toString();
    }

    /**
     * Create ORDER BY clause based on sorting settings in OverviewColumn's
     *
     * @param columns to base sorting on
     * @return ORDER BY clause based on given collection of overview columns
     */
    private String getOrderByClause(List<OverviewColumn> columns) {
        // Sort columns by their sorting index
        columns.sort(Comparator.comparingInt(OverviewColumn::getSortIndex));

        List<String> orderByColumns = new ArrayList<>();
        columns.forEach(c ->
        {
            if (c.isSortEnabled())
                orderByColumns.add(String.format("%s %s", c.getQueryColumn(), c.isSortAscending() ? "ASC" : "DESC"));
        });

        // If there are no sorting columns, return empty string
        if (orderByColumns.isEmpty()) return "";

        return String.format("ORDER BY %s", String.join(", ", orderByColumns));
    }

    /**
     * Create OFFSET and LIMIT clauses to a query, Also adds a column that represents the total amount of records
     *
     * Make sure to use this function only as the last query modifier, since anything after OFFSET and LIMIT will break the query.
     * @param sql to add OFFSET and LIMIT clauses to for pagination support
     * @param offset which index should the first record be?
     * @param pageSize how many records should the query return?
     * @return Given sql with pagination support attached
     * @deprecated since query manipulation uses subquery. left here for reference
     */
    @Deprecated
    private String getPaginationQuery(String sql, int offset, int pageSize) {
        String pageQuery = String.format("%s OFFSET %s LIMIT %s", sql, offset, pageSize);
        return pageQuery.replaceFirst(
                "[\\s]+(?i)from[\\s]+", // Find FROM keyword case insensitive
                String.format(", count(*) OVER() AS %s FROM ", Constants.QUERY_TOTAL_COUNT_KEY));
    }

    /**
     * Adds a column that represents the total amount of records
     * @param sql to add OFFSET and LIMIT clauses to for pagination support
     * @return Given sql with pagination support attached
     * @deprecated since query manipulation uses subquery. left here for reference
     */
    @Deprecated
    private String getTotalCountQuery(String sql) {
        return sql.replaceFirst(
                "[\\s]+(?i)from[\\s]+", // Find FROM keyword case insensitive
                String.format(", count(*) OVER() AS %s FROM ", Constants.QUERY_TOTAL_COUNT_KEY));
    }
}
