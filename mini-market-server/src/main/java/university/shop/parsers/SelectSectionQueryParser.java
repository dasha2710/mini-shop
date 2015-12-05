package university.shop.parsers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import university.shop.dao.SectionRepository;
import university.shop.entities.Section;
import university.shop.exception.BadRequestApiException;

import java.util.List;

/**
 * Created by Dasha on 21.11.2015.
 */
@Component
public class SelectSectionQueryParser {

    @Autowired
    private SectionRepository sectionRepository;

    @Transactional
    public Section parse(String query) throws BadRequestApiException {
        String[] leksems = query.split(" ");
        if (leksems.length < 5) {
            throw new BadRequestApiException("Two small select query. Must look like for example SELECT PRODUCT WITH CODE 1111");
        }
        String with = leksems[2];
        if (!with.equals("WITH")) {
            throw new BadRequestApiException("The keyword 'with' is absent");
        }
        String filter = leksems[3];
        if (filter.equals("NAME")) {
            String value = leksems[4];
            if (leksems.length > 5) {
                throw new BadRequestApiException("Unknown leksem was found after '" + value + "'");
            }
            return sectionRepository.findByNameIgnoreCase(value);
        } else {
            throw new BadRequestApiException("Unknown filter was found '" + filter + "'. Must be only 'NAME' for 'SECTION'");
        }
    }

    public List<Section> parseSelectAll(String query) throws BadRequestApiException {
        String[] leksems = query.split(" ");
        if (leksems.length > 3) {
            throw new BadRequestApiException("SELECT ALL query must be only SELECT ALL SECTIONS or SELECT ALL PRODUCTS FOR SECTION aaaa");
        }
        return sectionRepository.findAll();
    }
}

