package com.libraryms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "member")
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank @Column(nullable = false)
    private String name;

    @Email @NotBlank @Column(unique = true, nullable = false)
    private String email;

    @NotBlank @Column(nullable = false)
    @JsonIgnore                          // FIX 1: never send the BCrypt hash to the browser
    private String password;

    private String phone;
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;

    @Column(nullable = false)
    private boolean active = true;

    @Column(name = "member_id_number")
    private String memberIdNumber;

    @Column(name = "nic_front_path")
    private String nicFrontPath;

    @Column(name = "nic_back_path")
    private String nicBackPath;

    @Column(name = "id_verified")
    private boolean idVerified = false;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore                          // FIX 2: breaks Member→BorrowRecord→Member infinite recursion
    private List<BorrowRecord> borrowRecords;

    public Member() {}

    @PrePersist  protected void onCreate() { createdAt = updatedAt = LocalDateTime.now(); }
    @PreUpdate   protected void onUpdate() { updatedAt = LocalDateTime.now(); }

    public Long getId()               { return id; }
    public String getName()           { return name; }
    public String getEmail()          { return email; }
    public String getPassword()       { return password; }
    public String getPhone()          { return phone; }
    public String getAddress()        { return address; }
    public Role getRole()             { return role; }
    public boolean isActive()         { return active; }
    public String getMemberIdNumber() { return memberIdNumber; }
    public String getNicFrontPath()   { return nicFrontPath; }
    public String getNicBackPath()    { return nicBackPath; }
    public boolean isIdVerified()     { return idVerified; }
    public LocalDateTime getCreatedAt(){ return createdAt; }
    public LocalDateTime getUpdatedAt(){ return updatedAt; }
    public List<BorrowRecord> getBorrowRecords() { return borrowRecords; }

    public void setId(Long id)                  { this.id = id; }
    public void setName(String v)               { this.name = v; }
    public void setEmail(String v)              { this.email = v; }
    public void setPassword(String v)           { this.password = v; }
    public void setPhone(String v)              { this.phone = v; }
    public void setAddress(String v)            { this.address = v; }
    public void setRole(Role v)                 { this.role = v; }
    public void setActive(boolean v)            { this.active = v; }
    public void setMemberIdNumber(String v)     { this.memberIdNumber = v; }
    public void setNicFrontPath(String v)       { this.nicFrontPath = v; }
    public void setNicBackPath(String v)        { this.nicBackPath = v; }
    public void setIdVerified(boolean v)        { this.idVerified = v; }
    public void setCreatedAt(LocalDateTime v)   { this.createdAt = v; }
    public void setUpdatedAt(LocalDateTime v)   { this.updatedAt = v; }
    public void setBorrowRecords(List<BorrowRecord> v){ this.borrowRecords = v; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private String name,email,password,phone,address,memberIdNumber;
        private Role role = Role.USER;
        private boolean active=true,idVerified=false;
        public Builder name(String v){this.name=v;return this;}
        public Builder email(String v){this.email=v;return this;}
        public Builder password(String v){this.password=v;return this;}
        public Builder phone(String v){this.phone=v;return this;}
        public Builder address(String v){this.address=v;return this;}
        public Builder role(Role v){this.role=v;return this;}
        public Builder active(boolean v){this.active=v;return this;}
        public Builder memberIdNumber(String v){this.memberIdNumber=v;return this;}
        public Builder idVerified(boolean v){this.idVerified=v;return this;}
        public Member build(){
            Member m=new Member(); m.name=name;m.email=email;m.password=password;
            m.phone=phone;m.address=address;m.role=role;m.active=active;
            m.memberIdNumber=memberIdNumber;m.idVerified=idVerified;return m;
        }
    }
    public enum Role { ADMIN, LIBRARIAN, USER }
}
