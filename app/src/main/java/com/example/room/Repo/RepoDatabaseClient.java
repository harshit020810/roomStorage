package com.example.room.Repo;

import android.content.Context;

import androidx.room.Room;

public class RepoDatabaseClient {

    private Context mCtx;
    private static RepoDatabaseClient mInstance;
    private RepoDatabase repoDatabase;

    private RepoDatabaseClient(Context mCtx) {
        this.mCtx = mCtx;
        repoDatabase = Room.databaseBuilder(mCtx, RepoDatabase.class, "Region Details 15").build();
    }

    public static synchronized RepoDatabaseClient getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new RepoDatabaseClient(mCtx);
        }
        return mInstance;
    }

    public RepoDatabase getAppDatabase() {
        return repoDatabase;
    }
}
