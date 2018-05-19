package com.example.man78.foodapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import static java.sql.DriverManager.getConnection;

public class ViewAddedRecipie extends AppCompatActivity {

    private static final String connection_url= "jdbc:mysql://frankencluster.com:3306/mdevteam7";
    private static final String user = "mdevteam7admin";
    private static final String pass = "jwH2ny7b.8s";

    TextView recipe_name;
    TextView ingredients;
    TextView directions;

    SharedPreferences sharedpreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_added_recipie);

        //findViewById(R.id.back).setOnClickListener((View.OnClickListener) this);
        //findViewById(R.id.home).setOnClickListener((View.OnClickListener) this);

        recipe_name = findViewById(R.id.recipeName);
        ingredients = findViewById(R.id.ingredients);
        directions = findViewById(R.id.directions);

        sharedpreference = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String recipeName = sharedpreference.getString("recipeSelected", null);

        recipe_name.setText(recipeName);
        new getRecipieDetails(recipeName).execute();
    }


    //@Override
    public void onClick (View view){
        switch(view.getId()){
            case R.id.home:
                startActivity(new Intent(this, Home.class));

            case R.id.back:
                startActivity(new Intent(this, SearchAndAddRecipie.class));
                break;
        }
    }

    private class  getRecipieDetails extends AsyncTask<String, Void, Void> {

        private String queryResult;

        String name;


        getRecipieDetails (String name) {
            // list all the parameters like in normal class define
            this.name = name;
        }

        @Override
        protected Void doInBackground(String... strings) {
            try{
                queryResult = "DB CONN SUCCESS";
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = getConnection(connection_url,user, pass);
                PreparedStatement queryString = conn.prepareStatement("select * from recipes where recipe_food_name = ? ");
                queryString.setString(1,name);
                //Statement state = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                //       ResultSet.CONCUR_UPDATABLE);
                final ResultSet result = queryString.executeQuery();
                result.last();

                String ingredientsRequired = result.getString("recipe_ingredients");
                String steps = result.getString("recipe_directions");

                ingredients.setText(ingredientsRequired);
                directions.setText(steps);

                conn.close();
            }catch (Exception e){
                e.printStackTrace();
                queryResult = e.toString();
            }
            //directions.setText(queryResult);
            return null;
        }

    }


}
