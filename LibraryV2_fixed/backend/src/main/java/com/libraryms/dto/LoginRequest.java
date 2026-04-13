package com.libraryms.dto;
import jakarta.validation.constraints.Email; import jakarta.validation.constraints.NotBlank;
public class LoginRequest {
    @NotBlank @Email private String email;
    @NotBlank private String password;
    public String getEmail(){return email;} public void setEmail(String v){email=v;}
    public String getPassword(){return password;} public void setPassword(String v){password=v;}
}
