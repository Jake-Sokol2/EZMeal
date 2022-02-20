package com.example.ezmeal;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TestDao {
    @Insert
    //users is some kind of keyword for the DAO, its not a table name or something like that
    void insertAll(TestEntity... users);

    @Delete
    void delete(TestEntity user);

    @Query("SELECT * FROM usersTable")
    List<TestEntity> getAll();
}