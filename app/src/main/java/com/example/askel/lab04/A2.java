package com.example.askel.lab04;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Random;

public class A2 extends AppCompatActivity {

    private EditText etUsr;
    private String usrName;
    private Random random;
    private static final String alphabet = "AIUEOKTNAIUEOKRMNAIUEO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a2);

        Button btn;
        random = new Random();
        String generatedName = getRandomString();
        etUsr = (EditText) findViewById(R.id.et_auth);
        btn = (Button) findViewById(R.id.btn_auth);

        etUsr.setText(generatedName);

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

   private String getRandomString(){
        int len = 20;
        String s ="";
        char[] text = new char[len];



        for(int i = 0; i < len; i++)
            text[i] = alphabet.charAt(random.nextInt(alphabet.length()));

        for(int i = 0; i < text.length; i++)
            s += text[i];
        return s;
    }
}
