//*************************************************//
//          INTHER LOGISTICS ENGINEERING           //
//*************************************************//

package com.inthergroup.webpoc.framework.security.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * @author gvandenbekerom
 * @since 16-Nov-18
 */
@Service
public class UserDAO {
    private final JdbcTemplate db;

    @Autowired
    public UserDAO(@Qualifier("user") JdbcTemplate db) {
        this.db = db;
    }

    /**
     * Used to map queries for user information to User Pojo
     */
    private class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int i) throws SQLException {
            User user = new User();
            user.setId(rs.getString("id"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("hashpassword"));
            user.setName(rs.getString("name"));
            user.setLastName(rs.getString("lastname"));

            user.setCountryId(rs.getString("country_id"));
            user.setLanguageId(rs.getString("language_id"));
            user.setVariantId(rs.getString("variant_id"));

            user.setGroup(new UserGroup());
            user.getGroup().setId(rs.getString("usergroup_id"));
            user.getGroup().setLogicalId(rs.getString("logical_id"));
            user.getGroup().setDescription(rs.getString("description"));
            user.getGroup().setHierarchy(rs.getInt("hierarchy"));
            return user;
        }
    }

    private final String baseUserQuery =
            "SELECT u.id, u.logical_id as username, u.hashpassword, u.usergroup_id, pi.name, pi.lastname, pi.country_id, pi.language_id, pi.variant_id, ug.logical_id, ug.description, ug.hierarchy " +
            "FROM ilc_person_user u, ilc_personinfo pi, ilc_usergroup ug " +
            "WHERE u.personinfo_id = pi.id AND u.usergroup_id = ug.id";


    public Optional<User> findById(String id) {
        try {

            User user = db.queryForObject(baseUserQuery + " AND u.id = ?", new UserRowMapper(), id);

            if (user == null)
                return Optional.empty();

            return Optional.of(user);

        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<User> findByUsername(String username) {
        try {

            User user = db.queryForObject(baseUserQuery + " AND lower(u.logical_id) = ?", new UserRowMapper(), username.toLowerCase());

            if (user == null)
                return Optional.empty();

            return Optional.of(user);

        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    /**
     * Used to map queries for usergroup information to UserGroup Pojo
     */
    private class UserGroupRowMapper implements RowMapper<UserGroup> {
        @Override
        public UserGroup mapRow(ResultSet rs, int i) throws SQLException {
            UserGroup group = new UserGroup();
            group.setId(rs.getString("id"));
            group.setLogicalId(rs.getString("logical_id"));
            group.setDescription(rs.getString("description"));
            group.setHierarchy(rs.getInt("hierarchy"));
            return group;
        }
    }

    private final String baseUserGroupQuery = "SELECT id, logical_id, description, hierarchy FROM ilc_usergroup";

    public List<UserGroup> findAllUserGroups() {
        return db.query(baseUserGroupQuery, new UserGroupRowMapper());
    }

    public Optional<UserGroup> findUserGroupByLogicalId(String logicalId) {
        try {
            UserGroup group = db.queryForObject(baseUserGroupQuery + " WHERE lower(logical_id) = ?", new UserGroupRowMapper(), logicalId.toLowerCase());

            if (group == null)
                return Optional.empty();

            return Optional.of(group);

        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
