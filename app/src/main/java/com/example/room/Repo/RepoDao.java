package com.example.room.Repo;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RepoDao {

    @Query("SELECT * FROM task")
    List<RepoRoom> getAll();

    @Insert
    void insert(RepoRoom repoRoom);

}
