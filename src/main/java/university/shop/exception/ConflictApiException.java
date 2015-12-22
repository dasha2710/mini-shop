package university.shop.exception;

/**
 * Author: Gennadii Cherniaiev
 * Date: 11/3/2015
 */
public class ConflictApiException extends ApiException {

    public ConflictApiException(String message) {
        super(409, message);
    }
}
