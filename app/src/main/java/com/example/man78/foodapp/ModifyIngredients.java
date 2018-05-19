package com.example.man78.foodapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import static java.sql.DriverManager.getConnection;

public class ModifyIngredients extends AppCompatActivity implements View.OnClickListener{

    private static final String connection_url= "jdbc:mysql://frankencluster.com:3306/mdevteam7";
    private static final String user = "mdevteam7admin";
    private static final String pass = "jwH2ny7b.8s";

    ListView pantryList;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_ingredients);
        findViewById(R.id.addItem).setOnClickListener(this);

        findViewById(R.id.home).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);

        pantryList = findViewById(R.id.pantryListView);
        textView = findViewById(R.id.recipeTitle);

        new getFood().execute();
    }



    private class  getFood extends AsyncTask<Void, String, ArrayList<String>> {

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
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    ModifyIngredients.this,
                    android.R.layout.simple_list_item_1,
                    ingredients
            );
            pantryList.setAdapter(arrayAdapter);

            pantryList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view,
                                               int position, long id) {
                    // TODO Auto-generated method stub

                    ingredients.remove(position);

                    arrayAdapter.notifyDataSetChanged();

                    Toast.makeText(getApplicationContext(), "Item Deleted", Toast.LENGTH_LONG).show();

                    return true;
                }

            });
        }

    }



    @Override
    public void onClick (View view){
        switch(view.getId()){
            case R.id.addItem:
                startActivity(new Intent(this, ItemScan.class ));
                break;

            case R.id.home:
                startActivity(new Intent(this, Home.class ));
                break;
            case R.id.back:
                startActivity(new Intent(this, Home.class ));
                break;
        }
    }
}
