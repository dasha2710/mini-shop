package university.shop.parsers;

import jersey.repackaged.com.google.common.collect.Lists;
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
import java.util.Map;

/**
 * Created by dara on 12/5/2015.
 */
@Service
public class UpdateSectionQueryParser {
    private static final List<String> COLUMN_NAMES = Lists.newArrayList("NAME", "DESCRIPTION");
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
            throw new BadRequestApiException("Section name must be followed by keyword 'WITH'");
        }

        if (leksems.size() < 5) {
            throw new BadRequestApiException("Was not found new values. Columns and values must be specified after WITH");
        }

        Map<String, String> columnValueMap = ParserHelper.getValuesFromQuery(leksems, 4, COLUMN_NAMES);
        foundAndUpdateSection(sectionName, columnValueMap);
        return "Section was successfully updated";
    }

    @Transactional
    private void foundAndUpdateSection(String sectionName, Map<String, String> columnValueMap) throws ApiException {
        Section section = sectionRepository.findByNameIgnoreCase(sectionName);
        if (section == null) {
            throw new NotFoundApiException("The section with name " + sectionName + " was not found");
        }
        String name = columnValueMap.get("NAME");
        if (name != null && sectionRepository.findByNameIgnoreCase(name) != null) {
            throw new ConflictApiException("The section with name " + name + " alreadyExists");
        }
        name = (name == null) ? sectionName : name;
        String description = columnValueMap.get("DESCRIPTION");
        description = (description == null) ? section.getDescription() : description;
        sectionRepository.update(name, description, sectionName);
    }
}
