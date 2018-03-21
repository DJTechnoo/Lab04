package com.example.askel.lab04;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class A2 extends AppCompatActivity {

    private EditText etUsr;
    private Button btn;
    private String usrName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a2);

        etUsr = (EditText) findViewById(R.id.et_auth);
        btn = (Button) findViewById(R.id.btn_auth);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usrName = etUsr.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("USR", usrName);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }
}
