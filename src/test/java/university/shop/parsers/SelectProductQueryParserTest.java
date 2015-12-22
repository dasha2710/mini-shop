package university.shop.parsers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import university.shop.dao.ProductRepository;
import university.shop.exception.ApiException;
import university.shop.exception.BadRequestApiException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Created by dara on 12/4/2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class SelectProductQueryParserTest {
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private SelectProductQueryParser parser = new SelectProductQueryParser();

    @Test
    public void shouldFindProductByTitle() throws ApiException {
        parser.parse("SELECT PRODUCT WITH TITLE TOY");

        verify(productRepository).findByTitleIgnoreCase("TOY");
    }

    @Test
    public void shouldFindProductByTitleAndCode() throws ApiException {
        parser.parse("SELECT PRODUCT WITH TITLE TOY CODE 11111");

        verify(productRepository).findByCodeAndTitleIgnoreCase("11111", "TOY");
    }

    @Test
    public void shouldFindProductByTitleAndCodeInDifferentOrder() throws ApiException {
        parser.parse("SELECT PRODUCT WITH CODE 11111 TITLE TOY");

        verify(productRepository).findByCodeAndTitleIgnoreCase("11111", "TOY");
    }

    @Test
    public void shouldFindProductByCode() throws ApiException {
        parser.parse("SELECT PRODUCT WITH CODE 11111");

        verify(productRepository).findByCodeIgnoreCase("11111");
    }

    @Test
    public void shouldFindProductByCodeFailsWhenFirstParameterIsAbsent() throws ApiException {
        try {
            parser.parse("SELECT PRODUCT WITH TITLE CODE 11111");
            fail("Should have thrown BadRequestException but did not!");
        } catch (BadRequestApiException e) {
            assertEquals(e.getMessage(), "Unknown filter was found '11111'. Must be only 'TITLE' or 'CODE' for 'PRODUCT'");
        }

        verifyNoMoreInteractions(productRepository);
    }

    @Test
    public void shouldFindProductByCodeFailsWhenParameterIsAbsent() throws ApiException {
        try {
            parser.parse("SELECT PRODUCT WITH TITLE TOY CODE ");
            fail("Should have thrown BadRequestException but did not!");
        } catch (BadRequestApiException e) {
            assertEquals(e.getMessage(), "Was not found value for filter 'CODE'");
        }

        verifyNoMoreInteractions(productRepository);
    }

    @Test
    public void shouldFindProductByCodeFailsThereAreLotOfParameters() throws ApiException {
        try {
            parser.parse("SELECT PRODUCT WITH TITLE TOY CODE 1111 AND");
            fail("Should have thrown BadRequestException but did not!");
        } catch (BadRequestApiException e) {
            assertEquals(e.getMessage(), "Unknown leksem was found after 'TOY'");
        }

        verifyNoMoreInteractions(productRepository);
    }

}