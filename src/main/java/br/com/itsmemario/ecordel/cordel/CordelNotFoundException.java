package br.com.itsmemario.ecordel.cordel;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Cordel not found")
public class CordelNotFoundException extends RuntimeException {

}
