package com.libraryms.model;
import jakarta.persistence.*;

@Entity @Table(name = "barcode")
public class BarCode {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false,unique=true) private String code;
    @OneToOne(fetch=FetchType.LAZY) @JoinColumn(name="book_id",nullable=false,unique=true) private Book book;
    @Column(name="qr_data",columnDefinition="TEXT") private String qrData;
    @Column(name="file_path") private String filePath;
    public BarCode(){}
    public Long getId(){return id;} public String getCode(){return code;} public Book getBook(){return book;}
    public String getQrData(){return qrData;} public String getFilePath(){return filePath;}
    public void setId(Long v){id=v;} public void setCode(String v){code=v;} public void setBook(Book v){book=v;}
    public void setQrData(String v){qrData=v;} public void setFilePath(String v){filePath=v;}
    public static Builder builder(){return new Builder();}
    public static class Builder {
        private String code,qrData,filePath; private Book book;
        public Builder code(String v){code=v;return this;} public Builder book(Book v){book=v;return this;}
        public Builder qrData(String v){qrData=v;return this;} public Builder filePath(String v){filePath=v;return this;}
        public BarCode build(){BarCode b=new BarCode();b.code=code;b.book=book;b.qrData=qrData;b.filePath=filePath;return b;}
    }
}
