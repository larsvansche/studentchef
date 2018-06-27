package model;

import java.util.Date;

public class Rating {
    private int recipeId;
    private int userId;
    private String username;
    private int value;
    private Date datetime;
    private String description;

    public Rating(int recipeId, int userId, int value, Date datetime, String description) {
        this.recipeId = recipeId;
        this.userId = userId;
        this.username = "";
        this.value = value;
        this.datetime = datetime;
        this.description = description;
    }

    public Rating(int recipeId, int userId, String username, int value, Date datetime, String description) {
        this.recipeId = recipeId;
        this.userId = userId;
        this.username = username;
        this.value = value;
        this.datetime = datetime;
        this.description = description;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
