package com.libraryms.dto;
public class AuthResponse {
    private String token;
    private Long id;
    private String name;
    private String email;
    private String role;
    private boolean idVerified;
    private String memberIdNumber;
    private String nicFrontPath;
    private String nicBackPath;

    public AuthResponse() {}
    public AuthResponse(String token, Long id, String name, String email, String role,
                        boolean idVerified, String memberIdNumber, String nicFrontPath, String nicBackPath) {
        this.token=token; this.id=id; this.name=name; this.email=email; this.role=role;
        this.idVerified=idVerified; this.memberIdNumber=memberIdNumber;
        this.nicFrontPath=nicFrontPath; this.nicBackPath=nicBackPath;
    }
    public String getToken(){return token;} public void setToken(String v){token=v;}
    public Long getId(){return id;} public void setId(Long v){id=v;}
    public String getName(){return name;} public void setName(String v){name=v;}
    public String getEmail(){return email;} public void setEmail(String v){email=v;}
    public String getRole(){return role;} public void setRole(String v){role=v;}
    public boolean isIdVerified(){return idVerified;} public void setIdVerified(boolean v){idVerified=v;}
    public String getMemberIdNumber(){return memberIdNumber;} public void setMemberIdNumber(String v){memberIdNumber=v;}
    public String getNicFrontPath(){return nicFrontPath;} public void setNicFrontPath(String v){nicFrontPath=v;}
    public String getNicBackPath(){return nicBackPath;} public void setNicBackPath(String v){nicBackPath=v;}
}
