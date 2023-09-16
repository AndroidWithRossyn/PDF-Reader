package com.qsa.ebooks;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface myDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void addBook(Booksdb booksdb);

    @Query("select * from booksdb")
    public List<Booksdb> getBooks();
 }
