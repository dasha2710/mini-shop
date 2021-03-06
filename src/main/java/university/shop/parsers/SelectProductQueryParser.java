package university.shop.parsers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import university.shop.dao.ProductRepository;
import university.shop.entities.Product;
import university.shop.exception.ApiException;
import university.shop.exception.BadRequestApiException;
import university.shop.exception.NotFoundApiException;

import java.util.List;

/**
 * Created by Dasha on 21.11.2015.
 */
@Component
public class SelectProductQueryParser {
    @Autowired
    private ProductRepository productRepository;

    public Product parse(String query) throws ApiException {
        String[] leksems = query.split(" ");
        if (leksems.length < 5) {
            throw new BadRequestApiException("Two small select query. Must look like for example SELECT PRODUCT WITH CODE 1111");
        }
        String with = leksems[2];
        if (!with.equals("WITH")) {
            throw new BadRequestApiException("The keyword 'with' is absent");
        }
        Product product = null;
        String filter = leksems[3];
        if (filter.equals("CODE") || filter.equals("TITLE")) {
            String value = leksems[4];
            if (leksems.length > 5) {
                String secondFilter = leksems[5];

                if (secondFilter.equals("CODE") || secondFilter.equals("TITLE")) {
                    if (leksems.length == 6) {
                        throw new BadRequestApiException("Was not found value for filter '" + secondFilter + "'");
                    }
                    String secondValue = leksems[6];
                    if (leksems.length > 7) {
                        throw new BadRequestApiException("Unknown leksem was found after '" + value + "'");
                    }
                    if (filter.equals("CODE") && secondFilter.equals("TITLE")) {
                        product = productRepository.findByCodeAndTitleIgnoreCase(value, secondValue);
                    } else if (secondFilter.equals("CODE") && filter.equals("TITLE")) {
                        product = productRepository.findByCodeAndTitleIgnoreCase(secondValue, value);
                    } else {
                        throw new BadRequestApiException("Filters must be different");
                    }
                } else {
                    throw new BadRequestApiException("Unknown filter was found '" + secondFilter + "'. Must be only 'TITLE' or 'CODE' for 'PRODUCT'");
                }
            } else {
                if (filter.equals("CODE")) {
                    product = productRepository.findByCodeIgnoreCase(value);
                } else {
                    product = productRepository.findByTitleIgnoreCase(value);
                }
            }
        } else {
            throw new BadRequestApiException("Unknown filter was found '" + filter + "'. Must be only 'TITLE' or 'CODE' for 'PRODUCT'");
        }
        if (product == null) {
            throw new NotFoundApiException("Product was not found");
        }
        return product;

    }

    public List<Product> parseSelectAll(String query) throws ApiException {
        String[] leksems = query.split(" ");
        if (leksems.length == 5) {
            throw new BadRequestApiException("Section name is absent");
        }
        if (leksems.length > 6) {
            throw new BadRequestApiException("SELECT ALL query must be only SELECT ALL SECTIONS or SELECT ALL PRODUCTS FOR SECTION aaaa");
        }
        String sectionName = leksems[5];
        List<Product> products = productRepository.findBySectionNameIgnoreCase(sectionName);
        if (products == null || products.isEmpty()) {
            throw new NotFoundApiException("Products were not found");
        }
        return products;
    }
}

