package com.example.room.Repo;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "task")
public class RepoRoom {

    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo(name = "t_name")
    String name;

    @ColumnInfo(name = "t_capital")
    String capital;

    @ColumnInfo(name = "t_flag")
    String flag;

    @ColumnInfo(name = "t_region")
    String region;

    @ColumnInfo(name = "t_subRegion")
    String subregion;

    @ColumnInfo(name = "t_pop")
    int population;

    @ColumnInfo(name = "t_borders")
    String borders;

    @ColumnInfo(name = "t_languages")
    String languages;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getSubRegion() {
        return subregion;
    }

    public void setSubRegion(String subRegion) {
        this.subregion = subRegion;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public String getBorders() {
        return borders;
    }

    public void setBorders(String borders) {
        this.borders = borders;
    }

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }
}
