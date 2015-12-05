/*
 * Copyright 2015 Musicqubed.com. All Rights Reserved.
 */

package university.shop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import university.shop.dto.ResponseDto;
import university.shop.exception.ApiException;
import university.shop.exception.BadRequestApiException;
import university.shop.export.ExportService;
import university.shop.parsers.*;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

@Path("/")
@Component
public class ShopController {

    private final Logger logger = Logger.getLogger(getClass().getName());

    @Autowired
    private ExportService exportService;

    @Autowired
    private SelectProductQueryParser selectProductQueryParser;

    @Autowired
    private SelectSectionQueryParser selectSectionQueryParser;

    @Autowired
    private DeleteQueryParser deleteQueryParser;

    @Autowired
    private AddSectionQueryParser addSectionQueryParser;

    @Autowired
    private UpdateSectionQueryParser updateSectionQueryParser;

    @GET
    @Path("/api")
    @Produces("application/json")
    public ResponseDto parseQuery(@QueryParam("query") String query) throws ApiException {
        logger.info("Query: " + query);
        query = query.trim().toUpperCase();
        ResponseDto dto = new ResponseDto();
        if (query.startsWith("SELECT")) {
            if (query.startsWith("SELECT PRODUCT")) {
                dto.product = selectProductQueryParser.parse(query);
            } else if (query.startsWith("SELECT SECTION")) {
                dto.section = selectSectionQueryParser.parse(query);
            } else if (query.startsWith("SELECT ALL SECTIONS")) {
                dto.sections = selectSectionQueryParser.parseSelectAll(query);
            } else if (query.startsWith("SELECT ALL PRODUCTS FOR SECTION")) {
                dto.products = selectProductQueryParser.parseSelectAll(query);
            } else {
                throw new BadRequestApiException("SELECT query is incorrect. The name of entity was not recognized");
            }
        } else if (query.startsWith("DELETE")) {
            dto.message = deleteQueryParser.parse(query);
        } else if (query.startsWith("ADD")) {
            if (query.startsWith("ADD SECTION")) {
                dto.message = addSectionQueryParser.parse(query);
            } else if (query.startsWith("ADD PRODUCT")){

            } else {
                throw new BadRequestApiException("ADD query is incorrect. The name of entity was not recognized");
            }
        } else if (query.startsWith("UPDATE")) {
            if (query.startsWith("UPDATE SECTION")) {
                dto.message = updateSectionQueryParser.parse(query);
            } else if (query.startsWith("UPDATE PRODUCT")) {

            } else {
                throw new BadRequestApiException("UPDATE query is incorrect. The name of entity was not recognized");
            }
        }
        return dto;
    }

    @GET
    @Path("/export_to_csv")
    @Produces("application/vnd.ms-excel")
    public Response getFile() throws IOException {

        File file = exportService.export();

        Response.ResponseBuilder response = Response.ok((Object) file);
        response.header("Content-Disposition",
                "attachment; filename=products.xls");
        return response.build();

    }

}
