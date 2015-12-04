package university.shop.parsers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import university.shop.dao.SectionRepository;
import university.shop.exception.BadRequestApiException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Created by dara on 12/4/2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class SelectSectionQueryParserTest {
    @Mock
    private SectionRepository sectionRepository;

    @InjectMocks
    private SelectSectionQueryParser parser = new SelectSectionQueryParser();

    @Test
    public void shouldFindSectionByName() throws BadRequestApiException {
        parser.parse("SELECT SECTION WITH NAME TOYS");

        verify(sectionRepository).findByName("TOYS");
    }

    @Test
    public void shouldFindSectionByNameFailsWhenParameterIsAbsent() {
        try {
            parser.parse("SELECT SECTION WITH NAME");
            fail("Should have thrown BadRequestException but did not!");
        } catch (BadRequestApiException e) {
            assertEquals(e.getMessage(), "Two small select query. Must look like for example SELECT PRODUCT WITH CODE 1111");
        }

        verifyNoMoreInteractions(sectionRepository);

    }

    @Test
    public void shouldFindSectionByNameFailsWhenFilterIsNotValid() throws BadRequestApiException {
        try {
            parser.parse("SELECT SECTION WITH TITLE TOYS");
            fail("Should have thrown BadRequestException but did not!");
        } catch (BadRequestApiException e) {
            assertEquals(e.getMessage(), "Unknown filter was found 'TITLE'. Must be only 'NAME' for 'SECTION'");
        }

        verifyNoMoreInteractions(sectionRepository);
    }

}