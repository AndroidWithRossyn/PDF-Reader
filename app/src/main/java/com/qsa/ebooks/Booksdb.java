package com.qsa.ebooks;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Booksdb {

    @NonNull
    @PrimaryKey
    String id;

    @ColumnInfo(name = "bookName_db")
    String bookName;

    @ColumnInfo(name = "authorName_db")
    String authorName;

    @ColumnInfo(name = "bookImage_db")
    String bookImage;

    @ColumnInfo(name = "pdfUrl_db")
    String pdfUrl;

    @ColumnInfo(name = "categoryName_db")
    String categoryName;

    @ColumnInfo(name = "ratingBar_db")
    String ratingBar;

    @ColumnInfo(name = "description_db")
    String description;

    @ColumnInfo(name = "liked_db")
    String liked;

    @ColumnInfo(name = "shared_db")
    String shared;

    @ColumnInfo(name = "readers_db")
    String readers;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getBookImage() {
        return bookImage;
    }

    public void setBookImage(String bookImage) {
        this.bookImage = bookImage;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getRatingBar() {
        return ratingBar;
    }

    public void setRatingBar(String ratingBar) {
        this.ratingBar = ratingBar;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLiked() {
        return liked;
    }

    public void setLiked(String liked) {
        this.liked = liked;
    }

    public String getShared() {
        return shared;
    }

    public void setShared(String shared) {
        this.shared = shared;
    }

    public String getReaders() {
        return readers;
    }

    public void setReaders(String readers) {
        this.readers = readers;
    }
}
