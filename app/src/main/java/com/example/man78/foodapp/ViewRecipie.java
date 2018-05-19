package com.example.man78.foodapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ViewRecipie extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences sharedpreference;
    TextView recipeTitle;
    TextView recipeInstructions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipie);
        findViewById(R.id.homeButton).setOnClickListener(this);
        findViewById(R.id.Cancel).setOnClickListener(this);

        recipeTitle = findViewById(R.id. recipeTitle);
        recipeInstructions = findViewById(R.id.instructions);

        sharedpreference = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String recipeName = sharedpreference.getString("recipeName", null);
        String recipeSteps = sharedpreference.getString("recipeDirections", null);
        String position = sharedpreference.getString("recipePosition", null);
        int pos = Integer.parseInt(position);
        String[] steps = recipeSteps.split(",");

        recipeTitle.setText(recipeName);
        recipeInstructions.setText(steps[pos]);
        recipeInstructions.setMovementMethod(LinkMovementMethod.getInstance());

    }
    @Override
    public void onClick (View view){
        switch(view.getId()){
            case R.id.homeButton:
                startActivity(new Intent(this, Home.class ));
                break;
            case R.id.Cancel:
                startActivity(new Intent(this, RecipieSuggestions.class));
                break;
        }
    }
}
