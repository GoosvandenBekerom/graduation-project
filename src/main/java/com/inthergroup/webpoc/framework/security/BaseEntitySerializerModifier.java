//*************************************************//
//          INTHER LOGISTICS ENGINEERING           //
//*************************************************//

package com.inthergroup.webpoc.framework.security;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.inthergroup.webpoc.framework.domain.BaseEntity;

/**
 * @author gvandenbekerom
 * @since 30-Nov-18
 */
public class BaseEntitySerializerModifier extends BeanSerializerModifier {
    @Override
    public JsonSerializer<?> modifySerializer(SerializationConfig config, BeanDescription beanDesc, JsonSerializer<?> serializer) {
        if (BaseEntity.class.isAssignableFrom(beanDesc.getBeanClass())) {
            return new BaseEntitySecurityGroupSerializer((JsonSerializer<Object>) serializer);
        }
        return serializer;
    }
}
