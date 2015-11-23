/*
 * Copyright 2015 Musicqubed.com. All Rights Reserved.
 */

package university.shop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import university.shop.parsers.QueryParser;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

@Path("/")
@Component
public class ShopController {

    private final Logger logger = Logger.getLogger(getClass().getName());

    @Autowired
    private QueryParser queryParser;

    @GET
    @Path("/api")
    public Response parseQuery(@QueryParam("query") String query) {
        logger.info("Query: " + query);
        queryParser.parse();
        return Response.ok().build();
    }
}
