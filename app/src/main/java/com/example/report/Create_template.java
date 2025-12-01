package com.example.report;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Create_template extends AppCompatActivity {

    private Button btntemplate_type, btnadd_recipients, btnauto_fill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_template);

        initView();

        btntemplate_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Create_template.this, Template_types.class);
                startActivity(intent);
            }
        });
        btnadd_recipients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Create_template.this, Recipients.class);
                startActivity(intent);
            }
        });
        btnauto_fill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Create_template.this, Template_settings.class);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        btntemplate_type = findViewById(R.id.template_types);
        btnadd_recipients = findViewById(R.id.recipients);
        btnauto_fill = findViewById(R.id.auto_fill);
    }
}