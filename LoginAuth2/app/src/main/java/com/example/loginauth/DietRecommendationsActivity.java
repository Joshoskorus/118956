package com.example.loginauth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.loginauth.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.collect.HashBiMap;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;


public class DietRecommendationsActivity extends AppCompatActivity {

    // Declare UI elements
    EditText allergiesEditText;
    EditText healthGoalsEditText;
    EditText dietaryPreferencesEditText;
    Button submitButton;
    TextView dietRecommendationsText;

    FirebaseFirestore db;




    // Define your OpenAI API key
   // private static final String OPENAI_API_KEY = "ac46f8e8e6a24dd09be028b98c9e5f4b";
    //private static final String OPENAI_API_URL = "https://api.openai.com/v1/completions";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_recommendations);

        db= FirebaseFirestore.getInstance();

        // Initialize UI elements
        allergiesEditText = findViewById(R.id.allergiesEditText);
        healthGoalsEditText = findViewById(R.id.healthGoalsEditText);
        dietaryPreferencesEditText = findViewById(R.id.dietaryPreferencesEditText);
        submitButton = findViewById(R.id.submitButton);
        dietRecommendationsText = findViewById(R.id.dietRecommendationsText);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input from each field
                String allergiesInput = allergiesEditText.getText().toString();
                String healthGoalsInput = healthGoalsEditText.getText().toString();
                String dietaryPreferencesInput = dietaryPreferencesEditText.getText().toString();
                String question= "Allergies: " + allergiesInput + "\nHealth Goals: " + healthGoalsInput + "\nDietary Preferences: " + dietaryPreferencesInput +
                        "\nRecommend a very short and brief  suitable diet for me in point form less than 5 lines .";


                sendRequest(question);

                // Call the OpenAI API to generate diet recommendations

            }
        });

        //  button to navigate to PreviousResponsesActivity
        // button to navigate to PreviousResponsesActivity
        Button viewPreviousResponsesButton = findViewById(R.id.viewPreviousResponsesButton);
        viewPreviousResponsesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the method to fetch and display previous responses
                fetchPreviousResponses();

                // Create an Intent to start PreviousResponsesActivity
                Intent intent = new Intent(DietRecommendationsActivity.this, PreviousResponsesActivity.class);
                startActivity(intent);
            }
        });

        Button exportPdfButton = findViewById(R.id.exportPdfButton);
        exportPdfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    exportToPdf();
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });



    }
    public void sendRequest(String request){

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.openai.com/v1/completions";

        JSONObject jsonObject =new JSONObject();
        try {
            jsonObject.put("model", "gpt-3.5-turbo-instruct-0914");
            jsonObject.put("prompt", request);
            jsonObject.put("max_tokens",  800);




// Request a string response from the provided URL.
       JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String answer = null;
                        try {
                            answer = response.getJSONArray("choices").getJSONObject(0).getString("text");

                            //Save the result to Firestore
                            saveResultToFirestore(answer);

                            // Display the first 500 characters of the response string.
                            dietRecommendationsText.setText(answer);

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dietRecommendationsText.setText("That didn't work!");
            }

        }){
           @Override
           public Map<String, String> getHeaders() throws AuthFailureError {
               HashMap<String,String> map =new HashMap<>();
               map.put("Content-Type", "application/json");
               map.put("Authorization", "Bearer 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 ");

               return map;
           }
       };

       jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
           @Override
           public int getCurrentTimeout() {
               return 60000;
           }

           @Override
           public int getCurrentRetryCount() {
               return 6;
           }

           @Override
           public void retry(VolleyError error) throws VolleyError {

           }
       });

/// Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }



    }

    private void exportToPdf() throws FileNotFoundException {
        // Get the diet recommendations text
        String dietRecommendations = dietRecommendationsText.getText().toString();

        // Create a PDF document
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter("diet_recommendations.pdf"));
        Document document = new Document(pdfDocument);

        // Add the diet recommendations text to the PDF
        document.add(new Paragraph("Diet Recommendations:\n\n" + dietRecommendations));

        // Close the document
        document.close();

        // Inform the user that the PDF has been exported
        Toast.makeText(this, "Diet recommendations exported to PDF", Toast.LENGTH_SHORT).show();
    }
    private void saveResultToFirestore(String result) {
        // Create a map to store the result
        Map<String, Object> resultData = new HashMap<>();
        resultData.put("result", result);

        // Save the result to Firestore under "dietRecommendations/recommendations" document
        db.collection("Diet")
                .document("Recommendations")
                .set(resultData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Successfully saved to Firestore
                        // You can add any additional logic or feedback here
                        Toast.makeText(DietRecommendationsActivity.this, "Result saved to Firestore", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        // Failed to save to Firestore
                        // You can add any error handling logic here
                        Toast.makeText(DietRecommendationsActivity.this, "Failed to save result to Firestore", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void fetchPreviousResponses() {
        // Fetch previous responses from Firestore and display them
        db.collection("Diet")
                .document("Recommendations")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String previousResponse = documentSnapshot.getString("result");
                            // Display previous response in dietRecommendationsText
                            dietRecommendationsText.setText(previousResponse);
                        } else {
                            // No previous response found
                            // Handle this case, for example, by displaying a message
                            dietRecommendationsText.setText("No previous response found.");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        // Handle failure to fetch previous responses
                        Toast.makeText(DietRecommendationsActivity.this, "Failed to fetch previous response from Firestore", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    }







    // Function to generate diet recommendations using OpenAI and Volley
   // private void generateDietRecommendations(String allergies, String healthGoals, String dietaryPreferences) {

       // RequestQueue queue = Volley.newRequestQueue(this);
       // String url="https://api.openai.com/v1/completions";

       // JSONObject jsonObject = new JSONObject();
       // try {
          //  jsonObject.put("model", "gpt-3.5-turbo-instruct-0914");
          //  jsonObject.put("prompt", createMessages(allergies, healthGoals, dietaryPreferences));
           // jsonObject.put("max_tokens", 200);


           // JsonObjectRequest request = new JsonObjectRequest(
                  //  Request.Method.POST,
                  //  url,
                   // jsonObject,
                  //  new Response.Listener<JSONObject>() {
                     //   @Override
                      //  public void onResponse(JSONObject response) {
                         //   try {
                                // Extract the AI-generated response
                              //  JSONArray choices = response.getJSONArray("choices");
                              //  JSONObject choice = choices.getJSONObject(0);
                               // String aiResponse = choice.getString("text");

                                // Display the recommendations in the dietRecommendationsText
                               // dietRecommendationsText.setText(aiResponse);
                           // } catch (JSONException e) {
                              //  e.printStackTrace();
                          //  }
                      //  }
                   // },
                  //  new Response.ErrorListener() {
                     //   @Override
                      //  public void onErrorResponse(VolleyError error) {
                            // Handle API request error
                         //   dietRecommendationsText.setText("Error: " + error.toString());
                       // }
                   // }) {
               // @Override
               // public Map<String, String> getHeaders() throws AuthFailureError {
                  //  HashMap<String, String> map = new HashMap<>();
                  //  map.put("Content-Type", "application/json");
                   // map.put("Authorization", "Bearer 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");
                   // return map;
              //  }
           // };






            // Add the request to the Volley request queue
           // Volley.newRequestQueue(this).add(request);

       // } catch (JSONException e) {
          //  e.printStackTrace();
       // }
  //  }

    // Create an array of messages for the API call
   // private JSONArray createMessages(String allergies, String healthGoals, String dietaryPreferences) {
       // JSONArray messages = new JSONArray();
      //  messages.put(createMessage("system", "You are a helpful diet recommender."));
       // messages.put(createMessage("user", "I have allergies to " + allergies +
            //    ". My health goal is " + healthGoals + ". My dietary preference is " + dietaryPreferences));
       // messages.put(createMessage("system", "Based on your input, here are some diet recommendations for you:")); // Added for clarity

       // return messages;
   // }

    // Create a JSON object for a message
   // private JSONObject createMessage(String role, String content) {
       // JSONObject message = new JSONObject();
       // try {
        //    message.put("role", role);
        //    message.put("content", content);
       // } catch (JSONException e) {
          //  e.printStackTrace();
       // }
       // return message;
   // }

