package university.shop.exception;

/**
 * Author: Gennadii Cherniaiev
 * Date: 11/3/2015
 */
public class ApiException extends Exception {

    private String message;
    private int responseStatus;

    public ApiException(int responseStatus, String message) {
        this.responseStatus = responseStatus;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public int getResponseStatus() {
        return responseStatus;
    }
}
