package com.example.loginauth;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class DietRecommenderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diet_recommender); // Set the content view to the diet_recommender.xml layout

        // Retrieve user preferences and health goal (You should have a way to get these values)
        boolean isVegetarian = true; // Sample value, you should get it from user input
        boolean isVegan = false; // Sample value, you should get it from user input
        boolean isGlutenFree = true; // Sample value, you should get it from user input
        String healthGoal = "Weight Loss"; // Sample value, you should get it from user input

        // Call the DietRecommender to generate recommendations
        List<String> recommendations = DietRecommender.generateDietRecommendations(
                isVegetarian, isVegan, isGlutenFree, healthGoal);

        // Display the recommendations in the TextView (Assuming you have a TextView in diet_recommender.xml)
        TextView recommendationsTextView = findViewById(R.id.recommendationdTextView);

        if (recommendations != null && !recommendations.isEmpty()) {
            StringBuilder recommendationsText = new StringBuilder();
            for (String recommendation : recommendations) {
                recommendationsText.append(recommendation).append("\n");
            }
            recommendationsTextView.setText(recommendationsText.toString());
        } else {
            recommendationsTextView.setText("No specific recommendations available.");
        }
    }
}
