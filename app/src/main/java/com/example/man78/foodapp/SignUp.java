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

import static java.sql.DriverManager.*;

public class SignUp extends AppCompatActivity  implements View.OnClickListener{

    private static final String connection_url= "jdbc:mysql://frankencluster.com:3306/mdevteam7";
    private static final String user = "mdevteam7admin";
    private static final String pass = "jwH2ny7b.8s";

    EditText firstName;
    EditText lastName;
    EditText Email;
    EditText password;
    EditText confirmPassword;
    TextView statusMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        findViewById(R.id.SignupButton).setOnClickListener(this);
        findViewById(R.id.Cancel).setOnClickListener(this);


        firstName = findViewById(R.id.FirstName);
        lastName = findViewById(R.id.LastName);
        Email =  findViewById(R.id.Email);
        password =  findViewById(R.id.Password);
        confirmPassword =  findViewById(R.id.ConfirmPassword);
        statusMessage = findViewById(R.id.status);

    }
    @Override
    public void onClick (View view){
        switch(view.getId()){
            case R.id.SignupButton:
                String fName = firstName.getText().toString();
                String lName = lastName.getText().toString();
                String email = Email.getText().toString();
                String pass = password.getText().toString();
                String confirmpass = confirmPassword.getText().toString();

                if (pass.equals(confirmpass)) {
                    new insertUser(fName, lName, email, pass).execute();
                    statusMessage.setText("Registered Successfully. Please go back and sign in");
                }
                else
                    statusMessage.setText("Passwords don't match");

                break;
            case R.id.Cancel:
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }


    private class insertUser extends AsyncTask <String, Void, Void>{

        private String queryResult;

        String  fName;
        String lName;
        String email;
        String passwrd;

        insertUser (String fName, String lName, String email, String passwrd) {
            // list all the parameters like in normal class define
            this.fName = fName;
            this.lName = lName;
            this.email = email;
            this.passwrd = passwrd;
        }


        @Override
        protected Void doInBackground(String... strings) {
            try{
                queryResult = "DB CONN SUCCESS";
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = getConnection(connection_url,user, pass);
                String queryString = "select * from users";
                Statement state = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_UPDATABLE);
                final ResultSet result = state.executeQuery(queryString);
                result.moveToInsertRow();
                result.updateString("first_name", fName);
                result.updateString("last_name", lName);
                result.updateString("email", email);
                result.updateString("password", passwrd);
                result.insertRow();
                result.moveToCurrentRow();
                conn.close();
                //queryResult = "User Added";
                //queryResult= result.toString();
            }catch (Exception e){
                e.printStackTrace();
                queryResult = "User Not Added "+ e.toString();
            }
            statusMessage.setText(queryResult);
            return null;
        }

    }


}
