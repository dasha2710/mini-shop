/*
 * Copyright 2015 Musicqubed.com. All Rights Reserved.
 */

package university.shop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import university.shop.entities.Product;
import university.shop.entities.Section;
import university.shop.exception.BadRequestApiException;
import university.shop.parsers.SelectProductQueryParser;
import university.shop.parsers.SelectSectionQueryParser;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Logger;

@Path("/")
@Component
public class ShopController {

    private final Logger logger = Logger.getLogger(getClass().getName());

    @Autowired
    private SelectProductQueryParser selectProductQueryParser;

    @Autowired
    private SelectSectionQueryParser selectSectionQueryParser;

    @GET
    @Path("/api")
    public Response parseQuery(@QueryParam("query") String query) throws BadRequestApiException {
        logger.info("Query: " + query);
        query = query.trim().toUpperCase();
        if (query.startsWith("SELECT")) {
            if (query.startsWith("SELECT PRODUCT")) {
                Product product = selectProductQueryParser.parse(query);
            } else if (query.startsWith("SELECT SECTION")) {
                Section section = selectSectionQueryParser.parse(query);
            } else if (query.startsWith("SELECT ALL SECTIONS")) {
                List<Section> sections = selectSectionQueryParser.parseSelectAll(query);
            } else if (query.startsWith("SELECT ALL PRODUCTS FOR SECTION")) {
                List<Product> products = selectProductQueryParser.parseSelectAll(query);
            } else {
                throw new BadRequestApiException("SELECT query is incorrect");
            }
        }
        return Response.ok().build();
    }
}
