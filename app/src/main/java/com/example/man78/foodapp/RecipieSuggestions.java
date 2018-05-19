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
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static java.sql.DriverManager.getConnection;

public class RecipieSuggestions extends AppCompatActivity implements View.OnClickListener{

    private static final String connection_url= "jdbc:mysql://frankencluster.com:3306/mdevteam7";
    private static final String user = "mdevteam7admin";
    private static final String pass = "jwH2ny7b.8s";

    ListView recipiesList;
    TextView textview;
    SharedPreferences sharedpreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipie_suggestions);
        findViewById(R.id.homeButton).setOnClickListener(this);
        findViewById(R.id.Cancel).setOnClickListener(this);

        recipiesList = findViewById(R.id.recipiesList);
        textview = findViewById(R.id.recipeTitle);

        sharedpreference = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        new getRecipies().execute();
    }



    private class  getRecipies extends AsyncTask<Void, String, ArrayList<String>> {

        private String queryResult;
        ArrayList<String> ingredients = new ArrayList<String>();

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            String queryResult;

            try{
                queryResult = "DB CONN SUCCESS";
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = getConnection(connection_url,user, pass);
                String queryString = "select food_name from food";
                Statement state = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_UPDATABLE);
                final ResultSet result = state.executeQuery(queryString);
                //result.last();
                while(result.next()){
                    ingredients.add(result.getString(1));
                }
                conn.close();
            }catch (Exception e){
                e.printStackTrace();
                queryResult = e.toString();
            }
            //textView.setText(queryResult);
            return ingredients;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result)
        {
            super.onPostExecute(result);
            //textview.setText(ingredients.toString());
            final ArrayList<String> names = new ArrayList<String>();
            final ArrayList<String> steps = new ArrayList<String>();
            //
            AsyncHttpClient client = new AsyncHttpClient();
            final String request_url = "http://food2fork.com/api/search?key=b3263b56c9f6f7026a3de9cf752599fc";
            client.get(request_url, new AsyncHttpResponseHandler() {
                //private String queryResult;

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                     //textview.setText(responseBody.toString());
                    //String recipies;
                    JSONObject recipiesList = new JSONObject();
                    if (responseBody != null){
                        String response = new String(responseBody);
                        try {
                            JSONObject responseArray = new JSONObject(response);
                            Integer recipies_num = responseArray.getInt("count");
                            JSONArray recipies = new JSONArray(responseArray.getString("recipes"));

                            for (int i=0; i< recipies_num; i++){
                               // titles.add(recip.getString("title"));
                                String name = recipies.getJSONObject(i).getString("title");
                                String directions = recipies.getJSONObject(i).getString("source_url");
                                recipiesList.put(name,directions);
                                names.add(name);
                                steps.add(directions);
                            }

                            //textview.setText(recipiesList.toString());
                            //textview.setText(names.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    String response = new String(responseBody);
                    textview.setText(response);
                    //    v.setEnabled(true);
                }
            });

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    RecipieSuggestions.this,
                    android.R.layout.simple_list_item_1,
                    names
            );

            recipiesList.setAdapter(arrayAdapter);
            //recipiesList.setAdapter(arrayAdapter2);
            recipiesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick (AdapterView < ? > adapter, View v,int position, long id){
                    String s = recipiesList.getItemAtPosition(position).toString();
                    startActivity(new Intent(getApplicationContext(), ViewRecipie.class));

                    SharedPreferences.Editor editor = sharedpreference.edit();
                    editor.putString("recipeName",s );
                    editor.putString("recipeDirections",steps.toString());
                    editor.putString("recipePosition", Integer.toString(position) );
                    editor.apply();

                    //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                }
            });
        }

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
        }
    }
}
