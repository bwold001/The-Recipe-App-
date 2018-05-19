package com.example.man78.foodapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ModifyAndAddToPantry extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_scan);
        findViewById(R.id.homeButton).setOnClickListener(this);
        findViewById(R.id.Cancel).setOnClickListener(this);
        findViewById(R.id.addRecipie).setOnClickListener(this);
    }
    @Override
    public void onClick (View view) {
        switch (view.getId()) {
            case R.id.homeButton:
                startActivity(new Intent(this, Home.class ));
                break;
            case R.id.Cancel:
                startActivity(new Intent(this, ModifyIngredients.class));
                break;
            case R.id.addRecipie:
                startActivity(new Intent(this, ModifyAndAddToPantry.class));
                break;

            case R.id.useCamera:
                //startActivity(new Intent(this, ItemScan.class));
                Intent i = new Intent(Intent.ACTION_MAIN);
                PackageManager managerclock = getPackageManager();
                i = managerclock.getLaunchIntentForPackage("com.google.android.gms.samples.vision.barcodereader");
                i.addCategory(Intent.CATEGORY_LAUNCHER);
                startActivity(i);
                break;
        }
    }
}
