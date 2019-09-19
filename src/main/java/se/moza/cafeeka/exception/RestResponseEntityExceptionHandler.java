package se.moza.cafeeka.exception;

import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import javax.persistence.EntityNotFoundException;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    public RestResponseEntityExceptionHandler() {
        super();
    }

    // API
    // 400

    @ExceptionHandler({ ConstraintViolationException.class })
    public ResponseEntity<CustomExceptionResponse> handleBadRequest(final ConstraintViolationException ex) {

        CustomExceptionResponse response = new CustomExceptionResponse("Constraint Violation", "BAD REQUEST");
        return new ResponseEntity<CustomExceptionResponse>(response, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler({ DataIntegrityViolationException.class })
    public ResponseEntity<CustomExceptionResponse> handleBadRequest(final DataIntegrityViolationException ex) {

        CustomExceptionResponse response = new CustomExceptionResponse("Data Integritity Violation", "BAD REQUEST");
        return new ResponseEntity<CustomExceptionResponse>(response, HttpStatus.BAD_REQUEST);

    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(final HttpMessageNotReadableException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        final String bodyOfResponse = "HTTP message not readable.";
        // ex.getCause() instanceof JsonMappingException, JsonParseException
        return handleExceptionInternal(ex, bodyOfResponse, headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        final String bodyOfResponse = "Method argument not valid";
        return handleExceptionInternal(ex, bodyOfResponse, headers, HttpStatus.BAD_REQUEST, request);
    }



    // 404

    @ExceptionHandler(value = { EntityNotFoundException.class, CafeResourceNotFoundException.class })
    protected ResponseEntity<CustomExceptionResponse> handleNotFound(final RuntimeException ex) {

        CustomExceptionResponse response = new CustomExceptionResponse(ex.getMessage(), "NOT FOUND");
        return new ResponseEntity<CustomExceptionResponse>(response, HttpStatus.NOT_FOUND);

    }

    // 409

    @ExceptionHandler({ InvalidDataAccessApiUsageException.class, DataAccessException.class })
    protected ResponseEntity<CustomExceptionResponse> handleConflict(final RuntimeException ex) {

        CustomExceptionResponse response = new CustomExceptionResponse("Invalid data access api usage", "CONFLICT");
        return new ResponseEntity<CustomExceptionResponse>(response, HttpStatus.CONFLICT);

    }

    // 412
    // 500

    @ExceptionHandler({ NullPointerException.class, IllegalArgumentException.class, IllegalStateException.class })
    /*500*/public ResponseEntity<CustomExceptionResponse> handleInternal(final RuntimeException ex) {
        logger.error("500 Status Code", ex);

        CustomExceptionResponse response = new CustomExceptionResponse("Illegal argument or state", "INTERNAL_SERVER_ERROR");
        return new ResponseEntity<CustomExceptionResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);

    }
}
