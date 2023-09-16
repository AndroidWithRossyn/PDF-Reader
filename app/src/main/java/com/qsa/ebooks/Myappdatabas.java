package com.qsa.ebooks;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Booksdb.class},version = 1)
public abstract class Myappdatabas extends RoomDatabase {

    public abstract myDao myDao();
}
