package edu.school21.info.exception;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.MappingException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;

@ControllerAdvice
@Slf4j
class GlobalExceptionHandler {

    public static final String DEFAULT_ERROR_VIEW = "error";


    @ExceptionHandler(value = BindException.class)
    public ModelAndView bindExceptionHandler(BindException ex) {
        StringBuilder sb = new StringBuilder();
        ex.getBindingResult().getAllErrors()
                .stream()
                .filter(FieldError.class::isInstance)
                .map(FieldError.class::cast)
                .forEach(fieldError ->
                        sb.append(fieldError.getField())
                                .append(" - ")
                                .append(fieldError.getDefaultMessage())
                                .append('\n')
                );
        return packToTheFront(sb.toString());
    }

    @ExceptionHandler(value = MappingException.class)
    public ModelAndView mappingExceptionHandler(MappingException ex) {
        return packToTheFront(ex.getCause().getMessage());
    }

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public ModelAndView dataIntegrityViolationHandler(Exception ex) {
        return packToTheFront("The entered data violates the consistency of the database");
    }

    @ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler(Exception ex) {
        return packToTheFront(ex.getMessage());
    }

    private ModelAndView packToTheFront(String message) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("message", message);
        mav.setViewName(DEFAULT_ERROR_VIEW);
        log.warn(message);
        return mav;
    }


}