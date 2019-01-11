//*************************************************//
//          INTHER LOGISTICS ENGINEERING           //
//*************************************************//

package com.inthergroup.webpoc.framework.security;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.inthergroup.webpoc.framework.config.Constants;
import com.inthergroup.webpoc.framework.domain.BaseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

/**
 * @author gvandenbekerom
 * @since 30-Nov-18
 *
 * This serializer makes sure that a request only returns values that the authorized user is allowed to see
 */
public class BaseEntitySecurityGroupSerializer extends StdSerializer<BaseEntity> {
    private final JsonSerializer<Object> defaultSerializer;

    public BaseEntitySecurityGroupSerializer(JsonSerializer<Object> defaultSerializer) {
        super(BaseEntity.class);
        this.defaultSerializer = defaultSerializer;
    }

    @Override
    public void serialize(BaseEntity value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        boolean isAllowed =
                auth.getPrincipal().equals(Constants.AUTH_GOD_MODE)
                || value.getSecurityGroups().stream()
                        .anyMatch(sg -> sg.getId()
                                .equals(auth.getAuthorities().toArray(new GrantedAuthority[]{})[0]
                                        .getAuthority().replace("ROLE_", "")));

        if (isAllowed) {
            defaultSerializer.serialize(value, gen, provider);
        }
    }
}
