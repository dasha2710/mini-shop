package university.shop.parsers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import university.shop.dao.ProductRepository;
import university.shop.dao.SectionRepository;
import university.shop.exception.ApiException;
import university.shop.exception.BadRequestApiException;
import university.shop.exception.NotFoundApiException;

/**
 * Created by dara on 12/5/2015.
 */
@Component
public class DeleteQueryParser {
    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public String parse(String query) throws ApiException {
        String[] leksems = query.split(" ");
        if (leksems.length < 3) {
            throw new BadRequestApiException("Two small delete query. Must look like for example 'DELETE SECTION TOYS' or 'DELETE PRODUCT 1111'");
        }
        if (leksems.length > 3) {
            throw new BadRequestApiException("Two long delete query. Must look like for example 'DELETE SECTION TOYS' or 'DELETE PRODUCT 1111'");
        }
        String table = leksems[1];
        String value = leksems[2];
        if (table.equals("PRODUCT")) {
            if (productRepository.findByCodeIgnoreCase(value) == null) {
                throw new NotFoundApiException("Product with code " + value + " was not found");
            }
            productRepository.delete(value);
            return "Product with code " + value + " was deleted successfully";
        } else if (table.equals("SECTION")) {
            if (sectionRepository.findByNameIgnoreCase(value) == null) {
                throw new NotFoundApiException("Section with name " + value + " was not found");
            }
            sectionRepository.delete(value);
            return "Section with name " + value + " was deleted successfully";
        } else {
            throw new BadRequestApiException("Entity to delete was not recognized. It must be only PRODUCT or SECTION");
        }
    }
}
