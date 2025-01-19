package api_futbol.com.api_futbol.Exception;

import org.springframework.http.HttpStatus;

public class ConflictException extends RuntimeException{
    private final HttpStatus status = HttpStatus.CONFLICT;

    public ConflictException(String message){
        super(message);
    }

    public HttpStatus geStatus(){
        return status;
    }

}
