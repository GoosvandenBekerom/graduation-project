//*************************************************//
//          INTHER LOGISTICS ENGINEERING           //
//*************************************************//

package com.inthergroup.webpoc.framework.web;

import com.inthergroup.webpoc.framework.config.Constants;
import com.inthergroup.webpoc.framework.domain.Action;
import com.inthergroup.webpoc.framework.domain.MenuItem;
import com.inthergroup.webpoc.framework.domain.SecurityGroup;
import com.inthergroup.webpoc.framework.repositories.MenuItemRepository;
import com.inthergroup.webpoc.framework.services.ActionService;
import com.inthergroup.webpoc.framework.services.MenuItemService;
import com.inthergroup.webpoc.framework.web.errors.FrameworkEntityNotFoundException;
import com.inthergroup.webpoc.framework.web.errors.MaxMenuDepthViolationException;
import com.inthergroup.webpoc.framework.web.errors.ParentChildViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gvandenbekerom
 * @since 17-Sep-18
 */
@RestController
@RequestMapping("/api/menuItem")
public class MenuItemController extends BaseFrameworkController<MenuItem, Long, MenuItemRepository, MenuItemService> {

    private final ActionService actionService;

    @Autowired
    public MenuItemController(MenuItemService service, ActionService actionService) {
        super(service);
        this.actionService = actionService;
    }

    @GetMapping("/{id}/parent")
    public MenuItem getParent(@PathVariable long id) {
        MenuItem item = findById(id);
        return item.getParent();
    }

    @Override
    @PutMapping("{id}")
    public MenuItem update(@Valid @RequestBody MenuItem newObj, @PathVariable Long id) {
        MenuItem item = findById(id);
        item.setText(newObj.getText());
        item.setIcon(newObj.getIcon());
        return service.save(item);
    }

    @PutMapping("/{id}/action")
    public MenuItem setAction(@PathVariable long id, @RequestParam long actionId) {
        MenuItem item = findById(id);
        Action action = actionService.findById(actionId)
                .orElseThrow(() -> new FrameworkEntityNotFoundException(Action.class, actionId));

        List<SecurityGroup> groups = new ArrayList<>(item.getSecurityGroups());
        action.setSecurityGroups(groups);

        return service.setAction(item, action);
    }

    @PutMapping("/{id}/parent")
    public MenuItem setParent(@PathVariable long id, @RequestParam long parentId) {
        if (id == parentId) {
            throw new ParentChildViolationException();
        }

        MenuItem item = findById(id);
        MenuItem parent = findById(parentId);

        // new parent is already parent, just return the item
        if (item.getParent() != null && item.getParent().equals(parent)) return item;
        // new parent is already at the maximum depth
        if (parent.getDepth() >= Constants.MAX_MENU_DEPTH) throw new MaxMenuDepthViolationException();
        // check if this item is part of a structure that is already at the maximum depth or if it will pass it after this operation
        int currentDepth = service.findDepthAfterItem(item);
        if (currentDepth + parent.getDepth() >= Constants.MAX_MENU_DEPTH) throw new MaxMenuDepthViolationException();
        // check if the requested relationship creates a problematic recursive relationship
        if (service.checkChildrenForNewParentRecursive(item, parent)) throw new ParentChildViolationException();

        return service.setParent(item, parent);
    }

    @DeleteMapping("/{id}/parent")
    public MenuItem removeParent(@PathVariable long id) {
        MenuItem item = findById(id);
        return service.removeParent(item);
    }

    @DeleteMapping("/{id}/action")
    public MenuItem removeAction(@PathVariable long id) {
        MenuItem item = findById(id);
        return service.removeAction(item);
    }
}
