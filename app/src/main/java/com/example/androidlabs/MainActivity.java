package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences = null;
    private EditText typeField = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.email);

        sharedPreferences = getSharedPreferences("EmailAddress", Context.MODE_PRIVATE);
        String savedString = sharedPreferences.getString("Email", "");
        typeField = findViewById(R.id.address);
        typeField.setText(savedString);

        Button btLogin = findViewById(R.id.btLogin);
        btLogin.setOnClickListener(bt -> {
            onPause();
            Intent goToProfile = new Intent(MainActivity.this, ProfileActivity.class);
           goToProfile.putExtra("EMAIL", typeField.getText().toString());
           startActivity(goToProfile);

            });

          }

    @Override
    protected void onPause() {
        super.onPause();
        saveSharedPrefs(typeField.getText().toString());
        }

    private void saveSharedPrefs(String s) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Email", s);
        editor.commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}

/* TextView myTextView = findViewById(R.id.tv);

        Button bt = findViewById(R.id.btClick);
        bt.setOnClickListener(v -> Toast.makeText(MainActivity.this, getResources().getString(R.string.toast_message), Toast.LENGTH_LONG).show());

        CheckBox checkBox = findViewById(R.id.cbCheck);
        checkBox.setOnCheckedChangeListener((cb, isChecked) -> {if(isChecked==true)
                                    Snackbar.make(bt, getResources().getString(R.string.snackbar_message_on), Snackbar.LENGTH_INDEFINITE).setAction( "Undo", click -> checkBox.setChecked(!isChecked)).show();
                                else
                                     Snackbar.make(bt, getResources().getString(R.string.snackbar_message_off), Snackbar.LENGTH_LONG).setAction( "Undo", click -> checkBox.setChecked(!isChecked)).show();});
        */