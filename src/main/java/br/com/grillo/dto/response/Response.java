package br.com.grillo.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> {

    private T data;
    private Object errors;

    public void addErrorMsgToResponse(String msgError) {
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .message(msgError)
                .code(HttpStatus.BAD_REQUEST.value())
                .status(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .dateTime(LocalDateTime.now())
                .details("Tente novamente")
                .build();
        setErrors(exceptionResponse);
    }

}
