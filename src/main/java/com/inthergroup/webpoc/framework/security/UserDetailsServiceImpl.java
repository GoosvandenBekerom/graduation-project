//*************************************************//
//          INTHER LOGISTICS ENGINEERING           //
//*************************************************//

package com.inthergroup.webpoc.framework.security;

import com.inthergroup.webpoc.framework.security.user.User;
import com.inthergroup.webpoc.framework.security.user.UserDAO;
import com.inthergroup.webpoc.framework.web.errors.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author gvandenbekerom
 * @since 19-Nov-18
 *
 * This class makes most of the spring security magic possible
 * The loadUserByUsername function is used to map a Inther LC user to a spring security user
 *
 * This also makes @PreAuthorize hasRole() checks possible for endpoints and rest controllers with Inther LC user_group's
 */
@Primary
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserDAO userDAO;

    @Autowired
    public UserDetailsServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDAO.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getAuthorities(user));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        List<GrantedAuthority> permissions = new ArrayList<>();
        permissions.add(new SimpleGrantedAuthority(user.getGroup().getId())); // THIS HAS TO BE FIRST
        permissions.add(new SimpleGrantedAuthority("ROLE_"+user.getGroup().getLogicalId()));
        return permissions;
    }
}
