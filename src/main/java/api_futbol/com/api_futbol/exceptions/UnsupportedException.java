package api_futbol.com.api_futbol.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnsupportedException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    

}
