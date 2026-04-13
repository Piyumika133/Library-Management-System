package com.libraryms.exception;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime; import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntime(RuntimeException e){
        return ResponseEntity.badRequest().body(new ErrResp(400, e.getMessage()));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException e){
        Map<String,String> errs = new LinkedHashMap<>();
        e.getBindingResult().getAllErrors().forEach(err->errs.put(((FieldError)err).getField(), err.getDefaultMessage()));
        return ResponseEntity.badRequest().body(new ErrResp(400,"Validation failed",errs));
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneral(Exception e){
        return ResponseEntity.internalServerError().body(new ErrResp(500,"Internal error"));
    }
    record ErrResp(int status, String message, Object details, LocalDateTime ts){
        ErrResp(int s, String m){ this(s,m,null,LocalDateTime.now()); }
        ErrResp(int s, String m, Object d){ this(s,m,d,LocalDateTime.now()); }
    }
}
