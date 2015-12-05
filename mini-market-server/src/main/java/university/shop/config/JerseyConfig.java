/*
 * Copyright 2015 Musicqubed.com. All Rights Reserved.
 */

package university.shop.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;
import university.shop.controllers.ShopController;
import university.shop.exception.ApiExceptionMapper;

public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        // Enable Spring DI
        register(RequestContextFilter.class);
        // JSON converter
        register(JerseyMapperProvider.class);

        register(ApiExceptionMapper.class);

        // Application endpoints
        register(ShopController.class);

    }
}
