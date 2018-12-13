//*************************************************//
//          INTHER LOGISTICS ENGINEERING           //
//*************************************************//

package com.inthergroup.webpoc.framework.services;

import com.inthergroup.webpoc.framework.config.Constants;
import com.inthergroup.webpoc.framework.domain.Overview;
import com.inthergroup.webpoc.framework.domain.OverviewColumn;
import com.inthergroup.webpoc.framework.domain.SecurityGroup;
import com.inthergroup.webpoc.framework.domain.paging.OverviewPage;
import com.inthergroup.webpoc.framework.helper.FilterStructure;
import com.inthergroup.webpoc.framework.repositories.OverviewColumnRepository;
import com.inthergroup.webpoc.framework.repositories.OverviewRepository;
import com.inthergroup.webpoc.framework.web.errors.BadRequestException;
import com.inthergroup.webpoc.framework.web.errors.FrameworkEntityNotFoundException;
import com.inthergroup.webpoc.services.NativeQueryService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;

/**
 * @author gvandenbekerom
 * @since 03-Oct-18
 */
@Service
@Log
public class OverviewService extends EntityService<Overview, Long, OverviewRepository>{
    private final NativeQueryService nqs;
    private final OverviewColumnRepository columnRepository;

    @Autowired
    OverviewService(OverviewRepository repo, NativeQueryService nqs, OverviewColumnRepository columnRepository) {
        super(repo);
        this.nqs = nqs;
        this.columnRepository = columnRepository;
    }

    /**
     * Save an overview, and generate it's OverviewColumns.
     * Use this when the query of a overview changed
     *
     * Replaces all linked {@link com.inthergroup.webpoc.framework.domain.OverviewColumn}'s
     * with new ones based on the given overview's query (metadata)
     *
     * @param overview to save
     * @return saved overview
     */
    public <S extends Overview> S save(S overview, boolean generateColumns) {
        if (!generateColumns) {
            return super.save(overview);
        }

        // Remove any semicolon's from the query
        overview.setQuery(overview.getQuery().replaceAll(";", ""));

        if (!nqs.isValidSelectSyntax(overview.getQuery())) {
            throw new BadRequestException(Constants.VALIDATION_MESSAGE_SELECT_QUERY);
        }

        ResultSetMetaData metaData = nqs.getMetaData(overview.getQuery());

        // Remove old column relations
        List<OverviewColumn> currentColumns = overview.getColumns();
        if (currentColumns != null) {
            for (OverviewColumn c : currentColumns) {
                c.setOverview(null);
            }
            currentColumns.clear();
            columnRepository.deleteWhereOverviewIsNull();
        } else {
            overview.setColumns(new ArrayList<>());
        }

        try {
            for (int i = 1; i < metaData.getColumnCount() + 1; i++) {
                String column = metaData.getColumnName(i);
                OverviewColumn c = new OverviewColumn();
                c.setName(column);
                c.setQueryColumn(column);
                c.setJdbcType(metaData.getColumnType(i));
                c.setJdbcTypeName(metaData.getColumnTypeName(i));
                c.setSequenceIndex(i);
                c.setVisible(true);
                c.setOverview(overview);
                c.setSecurityGroups(overview.getSecurityGroups());
                overview.getColumns().add(c);
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

        return super.save(overview);
    }

    /**
     * Get a data page for a given overview
     * @param o overview to get data page for
     * @param page number representing the page to get
     * @param filters a map which should have query columns as keys and the values to filter for as values
     *                {column_one=column_one_filter_values, column_two=column_two_filter_values}
     * @return OverviewPage with the requested data
     */
    public OverviewPage getOverviewData(Overview o, int page, Map<String, FilterStructure> filters) {
        return nqs.executeSortedPagedOverviewQuery(o, page, filters);
    }

    /**
     * Update the columns of an overview
     *
     * @param overview to update columns of
     * @param columns updated columns for given overview
     * @return saved (jpa tracked) instance of given overview
     */
    public Overview updateColumns(Overview overview, List<OverviewColumn> columns) {
        for (OverviewColumn c : columns) {
            c.setOverview(overview);
        }
        overview.setColumns(columnRepository.saveAll(columns));
        return repo.save(overview);
    }

    /**
     * Updates the columns of a given overview to be sorted on the given column in the given order
     *
     * Currently only works for single column sorting, should be updated when multi column sorting is implemented
     *
     * @param overview to update sorting for
     * @param column to sort on
     * @param order of sort
     */
    public void updateSort(Overview overview, String column, String order) {
        List<OverviewColumn> columns = overview.getColumns();
        for (OverviewColumn c : columns) {
            if (c.getQueryColumn().equals(column)) {
                c.setSortEnabled(true);
                c.setSortAscending(order.equals("ASC"));
                columnRepository.save(c);
            } else {
                if (c.isSortEnabled()) {
                    c.setSortEnabled(false);
                    c.setSortIndex(0);
                    columnRepository.save(c);
                }
            }
        }
    }

    public Optional<OverviewColumn> getColumnById(long id) {
        return columnRepository.findById(id);
    }

    /**
     * Get a comma separated string of the given input string
     *
     * @param baseValue value to groupChange into comma separated string
     * @return values of the first column in query result if the baseValue is an sql query
     * otherwise just returns a trimmed version of the baseValue
     */
    public String getCommaSeparatedDropdownValues(String baseValue) {

        if (baseValue.startsWith("SQL>")) {
            String query = baseValue.replaceFirst("SQL>", "");
            List<Map<String, Object>> result = this.nqs.executeSelectQuery(query);

            List<String> values = new ArrayList<>();
            for(Map<String, Object> entry : result) {
                // Get the first value and add it to the result
                values.add(entry.entrySet().iterator().next().getValue().toString().trim());
            }
            return String.join(",", values);
        }

        return baseValue.trim();
    }

    public void setAllowedUserGroupsForColumn(List<SecurityGroup> securityGroups, OverviewColumn column) {
        column.setSecurityGroups(securityGroups);
        columnRepository.save(column);
    }
}
