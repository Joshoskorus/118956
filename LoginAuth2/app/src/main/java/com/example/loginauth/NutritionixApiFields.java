package com.example.loginauth;

public class NutritionixApiFields {

    private String item_name;
    private int nf_calories;
    private double nf_protein;
    private double nf_carbohydrates;
    private double nf_fat;

    public String getItemName() {
        return item_name;
    }

    public void setItemName(String item_name) {
        this.item_name = item_name;
    }

    public int getCalories() {
        return nf_calories;
    }

    public void setCalories(int nf_calories) {
        this.nf_calories = nf_calories;
    }

    public double getProtein() {
        return nf_protein;
    }

    public void setProtein(double nf_protein) {
        this.nf_protein = nf_protein;
    }

    public double getCarbohydrates() {
        return nf_carbohydrates;
    }

    public void setCarbohydrates(double nf_carbohydrates) {
        this.nf_carbohydrates = nf_carbohydrates;
    }

    public double getFat() {
        return nf_fat;
    }

    public void setFat(double nf_fat) {
        this.nf_fat = nf_fat;
    }
}
