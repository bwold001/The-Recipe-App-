package com.example.man78.foodapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static java.sql.DriverManager.getConnection;

public class ingredient_manual_entry extends AppCompatActivity  implements View.OnClickListener {

    private static final String connection_url= "jdbc:mysql://frankencluster.com:3306/mdevteam7";
    private static final String user = "mdevteam7admin";
    private static final String pass = "jwH2ny7b.8s";

    EditText itemName;
    EditText itemUPC;
    EditText itemDescription;
    EditText itemBrand;
    TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_manual_entry);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.home).setOnClickListener(this);
        findViewById(R.id.addItem).setOnClickListener(this);

        itemName = findViewById(R.id.itemName);
        itemUPC = findViewById(R.id.itemUPC);
        itemDescription = findViewById(R.id.itemDescription);
        itemBrand = findViewById(R.id.itemBrand);
        status = findViewById(R.id.status);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.back:
                startActivity(new Intent(this, ItemScan.class));
                break;
            case R.id.home:
                startActivity(new Intent(this, Home.class));
                break;
            case R.id.addItem:
                String iName = itemName.getText().toString();
                String iUPC = itemUPC.getText().toString();
                String iDesc = itemDescription.getText().toString();
                String iBrand = itemBrand.getText().toString();
                new insertItem(iName, iUPC, iDesc, iBrand).execute();
                status.setText("Item Successfully Added");
                status.setVisibility(View.VISIBLE);



        }

    }



    private class insertItem extends AsyncTask<String, Void, Void> {

        private String queryResult;

        String iName;
        String iUPC;
        String iDesc;
        String iBrand;

        insertItem (String iName, String iUPC, String iDesc, String iBrand) {
            // list all the parameters like in normal class define
            this.iName = iName;
            this.iUPC = iUPC;
            this.iDesc = iDesc;
            this.iBrand = iBrand;
        }


        @Override
        protected Void doInBackground(String... strings) {
            try{
                queryResult = "DB CONN SUCCESS";
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = getConnection(connection_url,user, pass);
                String queryString = "select * from food";
                Statement state = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_UPDATABLE);
                final ResultSet result = state.executeQuery(queryString);
                result.moveToInsertRow();
                result.updateString("food_name", iName);
                result.updateString("food_upc_code", iUPC);
                result.updateString("food_description", iDesc);
                result.updateString("food_brand", iBrand);
                result.insertRow();
                result.moveToCurrentRow();
                conn.close();
                //queryResult = "Item Successfully Added";

            }catch (Exception e){
                e.printStackTrace();
                queryResult = "Item Not Added "+ e.toString();
            }

            return null;
        }

    }

}
