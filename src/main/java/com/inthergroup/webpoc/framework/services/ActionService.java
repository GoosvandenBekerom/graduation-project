//*************************************************//
//          INTHER LOGISTICS ENGINEERING           //
//*************************************************//

package com.inthergroup.webpoc.framework.services;

import com.inthergroup.webpoc.framework.domain.Action;
import com.inthergroup.webpoc.framework.domain.ActionType;
import com.inthergroup.webpoc.framework.domain.Overview;
import com.inthergroup.webpoc.framework.repositories.ActionRepository;
import com.inthergroup.webpoc.framework.web.errors.FrameworkEntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author gvandenbekerom
 * @since 27-Sep-18
 */
@Service
public class ActionService extends EntityService<Action, Long, ActionRepository> {
    private final OverviewService overviewService;
    @Autowired
    ActionService(ActionRepository repo, OverviewService overviewService) {
        super(repo);
        this.overviewService = overviewService;
    }

    @Override
    public <S extends Action> S save(S action) {
        if (action.getType() == ActionType.Overview) {
            if (!overviewService.existsById(action.getOverviewId())) {
                throw new FrameworkEntityNotFoundException(Overview.class, action.getOverviewId());
            }
        }
        return super.save(action);
    }
}
