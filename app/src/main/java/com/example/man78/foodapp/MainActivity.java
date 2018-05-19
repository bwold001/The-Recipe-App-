package com.example.man78.foodapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import static java.sql.DriverManager.getConnection;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String connection_url= "jdbc:mysql://frankencluster.com:3306/mdevteam7";
    private static final String user = "mdevteam7admin";
    private static final String pass = "jwH2ny7b.8s";

    EditText Email;
    EditText password;
    TextView statusMessage;
    SharedPreferences sharedpreferences;

    public static final String UserID = "UserID";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.SignInButton).setOnClickListener(this);
        findViewById(R.id.SignupButton).setOnClickListener(this);

        Email =  findViewById(R.id.email);
        password =  findViewById(R.id.password);
        statusMessage = findViewById(R.id.status);

        sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
    }

    @Override
    public void onClick (View view){
        switch(view.getId()){
            case R.id.SignInButton:
                String email = Email.getText().toString();
                String pass = password.getText().toString();
                new signIn(email, pass).execute();


            case R.id.SignupButton:
                startActivity(new Intent(this, SignUp.class));
                break;
        }
    }

    private class  signIn extends AsyncTask<String, Void, Void> {

        private String queryResult;

        String email;
        String passwrd;

        signIn (String email, String passwrd) {
            // list all the parameters like in normal class define
            this.email = email;
            this.passwrd = passwrd;
        }

        @Override
        protected Void doInBackground(String... strings) {
            try{
                queryResult = "DB CONN SUCCESS";
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = getConnection(connection_url,user, pass);
                PreparedStatement queryString = conn.prepareStatement("select * from users where email= ? ");
                queryString.setString(1,email);
                //Statement state = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                 //       ResultSet.CONCUR_UPDATABLE);
                final ResultSet result = queryString.executeQuery();
                result.last();

                String corr_pass = result.getString("password");
                String user_id = result.getString("user_id");
                String user_name = result.getString("first_name");

                //queryResult = "SUCCESS";

                if  (passwrd.equals(corr_pass)){
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("UserID",user_id );
                    editor.putString("UserName",user_name);
                    editor.apply();

                    Intent intent = new Intent(getApplicationContext(), Home.class);
                    startActivity(intent);
                }
                else{
                    statusMessage.setText("Incorrect Password! Please try again.");
                }

                conn.close();
            }catch (Exception e){
                e.printStackTrace();
                queryResult = e.toString();
            }
            statusMessage.setText(queryResult);
            return null;
        }

    }



}


