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
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import static java.sql.DriverManager.getConnection;

public class SearchAndAddRecipie extends AppCompatActivity implements View.OnClickListener{

    SharedPreferences sharedpreference;

    private static final String connection_url= "jdbc:mysql://frankencluster.com:3306/mdevteam7";
    private static final String user = "mdevteam7admin";
    private static final String pass = "jwH2ny7b.8s";

    ListView recipiesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_and_add_recipie);
        findViewById(R.id.homeButton).setOnClickListener(this);
        findViewById(R.id.Cancel).setOnClickListener(this);
        findViewById(R.id.addRecipie).setOnClickListener(this);
        findViewById(R.id.suggestRecipie).setOnClickListener(this);

        recipiesList = findViewById(R.id.recipiesList);
        sharedpreference = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        new getAvailableRecipies().execute();

    }

    private class  getAvailableRecipies extends AsyncTask<Void, String, ArrayList<String>> {

        private String queryResult;
        ArrayList<String> recipies = new ArrayList<String>();

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            String queryResult;

            try{
                queryResult = "DB CONN SUCCESS";
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = getConnection(connection_url,user, pass);
                String queryString = "select recipe_food_name from recipes";
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
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    SearchAndAddRecipie.this,
                    android.R.layout.simple_list_item_1,
                    recipies
            );
            recipiesList.setAdapter(arrayAdapter);

            recipiesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick (AdapterView < ? > adapter, View v,int position, long id){
                    String s = recipiesList.getItemAtPosition(position).toString();
                    startActivity(new Intent(getApplicationContext(), ViewAddedRecipie.class));

                    SharedPreferences.Editor editor = sharedpreference.edit();
                    editor.putString("recipeSelected",s );
                    editor.apply();

                    //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                }
            });

            recipiesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view,
                                               int position, long id) {
                    // TODO Auto-generated method stub

                    recipies.remove(position);

                    arrayAdapter.notifyDataSetChanged();

                    Toast.makeText(getApplicationContext(), "Recipe Deleted", Toast.LENGTH_LONG).show();

                    return true;
                }

            });
        }

    }

    @Override
    public void onClick (View view) {
        switch (view.getId()) {
            case R.id.homeButton:
                startActivity(new Intent(this, Home.class ));
                break;
            case R.id.Cancel:
                startActivity(new Intent(this, Home.class));
                break;
            case R.id.addRecipie:
                startActivity(new Intent(this, AddRecipie.class));
                break;
            case R.id.suggestRecipie:
                startActivity(new Intent(this, RecipieSuggestions.class));
                break;
        }
    }



}
