/*
 * Copyright 2015 Musicqubed.com. All Rights Reserved.
 */

package university.shop.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

@Provider
public class JerseyMapperProvider implements ContextResolver<ObjectMapper> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.disable(MapperFeature.AUTO_DETECT_CREATORS)
                     .disable(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS)
                     .setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Override
    public ObjectMapper getContext(Class<?> aClass) {
        return OBJECT_MAPPER;
    }
}
