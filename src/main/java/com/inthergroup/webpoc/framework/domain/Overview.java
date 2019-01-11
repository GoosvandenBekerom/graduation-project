//*************************************************//
//          INTHER LOGISTICS ENGINEERING           //
//*************************************************//

package com.inthergroup.webpoc.framework.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author gvandenbekerom
 * @since 03-Oct-18
 */
@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public @Data class Overview extends BaseEntity {
    @NotNull
    private String name;
    @NotNull
    @Column(columnDefinition = "text")
    private String query;

    private int pageSize = 20;

    private boolean refreshEnabled = false;
    private int refreshRate = 30;

    private boolean autoRefreshFilters;

    /**
     * Set this to the query column name if the query/view has its own total count column
     * When this column is set the default count functionality should be ignored.
     */
    @Column(nullable = true)
    private String countColumn;

    @OneToMany(mappedBy = "overview", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"overview"})
    private List<OverviewColumn> columns;
}
