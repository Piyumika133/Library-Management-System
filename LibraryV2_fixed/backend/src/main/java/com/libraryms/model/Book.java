package com.libraryms.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity @Table(name = "book")
public class Book {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @NotBlank @Column(nullable = false) private String title;
    @NotBlank @Column(nullable = false) private String author;
    @NotBlank @Column(unique = true, nullable = false) private String isbn;
    private String category;
    private String description;
    @Min(0) @Column(nullable = false) private int quantity;
    @Column(name = "available_quantity", nullable = false) private int availableQuantity;
    @Column(name = "published_year") private Integer publishedYear;
    @Column(name = "cover_image_url") private String coverImageUrl;
    @Column(name = "barcode_value", unique = true) private String barcodeValue;
    @Column(name = "created_at", updatable = false) private LocalDateTime createdAt;
    @Column(name = "updated_at") private LocalDateTime updatedAt;
    public Book() {}
    @PrePersist protected void onCreate() {
        createdAt = updatedAt = LocalDateTime.now();
        if (availableQuantity == 0 && quantity > 0) availableQuantity = quantity;
    }
    @PreUpdate protected void onUpdate() { updatedAt = LocalDateTime.now(); }
    public Long getId(){return id;} public String getTitle(){return title;} public String getAuthor(){return author;}
    public String getIsbn(){return isbn;} public String getCategory(){return category;} public String getDescription(){return description;}
    public int getQuantity(){return quantity;} public int getAvailableQuantity(){return availableQuantity;}
    public Integer getPublishedYear(){return publishedYear;} public String getCoverImageUrl(){return coverImageUrl;}
    public String getBarcodeValue(){return barcodeValue;}
    public LocalDateTime getCreatedAt(){return createdAt;} public LocalDateTime getUpdatedAt(){return updatedAt;}
    public void setId(Long v){id=v;} public void setTitle(String v){title=v;} public void setAuthor(String v){author=v;}
    public void setIsbn(String v){isbn=v;} public void setCategory(String v){category=v;} public void setDescription(String v){description=v;}
    public void setQuantity(int v){quantity=v;} public void setAvailableQuantity(int v){availableQuantity=v;}
    public void setPublishedYear(Integer v){publishedYear=v;} public void setCoverImageUrl(String v){coverImageUrl=v;}
    public void setBarcodeValue(String v){barcodeValue=v;}
    public void setCreatedAt(LocalDateTime v){createdAt=v;} public void setUpdatedAt(LocalDateTime v){updatedAt=v;}
    public static Builder builder(){return new Builder();}
    public static class Builder {
        private String title,author,isbn,category,description,coverImageUrl,barcodeValue;
        private int quantity,availableQuantity; private Integer publishedYear;
        public Builder title(String v){title=v;return this;} public Builder author(String v){author=v;return this;}
        public Builder isbn(String v){isbn=v;return this;} public Builder category(String v){category=v;return this;}
        public Builder description(String v){description=v;return this;} public Builder quantity(int v){quantity=v;return this;}
        public Builder availableQuantity(int v){availableQuantity=v;return this;} public Builder publishedYear(Integer v){publishedYear=v;return this;}
        public Builder coverImageUrl(String v){coverImageUrl=v;return this;} public Builder barcodeValue(String v){barcodeValue=v;return this;}
        public Book build(){Book b=new Book();b.title=title;b.author=author;b.isbn=isbn;b.category=category;
            b.description=description;b.quantity=quantity;b.availableQuantity=availableQuantity;
            b.publishedYear=publishedYear;b.coverImageUrl=coverImageUrl;b.barcodeValue=barcodeValue;return b;}
    }
}
