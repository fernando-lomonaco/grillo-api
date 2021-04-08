package br.com.grillo.exception;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import br.com.grillo.dto.response.ExceptionResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class CustomResponseEntity extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleCustomEntityException(final Exception ex, final WebRequest request) {

        final ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(), LocalDateTime.now(), request.getDescription(false));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionResponse);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
                                                                  final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        final List<ObjectError> errors = getErrors(ex);
        final ExceptionArgumentNotValidResponse errorResponse = getErrorResponse(ex, status, errors);
        return new ResponseEntity<>(errorResponse, status);
    }

    private ExceptionArgumentNotValidResponse getErrorResponse(MethodArgumentNotValidException ex, HttpStatus status,
                                                               List<ObjectError> errors) {
        return new ExceptionArgumentNotValidResponse("Request has invalids fields", status.value(),
                status.getReasonPhrase(), ex.getBindingResult().getObjectName(), LocalDateTime.now(), errors);
    }

    private List<ObjectError> getErrors(final MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new ObjectError(error.getDefaultMessage(), error.getField(), error.getRejectedValue()))
                .collect(Collectors.toList());
    }

}
