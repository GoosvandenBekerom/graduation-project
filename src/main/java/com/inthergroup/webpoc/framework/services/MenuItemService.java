//*************************************************//
//          INTHER LOGISTICS ENGINEERING           //
//*************************************************//

package com.inthergroup.webpoc.framework.services;

import com.inthergroup.webpoc.framework.config.Constants;
import com.inthergroup.webpoc.framework.domain.Action;
import com.inthergroup.webpoc.framework.domain.MenuItem;
import com.inthergroup.webpoc.framework.repositories.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gvandenbekerom
 * @since 17-Sep-18
 */
@Service
public class MenuItemService extends EntityService<MenuItem, Long, MenuItemRepository> {
    private final ActionService actionService;

    @Autowired
    public MenuItemService(MenuItemRepository repo, ActionService actionService) {
        super(repo);
        this.actionService = actionService;
    }

    public MenuItem setParent(@NonNull MenuItem item, @NonNull MenuItem parent) {
        item.setParent(parent);
        parent.getChildren().add(item);
        super.save(parent);
        return super.save(item);
    }

    public MenuItem setAction(MenuItem item, Action action) {
        item.setAction(action);
        action.setMenuItem(item);
        actionService.save(action);
        return super.save(item);
    }

    public MenuItem removeParent(MenuItem item) {
        item.removeParent();
        return super.save(item);
    }

    public MenuItem removeAction(MenuItem item) {
        item.setAction(null);
        return super.save(item);
    }

    /**
     * Searches for the depth if the given MenuItem was the root
     * @param item to find the depth for
     * @return the depth after the given item
     */
    public int findDepthAfterItem(MenuItem item) {
        List<MenuItem> currentChildren = item.getChildren();
        if (currentChildren == null || currentChildren.isEmpty()) return 0;

        int depth = 1;
        List<MenuItem> temp = new ArrayList<>();

        while(depth < Constants.MAX_MENU_DEPTH) {
            for (MenuItem child : currentChildren) {
                temp.addAll(child.getChildren());
            }

            if (temp.isEmpty()) break;
            else {
                temp.clear();
                depth++;
            }
        }
        return depth;
    }

    /**
     * Checks children recursively for parent child violations.
     * @param item child to check
     * @param newParent parent to be
     * @return true when violated (if the new parent is the child or one of it's (recursive) children)
     */
    public boolean checkChildrenForNewParentRecursive(MenuItem item, MenuItem newParent) {
        List<MenuItem> children = item.getChildren();
        if (children.size() != 0) {
            for (MenuItem child : children) {
                if (child.equals(item)) return true;
                if (checkChildrenForNewParentRecursive(child, newParent)) return true;
            }
        }
        return false;
    }
}
