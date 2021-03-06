package university.shop.export;

import org.springframework.stereotype.Service;
import university.shop.entities.Product;
import university.shop.entities.Section;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by dara on 12/5/2015.
 */
@Service
public class ExportService {
    //Delimiter used in CSV file
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";

    //CSV file header
    private static final String FILE_HEADER_PRODUCT = "code,title,price,producerCountry,section,sectionDescription";
    private static final String FILE_HEADER_SECTION = "name,description";

    public File exportProducts(List<Product> products) throws IOException {
        try (FileWriter fileWriter = new FileWriter("G:/products.csv")) {

            //Write the CSV file header
            fileWriter.append(FILE_HEADER_PRODUCT.toString());

            //Add a new line separator after the header
            fileWriter.append(NEW_LINE_SEPARATOR);

            for (Product product : products) {
                fileWriter.append(product.getCode());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(product.getTitle());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(product.getPrice()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(product.getProducerCountry());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(product.getSection().getName());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(product.getSection().getDescription());
                fileWriter.append(NEW_LINE_SEPARATOR);
            }
            return new File("G:/products.csv");
        }
    }

    public File exportSections(List<Section> sections) throws IOException {
        try (FileWriter fileWriter = new FileWriter("G:/sections.csv")) {

            //Write the CSV file header
            fileWriter.append(FILE_HEADER_SECTION.toString());

            //Add a new line separator after the header
            fileWriter.append(NEW_LINE_SEPARATOR);

            for (Section section : sections) {
                fileWriter.append(section.getName());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(section.getDescription());
                fileWriter.append(NEW_LINE_SEPARATOR);
            }
            return new File("G:/sections.csv");
        }
    }
}
