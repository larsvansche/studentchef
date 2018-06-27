package nl.model;

import java.util.ArrayList;

public class Recipe {
    private int id;
    private String title;
    private int preptime;
    private String description;
    private String guideJSON;
    private ArrayList<Rating> ratings;

    public Recipe(int id, String title, int preptime, String description, String guideJSON, ArrayList<Rating> ratings) {
        this.id = id;
        this.title = title;
        this.preptime = preptime;
        this.description = description;
        this.guideJSON = guideJSON;
        this.ratings = ratings;
    }

//    public Recipe(String title, int preptime, String description, String guideJSON, ArrayList<Rating> ratings) {
//        this.title = title;
//        this.preptime = preptime;
//        this.description = description;
//        this.guideJSON = guideJSON;
//        this.ratings = ratings;
//    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPreptime() {
        return preptime;
    }

    public double getAverageRating() {
        if (this.ratings.size() == 0) {
            return -1;
        }

        double totalValue = 0;
        for (Rating rating : this.ratings) {
            totalValue += rating.getValue();
        }

        System.out.println("Average rating of this recipe: " + totalValue / ratings.size());
        return totalValue / ratings.size();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGuideJSON() {
        return guideJSON;
    }

    public void setGuideJSON(String guideJSON) {
        this.guideJSON = guideJSON;
    }
}
