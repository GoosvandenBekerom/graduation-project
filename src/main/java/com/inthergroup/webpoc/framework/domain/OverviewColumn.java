//*************************************************//
//          INTHER LOGISTICS ENGINEERING           //
//*************************************************//

package com.inthergroup.webpoc.framework.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * @author gvandenbekerom
 * @since 23-Oct-18
 */
@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public @Data class OverviewColumn extends BaseEntity {
    @ManyToOne
    @JsonIgnore
    private Overview overview;

    private String name;
    @NotNull
    private String queryColumn;

    private int jdbcType;
    private String jdbcTypeName;

    private boolean visible;

    /**
     * Used to determine display order
     */
    private int sequenceIndex;

    private boolean sortEnabled;
    private int sortIndex;
    private boolean sortAscending;

    /**
     * Indicates if this column should show a custom dropdown filter
     * if true, the value in dropdownFilterValues will be used to fill the dropdown
     */
    private boolean dropdownFilter;
    /**
     * Comma separated collection of values to fill dropdown with,
     * Or SQL that returns with possible values
     *
     * SQL Should be prefixed with "SQL&gt;"
     */
    private String dropdownFilterValues = "";
}
