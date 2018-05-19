package com.example.man78.foodapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static java.sql.DriverManager.getConnection;

public class AddRecipie extends AppCompatActivity implements View.OnClickListener{

    private static final String connection_url= "jdbc:mysql://frankencluster.com:3306/mdevteam7";
    private static final String user = "mdevteam7admin";
    private static final String pass = "jwH2ny7b.8s";

    EditText recipeName;
    EditText directions;
    EditText ingredients;
    TextView status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipie);
        findViewById(R.id.homeButton).setOnClickListener(this);
        findViewById(R.id.Cancel).setOnClickListener(this);
        findViewById(R.id.addRecipie).setOnClickListener(this);

        recipeName = findViewById(R.id.recipeName);
        directions = findViewById(R.id.directions);
        ingredients = findViewById(R.id.ingredientsRequired);
        status = findViewById(R.id.statusMess);

    }
    @Override
    public void onClick (View view){
        switch(view.getId()){
            case R.id.homeButton:
                startActivity(new Intent(this, Home.class ));
                break;
            case R.id.Cancel:
                startActivity(new Intent(this, SearchAndAddRecipie.class ));
                break;
            case R.id.addRecipie:
                String rname = recipeName.getText().toString();
                String direct = directions.getText().toString();
                String ingred = ingredients.getText().toString();
                new addRecipe(rname, ingred,direct).execute();
                status.setText("Recipe Successfully Added!");
                status.setVisibility(View.VISIBLE);

                break;
        }
    }


    private class addRecipe extends AsyncTask<String, Void, Void> {

        private String queryResult;

        String  rName;
        String ingred;
        String direct;

        addRecipe (String rName, String ingred, String direct) {
            // list all the parameters like in normal class define
            this.rName = rName;
            this.ingred = ingred;
            this.direct = direct;
        }


        @Override
        protected Void doInBackground(String... strings) {
            try{
                queryResult = "DB CONN SUCCESS";
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = getConnection(connection_url,user, pass);
                String queryString = "select * from recipes";
                Statement state = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_UPDATABLE);
                final ResultSet result = state.executeQuery(queryString);
                result.moveToInsertRow();
                result.updateString("recipe_food_name", rName);
                result.updateString("recipe_ingredients", ingred);
                result.updateString("recipe_directions", direct);
                result.insertRow();
                result.moveToCurrentRow();
                conn.close();
            }catch (Exception e){
                e.printStackTrace();
                queryResult = "Recipe Not Added "+ e.toString();
            }

            return null;
        }

    }
}
