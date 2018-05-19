package com.example.man78.foodapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import static java.sql.DriverManager.getConnection;

public class Home extends AppCompatActivity implements View.OnClickListener {

    private static final String connection_url= "jdbc:mysql://frankencluster.com:3306/mdevteam7";
    private static final String user = "mdevteam7admin";
    private static final String pass = "jwH2ny7b.8s";


    TextView textView12;
    SharedPreferences sharedpreferences;
    ListView recentList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        findViewById(R.id.updateInfo).setOnClickListener(this);
        findViewById(R.id.IngredientButton).setOnClickListener(this);
        findViewById(R.id.RecipiesButton).setOnClickListener(this);

        sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        //SharedPreferences.Editor editor = sharedpreferences.edit();
        //String user_id = sharedpreferences.getString("UserID", null);
        String user_name = sharedpreferences.getString("UserName", null);

        textView12 = findViewById(R.id.textView12);
        textView12.setText(user_name);
        recentList = findViewById(R.id.recentList);


        new getRecentRecipies().execute();
    }

    private class  getRecentRecipies extends AsyncTask<Void, String, ArrayList<String>> {

        private String queryResult;
        ArrayList<String> recipies = new ArrayList<String>();

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            String queryResult;

            try{
                queryResult = "DB CONN SUCCESS";
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = getConnection(connection_url,user, pass);
                String queryString = "select recipe_food_name from recipes limit 5";
                Statement state = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_UPDATABLE);
                final ResultSet result = state.executeQuery(queryString);
                //result.last();
                while(result.next()){
                    recipies.add(result.getString(1));
                }
                conn.close();
            }catch (Exception e){
                e.printStackTrace();
                queryResult = e.toString();
            }
            //textView.setText(queryResult);
            return recipies;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result)
        {
            super.onPostExecute(result);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    Home.this,
                    android.R.layout.simple_list_item_1,
                    recipies
            );
            recentList.setAdapter(arrayAdapter);
        }

    }


    @Override
    public void onClick (View view){
        switch(view.getId()){
            case R.id.updateInfo:
                startActivity(new Intent(this, UpdateInfo.class ));
                break;
            case R.id.IngredientButton:
                startActivity(new Intent(this, ModifyIngredients.class));
                break;
            case R.id.RecipiesButton:
                startActivity(new Intent(this, SearchAndAddRecipie.class));
                break;
        }
    }
}
