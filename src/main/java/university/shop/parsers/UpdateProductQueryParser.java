package university.shop.parsers;

import jersey.repackaged.com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import university.shop.dao.ProductRepository;
import university.shop.entities.Product;
import university.shop.exception.ApiException;
import university.shop.exception.BadRequestApiException;
import university.shop.exception.ConflictApiException;
import university.shop.exception.NotFoundApiException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by dara on 12/5/2015.
 */
@Service
public class UpdateProductQueryParser {
    private final List<String> COLUMN_NAMES = Lists.newArrayList("CODE", "TITLE", "PRICE", "PRODUCER_COUNTRY");
    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public String parse(String query) throws ApiException {
        List<String> leksems = Arrays.asList(query.split(" "));

        if (leksems.size() == 2) {
            throw new BadRequestApiException("PRODUCT must be followed by the code of the product you want to update");
        }
        String productCode = leksems.get(2);

        if (leksems.size() < 4 || !leksems.get(3).equals("WITH")) {
            throw new BadRequestApiException("Product name must be followed by keyword 'WITH'");
        }

        if (leksems.size() < 5) {
            throw new BadRequestApiException("Was not found new values. Columns and values must be specified after WITH");
        }

        Map<String, String> columnValueMap = ParserHelper.getValuesFromQuery(leksems, 4, COLUMN_NAMES);
        foundAndUpdateProduct(productCode, columnValueMap);
        return "Product was successfully updated";
    }

    @Transactional
    private void foundAndUpdateProduct(String productCode, Map<String, String> columnValueMap) throws ApiException {
        Product product = productRepository.findByCodeIgnoreCase(productCode);
        if (product == null) {
            throw new NotFoundApiException("The product with code " + productCode + " was not found");
        }
        String code = columnValueMap.get("CODE");
        if (code != null) {
            Product existingProduct = productRepository.findByCodeIgnoreCase(code);
            if (existingProduct != null) {
                throw new ConflictApiException("The product with the same code already exists");
            }
            product.setCode(code);
        }
        String title = columnValueMap.get("TITLE");
        if (title != null) {
            Product existingProduct = productRepository.findByTitleIgnoreCase(title);
            if (existingProduct != null) {
                throw new ConflictApiException("The product with the same title already exists");
            }
            product.setTitle(title);
        }
        String price = columnValueMap.get("PRICE");
        if (price != null) {
            product.setPrice(Integer.valueOf(price));
        }
        String producerCountry = columnValueMap.get("PRODUCER_COUNTRY");
        if (producerCountry != null) {
            product.setProducerCountry(producerCountry);
        }
        productRepository.save(product);
    }
}
