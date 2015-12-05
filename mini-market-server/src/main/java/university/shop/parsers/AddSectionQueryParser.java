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

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by dara on 12/5/2015.
 */
@Service
public class AddSectionQueryParser {
    private static final List<String> COLUMN_NAMES = Lists.newArrayList("NAME", "DESCRIPTION");
    @Autowired
    private SectionRepository sectionRepository;

    public String parse(String query) throws ApiException {
        List<String> leksems = Arrays.asList(query.split(" "));

        if (leksems.size() < 3 || !leksems.get(2).equals("WITH")) {
            throw new BadRequestApiException("SECTION must be followed by keyword 'WITH'");
        }

        Map<String, String> columnValueMap = ParserHelper.getValuesFromQuery(leksems, 3, COLUMN_NAMES);
        if (!columnValueMap.containsKey("NAME")) {
            throw new BadRequestApiException("Was not found required values for column NAME");
        }
        createAndSaveSection(columnValueMap);
        return "Section was successfully created";
    }

    @Transactional
    private void createAndSaveSection(Map<String, String> columnValueMa) throws ConflictApiException {
        String name = columnValueMa.get("NAME");
        if (sectionRepository.findByNameIgnoreCase(name) != null) {
            throw new ConflictApiException("The section with name " + name + " alreadyExists");
        }
        Section section = new Section();
        section.setName(name);
        section.setDescription(columnValueMa.get("DESCRIPTION"));
        sectionRepository.save(section);
    }
}
