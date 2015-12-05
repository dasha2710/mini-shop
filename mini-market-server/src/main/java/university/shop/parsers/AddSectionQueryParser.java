package university.shop.parsers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import university.shop.dao.SectionRepository;
import university.shop.entities.Section;
import university.shop.exception.ApiException;
import university.shop.exception.BadRequestApiException;
import university.shop.exception.ConflictApiException;

import java.util.Arrays;
import java.util.List;

/**
 * Created by dara on 12/5/2015.
 */
@Service
public class AddSectionQueryParser {
    @Autowired
    private SectionRepository sectionRepository;

    public String parse(String query) throws ApiException {
        List<String> leksems = Arrays.asList(query.split(" "));

        if (leksems.size() < 3 || !leksems.get(2).equals("WITH")) {
            throw new BadRequestApiException("SECTION must be followed by keyword 'WITH'");
        }

        if (leksems.size() == 3) {
            throw new BadRequestApiException("Was not found required  value for column NAME");
        }

        String firstColumnName = leksems.get(3);
        if (!firstColumnName.equals("NAME") && !firstColumnName.equals("DESCRIPTION")) {
            throw new BadRequestApiException("Incorrect column name " + firstColumnName + " was passed. Must be only NAME or DESCRIPTION");
        }
        if (leksems.size() < 5) {
            throw new BadRequestApiException("Was not found value for column " + firstColumnName);
        }
        String firstColumnValue = leksems.get(4);
        if (leksems.size() == 5 && firstColumnName.equals("NAME")) {
            createAndSaveSection(firstColumnValue, null);
            return "Section was successfully created";
        }
        if (leksems.size() == 5 && !firstColumnName.equals("NAME")) {
            throw new BadRequestApiException("Was not found required  value for column NAME");
        }
        String secondColumnName = leksems.get(5);
        if (!secondColumnName.equals("NAME") && !secondColumnName.equals("DESCRIPTION")) {
            throw new BadRequestApiException("Incorrect column name " + secondColumnName + " was passed. Must be only NAME or DESCRIPTION");
        }
        if (leksems.size() < 7) {
            throw new BadRequestApiException("Was not found value for column " + secondColumnName);
        }
        String secondColumnValue = leksems.get(6);
        if (secondColumnName.equals(firstColumnName)) {
            throw new BadRequestApiException("The query has two values for the same column " + secondColumnName);
        }
        if (leksems.size() > 7) {
            throw new BadRequestApiException("The unrecognizable characters were found after " + secondColumnValue);
        }
        if (firstColumnName.equals("NAME")) {
            createAndSaveSection(firstColumnValue, secondColumnValue);
        } else {
            createAndSaveSection(secondColumnValue, firstColumnValue);
        }
        return "Section was successfully created";
    }

    @Transactional
    private void createAndSaveSection(String name, String description) throws ConflictApiException {
        if (sectionRepository.findByNameIgnoreCase(name) != null) {
            throw new ConflictApiException("The section with name " + name + " alreadyExists");
        }
        Section section = new Section();
        section.setName(name);
        section.setDescription(description);
        sectionRepository.save(section);
    }
}
