package com.example.room.Repo;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {RepoRoom.class} ,  version = 1)

public abstract class RepoDatabase extends RoomDatabase {
    public abstract RepoDao repoDao();

}
