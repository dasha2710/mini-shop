/*
 * Copyright 2015 Musicqubed.com. All Rights Reserved.
 */

package university.shop.exception;

public class NotFoundApiException extends ApiException  {

    public NotFoundApiException(String message) {
        super(404, message);
    }
}
