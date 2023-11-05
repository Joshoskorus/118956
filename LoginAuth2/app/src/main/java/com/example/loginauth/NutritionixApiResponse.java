package com.example.loginauth;

import java.util.List;

public class NutritionixApiResponse {

    private List<NutritionixApiItem> hits;

    public List<NutritionixApiItem> getHits() {
        return hits;
    }

    public void setHits(List<NutritionixApiItem> hits) {
        this.hits = hits;
    }
}
