package com.example.report;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

public class Template_types extends AppCompatActivity {

    private RadioButton btnincident_report, btnquick_note;
    private TemplateViewModel templateViewModel;
    private Template templaite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template_types);

        initView();
        templateViewModel = new ViewModelProvider(Template_types.this).get(TemplateViewModel.class);
        templateViewModel.getCurrent();
        templateViewModel.getSelected().observe(Template_types.this, new Observer<Template>() {
            @Override
            public void onChanged(Template template) {
                templaite = template;
                btnincident_report.setChecked(template.getType().equals(
                        "incidence"));
                btnquick_note.setChecked(template.getType().equals("empty"));
            }
        });
        btnincident_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:change the string bellow to global constant
                templaite.setType(String.valueOf("incidence"));
                templateViewModel.update(templaite);
            }
        });
        btnquick_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                templaite.setType("empty");
                templateViewModel.update(templaite);
            }
        });
    }

    private void initView() {
        btnincident_report = findViewById(R.id.incident_report);
        btnquick_note = findViewById(R.id.empty);
    }
}