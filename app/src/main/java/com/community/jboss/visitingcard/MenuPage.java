package com.community.jboss.visitingcard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MenuPage extends AppCompatActivity {
    public void goToList(View view) {
        Intent toList = new Intent(getApplicationContext(), ContributorList.class);
        startActivity(toList);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_page);
    }
}
