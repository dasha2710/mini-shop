package university.shop.parsers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import university.shop.dao.SectionRepository;
import university.shop.entities.Section;
import university.shop.exception.ApiException;
import university.shop.exception.BadRequestApiException;
import university.shop.exception.ConflictApiException;
import university.shop.exception.NotFoundApiException;

import java.util.Arrays;
import java.util.List;

/**
 * Created by dara on 12/5/2015.
 */
@Service
public class UpdateSectionQueryParser {
    @Autowired
    private SectionRepository sectionRepository;

    @Transactional
    public String parse(String query) throws ApiException {
        List<String> leksems = Arrays.asList(query.split(" "));

        if (leksems.size() == 2) {
            throw new BadRequestApiException("SECTION must be followed by the name of the section you want to update");
        }
        String sectionName = leksems.get(2);

        if (leksems.size() < 4 || !leksems.get(3).equals("WITH")) {
            throw new BadRequestApiException("SECTION must be followed by keyword 'WITH'");
        }

        if (leksems.size() < 5) {
            throw new BadRequestApiException("Was not found new values. Columns and values must be specified after WITH");
        }

        String firstColumnName = leksems.get(4);
        if (!firstColumnName.equals("NAME") && !firstColumnName.equals("DESCRIPTION")) {
            throw new BadRequestApiException("Incorrect column name " + firstColumnName + " was passed. Must be only NAME or DESCRIPTION");
        }
        if (leksems.size() < 6) {
            throw new BadRequestApiException("Was not found value for column " + firstColumnName);
        }
        String firstColumnValue = leksems.get(5);
        if (leksems.size() == 6) {
            if (firstColumnName.equals("NAME")) {
                foundAndUpdateSection(sectionName, firstColumnValue, null);
            } else {
                foundAndUpdateSection(sectionName, null, firstColumnValue);
            }
            return "Section was successfully updated";
        }
        String secondColumnName = leksems.get(6);
        if (!secondColumnName.equals("NAME") && !secondColumnName.equals("DESCRIPTION")) {
            throw new BadRequestApiException("Incorrect column name " + secondColumnName + " was passed. Must be only NAME or DESCRIPTION");
        }
        if (leksems.size() < 8) {
            throw new BadRequestApiException("Was not found value for column " + secondColumnName);
        }
        String secondColumnValue = leksems.get(7);
        if (secondColumnName.equals(firstColumnName)) {
            throw new BadRequestApiException("The query has two values for the same column " + secondColumnName);
        }
        if (leksems.size() > 8) {
            throw new BadRequestApiException("The unrecognizable characters were found after " + secondColumnValue);
        }
        if (firstColumnName.equals("NAME")) {
            foundAndUpdateSection(sectionName, firstColumnValue, secondColumnValue);
        } else {
            foundAndUpdateSection(sectionName, secondColumnValue, firstColumnValue);
        }
        return "Section was successfully updated";
    }

    @Transactional
    private void foundAndUpdateSection(String sectionName, String name, String description) throws ApiException {
        Section section = sectionRepository.findByNameIgnoreCase(sectionName);
        if (section == null) {
            throw new NotFoundApiException("The section with name " + sectionName + " was not found");
        }

        if (name != null && sectionRepository.findByNameIgnoreCase(name) != null) {
            throw new ConflictApiException("The section with name " + name + " alreadyExists");
        }
        name = (name == null) ? sectionName : name;
        description = (description == null) ? section.getDescription() : description;
        sectionRepository.update(name, description, sectionName);
    }
}
