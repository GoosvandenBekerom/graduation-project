//*************************************************//
//          INTHER LOGISTICS ENGINEERING           //
//*************************************************//

package com.inthergroup.webpoc.framework.services;

import com.inthergroup.webpoc.framework.domain.SecurityGroup;
import com.inthergroup.webpoc.framework.repositories.SecurityGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author gvandenbekerom
 * @since 26-Nov-18
 */
@Service
public class SecurityGroupService extends EntityService<SecurityGroup, String, SecurityGroupRepository> {
    @Autowired
    SecurityGroupService(SecurityGroupRepository repo) {
        super(repo);
    }

    public List<SecurityGroup> generateFromIdStrings(List<String> userGroups) {
        return userGroups.stream().map(
                userGroup -> repo
                        .findById(userGroup)
                        .orElse(repo.save(new SecurityGroup(userGroup)))
        ).collect(Collectors.toList());
    }
}
