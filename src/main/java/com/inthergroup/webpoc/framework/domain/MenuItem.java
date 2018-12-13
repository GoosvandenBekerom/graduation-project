//*************************************************//
//          INTHER LOGISTICS ENGINEERING           //
//*************************************************//

package com.inthergroup.webpoc.framework.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gvandenbekerom
 * @since 17-Sep-18
 */
@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public @Data class MenuItem extends BaseEntity {
    @NotNull
    @Size(min = 3, max = 50)
    private String text;

    @Getter
    @Setter(AccessLevel.NONE)
    private int depth;

    private String icon;

    @OneToOne
    private Action action; // if this crashes sometime it might have to do with securityGroup differences in item and action

    @ManyToOne
//    @JsonIgnoreProperties( { "createdAt", "updatedAt", "parent", "action", "children" })
    @JsonIgnore
    @Setter(AccessLevel.NONE) // To allow the custom setter below
    private MenuItem parent;
    @OneToMany(mappedBy = "parent")
    @EqualsAndHashCode.Exclude
    private List<MenuItem> children;

    public MenuItem(String text, Action action) {
        this.text = text;
        this.action = action;
        this.children = new ArrayList<>();
    }

    public boolean isLeaf() {
        return depth == 2 || (children != null && children.size() == 0);
    }

    public void setParent(MenuItem parent) {
        this.depth = parent.getDepth() + 1;
        this.parent = parent;
    }

    public void removeParent() {
        this.depth--;
        this.parent = null;
    }
}
