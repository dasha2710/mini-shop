package university.shop.parsers;

import jersey.repackaged.com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import university.shop.dao.ProductRepository;
import university.shop.dao.SectionRepository;
import university.shop.entities.Product;
import university.shop.entities.Section;
import university.shop.exception.ApiException;
import university.shop.exception.BadRequestApiException;
import university.shop.exception.ConflictApiException;
import university.shop.exception.NotFoundApiException;

import java.util.*;

/**
 * Created by dara on 12/5/2015.
 */
@Service
public class AddProductQueryParser {
    private final List<String> COLUMN_NAMES = Lists.newArrayList("CODE", "TITLE", "PRICE", "PRODUCER_COUNTRY");
    @Autowired
    private SectionRepository sectionRepository;
    @Autowired
    private ProductRepository productRepository;

    public String parse(String query) throws ApiException{
        List<String> leksems = Arrays.asList(query.split(" "));
        if (leksems.size() < 4) {
            throw new BadRequestApiException("PRODUCT must be following by TO 'section' for defining to what section add product");
        }
        String sectionName = leksems.get(3);

        if (leksems.size() < 5 || !leksems.get(4).equals("WITH")) {
            throw new BadRequestApiException("WITH was not found after section name");
        }

        if (leksems.size() < 6) {
            throw new BadRequestApiException("Was not found any values");
        }

        Map<String, String> columnValueMap = ParserHelper.getValuesFromQuery(leksems, 5, COLUMN_NAMES);

        if (!columnValueMap.containsKey("TITLE") || !columnValueMap.containsKey("CODE")) {
            throw new BadRequestApiException("Was not found required values for columns TITLE and CODE");
        }

        createAndSaveProduct(columnValueMap, sectionName);

        return "Product was successfully added";
    }

    private void createAndSaveProduct(Map<String, String> columnValueMap, String sectionName) throws ApiException {
        Section section = sectionRepository.findByNameIgnoreCase(sectionName);
        if (section == null) {
            throw new NotFoundApiException("Section with name " + " was not found");
        }
        Product existingProduct = productRepository.findByCodeIgnoreCase(columnValueMap.get("CODE"));
        if (existingProduct != null) {
            throw new ConflictApiException("The product with the same code already exists");
        }
        existingProduct = productRepository.findByTitleIgnoreCase(columnValueMap.get("TITLE"));
        if (existingProduct != null) {
            throw new ConflictApiException("The product with the same title already exists");
        }
        Product product = new Product();
        product.setCode(columnValueMap.get("CODE"));
        product.setTitle(columnValueMap.get("TITLE"));
        product.setPrice((columnValueMap.get("PRICE") == null) ? 0 : Integer.valueOf((columnValueMap.get("PRICE"))));
        product.setProducerCountry(columnValueMap.get("PRODUCER_COUNTRY"));
        product.setSection(section);
        productRepository.save(product);
    }


}
