package com.example.report;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;

public class Template_settings extends AppCompatActivity {
    private Switch autoFillTitle, use_location, synced_time;
    private TemplateViewModel templateViewModel;
    private Template templaite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template_settings);
        autoFillTitle = findViewById(R.id.autofill_title);
        use_location = findViewById(R.id.use_location);
        synced_time = findViewById(R.id.synced_time);
        templateViewModel = new ViewModelProvider(Template_settings.this).get(TemplateViewModel.class);
        templateViewModel.getCurrent();
        templateViewModel.getSelected().observe(Template_settings.this, new Observer<Template>() {
            @Override
            public void onChanged(Template template) {
                templaite = template;
                autoFillTitle.setChecked(template.getAutofillTitle());
                use_location.setChecked(template.getUserYourlocation());
                synced_time.setChecked(template.getSyncedTime());
            }
        });
        autoFillTitle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                templaite.setAutofillTitle(autoFillTitle.isChecked());
                templateViewModel.update(templaite);
                //templateViewModel.updateAuto(autoFillTitle.isChecked());
            }
        });
        use_location.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                templaite.setUserYourlocation(use_location.isChecked());
                templateViewModel.update(templaite);
            }
        });
        synced_time.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                templaite.setSyncedTime(synced_time.isChecked());
                templateViewModel.update(templaite);
            }
        });
    }
}