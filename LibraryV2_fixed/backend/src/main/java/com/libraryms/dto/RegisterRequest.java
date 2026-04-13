package com.libraryms.dto;
import jakarta.validation.constraints.Email; import jakarta.validation.constraints.NotBlank; import jakarta.validation.constraints.Size;
public class RegisterRequest {
    @NotBlank private String name;
    @NotBlank @Email private String email;
    @NotBlank @Size(min=6) private String password;
    private String phone;
    private String address;
    private String memberIdNumber;   // Student/NIC ID number
    public String getName(){return name;} public void setName(String v){name=v;}
    public String getEmail(){return email;} public void setEmail(String v){email=v;}
    public String getPassword(){return password;} public void setPassword(String v){password=v;}
    public String getPhone(){return phone;} public void setPhone(String v){phone=v;}
    public String getAddress(){return address;} public void setAddress(String v){address=v;}
    public String getMemberIdNumber(){return memberIdNumber;} public void setMemberIdNumber(String v){memberIdNumber=v;}
}
