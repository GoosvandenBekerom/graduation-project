//*************************************************//
//          INTHER LOGISTICS ENGINEERING           //
//*************************************************//

package com.inthergroup.webpoc.framework.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inthergroup.webpoc.framework.config.Constants;
import com.inthergroup.webpoc.framework.validation.MenuActionValidation;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author gvandenbekerom
 * @since 27-Sep-18
 */
@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@MenuActionValidation
public @Data class Action extends BaseEntity {
    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private ActionType type;
    @Pattern(regexp = Constants.REGEX_PATTERN_URL)
    private String url;
    private Long overviewId;
    private String path;

    @OneToOne(mappedBy = "action")
    @JsonIgnore
    private MenuItem menuItem;
}

