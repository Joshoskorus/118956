package com.example.loginauth;

import com.example.loginauth.FoodItem;
import com.example.loginauth.NutritionixApiItem;
import com.example.loginauth.NutritionixApiResponse;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NutritionixApiFetcher {
    private static final String API_BASE_URL = "https://api.nutritionix.com/v1_1/search/";
    private static final String API_KEY = "fdeb31c46a917e795423931fb8122c3e"; // Replace with your actual API key
    private static final String APP_ID = "39de1fa6"; // Replace with your actual app ID

    public List<FoodItem> fetchFoodItems(String foodQuery) {
        List<FoodItem> foodItems = new ArrayList<>();

        try {
            URL url = new URL(API_BASE_URL + foodQuery + "?results=0:5&fields=item_name,nf_calories,nf_protein,nf_carbohydrates,nf_fat&appId=" + APP_ID + "&appKey=" + API_KEY);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();
                connection.disconnect();

                // Parse the JSON response and create FoodItem objects
                foodItems = parseJsonResponse(response.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return foodItems;
    }

    private List<FoodItem> parseJsonResponse(String jsonResponse) {
        List<FoodItem> foodItems = new ArrayList<>();

        // Use a JSON parsing library like Gson to parse the JSON response
        Gson gson = new Gson();
        NutritionixApiResponse apiResponse = gson.fromJson(jsonResponse, NutritionixApiResponse.class);

        // Extract and transform data from the API response into FoodItem objects
        for (NutritionixApiItem item : apiResponse.getHits()) {
            FoodItem foodItem = new FoodItem(
                    item.getFields().getItemName(),
                    item.getFields().getCalories(),
                    item.getFields().getProtein(),
                    item.getFields().getCarbohydrates(),
                    item.getFields().getFat()
            );
            foodItems.add(foodItem);
        }

        return foodItems;
    }
}
