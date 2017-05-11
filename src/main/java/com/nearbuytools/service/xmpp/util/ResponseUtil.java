package com.nearbuytools.service.xmpp.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {
    
    public static HttpEntity<Object> sendResponse(Object response, HttpHeaders headers, HttpStatus status) {
        return new ResponseEntity<Object>(response, headers, status);
    }
    
    /**
	 * Check the HTTPStatus for error
	 * 
	 * @param status
	 * @return
	 */
	public static boolean isError(final HttpStatus status) 
	{
        final HttpStatus.Series series = status.series();
        return HttpStatus.Series.CLIENT_ERROR.equals(series)
                || HttpStatus.Series.SERVER_ERROR.equals(series);
    }
    
}