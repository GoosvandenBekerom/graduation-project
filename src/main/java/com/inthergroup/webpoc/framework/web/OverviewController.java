//*************************************************//
//          INTHER LOGISTICS ENGINEERING           //
//*************************************************//

package com.inthergroup.webpoc.framework.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inthergroup.webpoc.framework.domain.Overview;
import com.inthergroup.webpoc.framework.domain.OverviewColumn;
import com.inthergroup.webpoc.framework.domain.SecurityGroup;
import com.inthergroup.webpoc.framework.helper.FilterStructure;
import com.inthergroup.webpoc.framework.repositories.OverviewRepository;
import com.inthergroup.webpoc.framework.services.OverviewService;
import com.inthergroup.webpoc.framework.domain.paging.OverviewPage;
import com.inthergroup.webpoc.framework.services.SecurityGroupService;
import com.inthergroup.webpoc.framework.web.errors.FilterMappingException;
import com.inthergroup.webpoc.framework.web.errors.FrameworkEntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gvandenbekerom
 * @since 03-Oct-18
 */
@RestController
@RequestMapping("/api/overview")
public class OverviewController extends BaseFrameworkController<Overview, Long, OverviewRepository, OverviewService> {
    private final SecurityGroupService securityGroupService;

    @Autowired
    OverviewController(OverviewService service, SecurityGroupService securityGroupService) {
        super(service);
        this.securityGroupService = securityGroupService;
    }

    @Override
    @PostMapping
    public Overview create(@Valid @RequestBody Overview obj) {
        return service.save(obj, true);
    }

    @Override
    @PutMapping("{id}")
    public Overview update(@Valid @RequestBody Overview newObj, @PathVariable Long id) {
        Overview overview = findById(id);
        overview.setName(newObj.getName());
        overview.setAutoRefreshFilters(newObj.isAutoRefreshFilters());
        overview.setRefreshEnabled(newObj.isRefreshEnabled());
        overview.setRefreshRate(newObj.getRefreshRate());
        overview.setPageSize(newObj.getPageSize());
        overview.setCountColumn(newObj.getCountColumn());

        if (overview.getQuery().equals(newObj.getQuery())) {
            return service.save(overview);
        }

        overview.setQuery(newObj.getQuery());
        return service.save(overview, true);
    }

    /**
     * Rest endpoint for overview data page.
     *
     * Extracts page, sortBy and sortOrder values from query params.
     * All other query parameters are handled as (key) query column -> (value) JSON representation of FilterStructure.
     *
     * @param overviewId of overview to get page for
     * @param params information about sorting, filtering and paging
     * @return {@link com.inthergroup.webpoc.framework.domain.paging.OverviewPage} Containing requested overview page
     */
    @GetMapping("/{id}/data")
    public OverviewPage getDataOfId(@PathVariable("id") long overviewId, @RequestParam Map<String, Object> params) {
        Overview o = findById(overviewId);

        int page = Integer.parseInt(params.getOrDefault("page", "1").toString());
        params.remove("page");

        Object sortBy = params.getOrDefault("sortBy", null);
        Object sortOrder = params.getOrDefault("sortOrder", null);
        params.remove("sortBy");
        params.remove("sortOrder");

        if (sortBy != null) {
            service.updateSort(o, sortBy.toString(), sortOrder != null ? sortOrder.toString() : "ASC");
        }

        // The remaining params should be filters for the data query
        // key = query column, value: json representation of FilterStructure
        ObjectMapper mapper = new ObjectMapper();
        Map<String, FilterStructure> filters = new HashMap<>();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            try {
                filters.put(param.getKey(), mapper.readValue(param.getValue().toString(), FilterStructure.class));
            } catch (IOException e) {
                throw new FilterMappingException();
            }
        }

        return service.getOverviewData(o, page, filters);
    }

    @PutMapping("/{id}/columns")
    public Overview updateColumns(@Valid @RequestBody List<OverviewColumn> columns, @PathVariable long id) {
        Overview overview = findById(id);
        // TODO: validate columns for overview?
        return service.updateColumns(overview, columns);
    }

    @GetMapping("/column/{id}/filterValues")
    public String[] getColumnFilterValues(@PathVariable long id) {
        OverviewColumn column = findColumnById(id);
        return service.getCommaSeparatedDropdownValues(column.getDropdownFilterValues()).split(",");
    }

    @PutMapping("column/{id}/allowedUserGroups")
    public void setAllowedUserGroupsForColumn(@RequestBody List<String> userGroups, @PathVariable long id) {
        OverviewColumn column = findColumnById(id);
        List<SecurityGroup> securityGroups = securityGroupService.generateFromIdStrings(userGroups);
        service.setAllowedUserGroupsForColumn(securityGroups, column);
    }

    private OverviewColumn findColumnById(long id) {
        return service.getColumnById(id).orElseThrow(() -> new FrameworkEntityNotFoundException(OverviewColumn.class, id));
    }
}
