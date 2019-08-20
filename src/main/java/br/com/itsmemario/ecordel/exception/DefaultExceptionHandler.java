package br.com.itsmemario.ecordel.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DefaultExceptionHandler {
	
	@Autowired
	private MessageSource messageSource;
	
	@ResponseStatus(code=HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public List<FormError> handle(MethodArgumentNotValidException exception) {
		List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();

		return fieldErrors.stream()
				.map( err -> {
						String msg = messageSource.getMessage(err, LocaleContextHolder.getLocale());
						return new FormError(err.getField(), msg);
					}
				) .collect(Collectors.toList());
	}	

}
