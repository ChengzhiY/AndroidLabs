package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.linear_layout);
        TextView myTextView = findViewById(R.id.tv);

        Button bt = findViewById(R.id.btClick);
        bt.setOnClickListener(v -> Toast.makeText(MainActivity.this, getResources().getString(R.string.toast_message), Toast.LENGTH_LONG).show());

        CheckBox checkBox = findViewById(R.id.cbCheck);
        checkBox.setOnCheckedChangeListener((cb, isChecked) -> {if(isChecked==true)
                                    Snackbar.make(bt, getResources().getString(R.string.snackbar_message_on), Snackbar.LENGTH_INDEFINITE).setAction( "Undo", click -> checkBox.setChecked(!isChecked)).show();
                                else
                                     Snackbar.make(bt, getResources().getString(R.string.snackbar_message_off), Snackbar.LENGTH_LONG).setAction( "Undo", click -> checkBox.setChecked(!isChecked)).show();});
          }
}