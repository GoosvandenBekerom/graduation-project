//*************************************************//
//          INTHER LOGISTICS ENGINEERING           //
//*************************************************//

package com.inthergroup.webpoc.framework.web;

import com.inthergroup.webpoc.framework.domain.Action;
import com.inthergroup.webpoc.framework.repositories.ActionRepository;
import com.inthergroup.webpoc.framework.services.ActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author gvandenbekerom
 * @since 27-Sep-18
 */
@RestController
@RequestMapping("/api/action")
public class ActionController extends BaseFrameworkController<Action, Long, ActionRepository, ActionService> {
    @Autowired
    ActionController(ActionService service) {
        super(service);
    }

    /**
     * Updates an action, clears the current action and replaces it with the requested action type/action
     * @param newObj new values for the entity should have @Valid and @RequestBody
     * @param id of the entity to update should have @PathVariable
     * @return updated Action
     */
    @Override
    @PutMapping("{id}")
    public Action update(@Valid @RequestBody Action newObj, @PathVariable Long id) {
        Action action = findById(id);
        action.setUrl(null);
        action.setOverviewId(null);
        action.setPath(null);
        action.setType(newObj.getType());

        switch (action.getType()) {
            case External_Url:
                action.setUrl(newObj.getUrl());
                break;
            case Overview:
                action.setOverviewId(newObj.getOverviewId());
                break;
            case Path:
                action.setPath(newObj.getPath());
                break;
        }
        return service.save(action);
    }
}
