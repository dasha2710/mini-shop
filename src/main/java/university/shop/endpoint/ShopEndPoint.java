/*
 * Copyright 2015 Musicqubed.com. All Rights Reserved.
 */

package university.shop.endpoint;

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
public class ShopEndPoint {

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

    @Autowired
    private AddProductQueryParser addProductQueryParser;

    @Autowired
    private UpdateProductQueryParser updateProductQueryParser;

    @GET
    @Path("/dsl")
    @Produces("application/json")
    public ResponseDto parseQuery(@QueryParam("query") String query) throws ApiException {
        logger.info("Query: " + query);
        query = query.trim().replaceAll("\\s+", " ").toUpperCase();
        ResponseDto dto = new ResponseDto();
        if (query.startsWith("SELECT ALL")) {
            if (query.startsWith("SELECT ALL SECTIONS")) {
                dto.sections = selectSectionQueryParser.parseSelectAll(query);
            } else if (query.startsWith("SELECT ALL PRODUCTS FOR SECTION")) {
                dto.products = selectProductQueryParser.parseSelectAll(query);
            } else {
                throw new BadRequestApiException("SELECT ALL query is incorrect. Must be SELECT ALL PRODUCTS FOR SECTION **** or SELECT ALL SECTIONS");
            }
        } else if (query.startsWith("SELECT")) {
            if (query.startsWith("SELECT PRODUCT")) {
                dto.product = selectProductQueryParser.parse(query);
            } else if (query.startsWith("SELECT SECTION")) {
                dto.section = selectSectionQueryParser.parse(query);
            } else {
                throw new BadRequestApiException("SELECT query is incorrect. The name of entity was not recognized");
            }
        } else if (query.startsWith("DELETE")) {
            dto.message = deleteQueryParser.parse(query);
        } else if (query.startsWith("ADD")) {
            if (query.startsWith("ADD SECTION")) {
                dto.message = addSectionQueryParser.parse(query);
            } else if (query.startsWith("ADD PRODUCT")){
                dto.message = addProductQueryParser.parse(query);
            } else {
                throw new BadRequestApiException("ADD query is incorrect. The name of entity was not recognized");
            }
        } else if (query.startsWith("UPDATE")) {
            if (query.startsWith("UPDATE SECTION")) {
                dto.message = updateSectionQueryParser.parse(query);
            } else if (query.startsWith("UPDATE PRODUCT")) {
                dto.message = updateProductQueryParser.parse(query);
            } else {
                throw new BadRequestApiException("UPDATE query is incorrect. The name of entity was not recognized");
            }
        } else {
            throw new BadRequestApiException("Unrecognizable query");
        }
        return dto;
    }

    @GET
    @Path("/export_to_csv")
    @Produces({"application/vnd.ms-excel", "application/json"})
    public Response getFile(@QueryParam("query") String query) throws IOException, ApiException {
        File file;
        logger.info("Query: " + query);
        query = query.trim().replaceAll("\\s+", " ").toUpperCase();
        if (query.startsWith("SELECT ALL")) {
            if (query.startsWith("SELECT ALL SECTIONS")) {
                file = exportService.exportSections(selectSectionQueryParser.parseSelectAll(query));
            } else if (query.startsWith("SELECT ALL PRODUCTS FOR SECTION")) {
                file = exportService.exportProducts(selectProductQueryParser.parseSelectAll(query));
            } else {
                throw new BadRequestApiException("SELECT ALL query is incorrect. Must be SELECT ALL PRODUCTS FOR SECTION **** or SELECT ALL SECTIONS");
            }

            Response.ResponseBuilder response = Response.ok((Object) file);
            response.header("Content-Disposition",
                    "attachment; filename=products.xls");
            return response.build();
        }
        throw new BadRequestApiException("You could only export all sections or all product for the section");
    }

}
