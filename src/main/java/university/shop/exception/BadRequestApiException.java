package university.shop.exception;

/**
 * Author: Gennadii Cherniaiev
 * Date: 11/3/2015
 */
public class BadRequestApiException extends ApiException {

    public BadRequestApiException(String message) {
        super(400, message);
    }
}
