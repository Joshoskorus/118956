package com.example.loginauth;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.loginauth.DietRecommender;
import com.example.loginauth.FoodItem;
import com.example.loginauth.R;

import java.util.ArrayList;
import java.util.List;

public class DietInputActivity extends AppCompatActivity {

    private EditText editDietaryPreferences;
    private EditText editHealthGoals;
    private EditText editFoodRestrictions;
    private Button getRecommendationsButton;
    private ListView recommendationsListView;
    private TextView recommendationsTextView; // Add this

    private DietRecommender dietRecommender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_input);

        // Initialize UI elements
        editDietaryPreferences = findViewById(R.id.editDietaryPreferences);
        editHealthGoals = findViewById(R.id.editHealthGoals);
        editFoodRestrictions = findViewById(R.id.editFoodRestrictions);
        getRecommendationsButton = findViewById(R.id.buttonGetRecommendations);
        recommendationsListView = findViewById(R.id.recommendationsListView);
        recommendationsTextView = findViewById(R.id.recommendationsTextView); // Initialize the TextView

        // Create a list to hold your food database (replace this with your actual food data)
        List<FoodItem> foodDatabase = new ArrayList<>();
        // ... Add food items to the database ...

        // Initialize your DietRecommender with the food database
        dietRecommender = new DietRecommender(foodDatabase);

        getRecommendationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get user input
                String dietaryPreferences = editDietaryPreferences.getText().toString();
                String healthGoals = editHealthGoals.getText().toString();
                String foodRestrictions = editFoodRestrictions.getText().toString();

                // Get diet recommendations
                List<FoodItem> recommendations = dietRecommender.getRecommendations(dietaryPreferences, healthGoals, foodRestrictions);

                // Create an adapter to display recommendations in the ListView
                ArrayAdapter<String> adapter = new ArrayAdapter<>(DietInputActivity.this, android.R.layout.simple_list_item_1);
                for (FoodItem item : recommendations) {
                    adapter.add(item.getName() + " - Calories: " + item.getCalories());
                }

                // Set the adapter for the ListView to display the recommendations
                recommendationsListView.setAdapter(adapter);

                // Display recommendations in the TextView
                StringBuilder recommendationsText = new StringBuilder("Diet Recommendations:\n");
                for (FoodItem item : recommendations) {
                    recommendationsText.append(item.getName()).append(" - Calories: ").append(item.getCalories()).append("\n");
                }
                recommendationsTextView.setText(recommendationsText.toString());
            }
        });
    }
}
