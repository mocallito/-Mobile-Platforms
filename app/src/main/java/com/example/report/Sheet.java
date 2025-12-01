package com.example.report;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Sheet extends AppCompatActivity {

    private FloatingActionButton btnsend;
    private EditText titleField, emailField, breadField;
    private TemplateViewModel templateViewModel;
    public final String SHARED_PREFS = "sharedPrefs";
    public final String TEXT = "text";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private ImageView imageView;
    private String title, bread, email, image;
    private Set temp;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet);

        initView();
/*
        btntemplate_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Create_template.this, Template_types.class);
                startActivity(intent);
            }
        });*/
    }

    private void initView() {
        btnsend = findViewById(R.id.send);

        titleField = findViewById(R.id.emptySheetTitle);
        emailField = findViewById(R.id.emptySheetEmails);
        breadField = findViewById(R.id.emptySheetBread);
        imageView = findViewById(R.id.imageView2);

        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
                loadData();
                mailWithFile(Sheet.this, uri, stringToList(email), title, bread);
            }
        });

        templateViewModel =  new ViewModelProvider(Sheet.this).get(TemplateViewModel.class);
        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        //temp = new HashSet();
        /*
        templateViewModel.getCurrentImage().observe(Sheet.this, new Observer<Draft>() {
            @Override
            public void onChanged(Draft draft) {
                if (draft != null) {
                    uri = Uri.parse(draft.getTakenPhoto());
                    imageView.setImageURI(uri);
                    titleField.setText(draft.getTitleFill());
                }
            }
        });

         */

        templateViewModel.usingTemplate();
        templateViewModel.getUsingEmails().observe(Sheet.this, new Observer<List<Email>>() {
            @Override
            public void onChanged(List<Email> emails) {
                Iterator<Email> it = emails.iterator();
                List<String> emailString = new ArrayList();
                while(it.hasNext()){
                    Email node = it.next();
                    emailString.add(node.getEmail());
                }
                String localemail = String.join(",", emailString);
                
                if(
                        //emailField.getText().toString().length()<1
                        !sharedPreferences.contains(TEXT+"email")
                ) {
                    emailField.setText(localemail);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadData();
        updateView();
    }

    @Override
    protected void onStop() {
        saveData();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d("Sheet", "destroyed");

        super.onDestroy();
    }

    private void saveData() {
        editor = sharedPreferences.edit();
        editor.putString(TEXT + "title", titleField.getText().toString());
        editor.putString(TEXT + "email", emailField.getText().toString());//change to stringSet
        editor.putString(TEXT + "bread", breadField.getText().toString());
        editor.putString(TEXT + "image", image);
        editor.apply();
    }

    private void loadData() {
        title = sharedPreferences.getString(TEXT + "title", "");
        email = sharedPreferences.getString(TEXT + "email", "");
        bread = sharedPreferences.getString(TEXT + "bread", "");
        image = sharedPreferences.getString(TEXT + "image", "");
    }

    private void updateView() {
        titleField.setText(title);
        emailField.setText(email);//convert from stringSet to char sequence
        breadField.setText(bread);
        uri = Uri.parse(image);
        imageView.setImageURI(uri);
    }

    private void clearData() {
        editor.remove(TEXT + "title").commit();
        editor.remove(TEXT + "email").commit();
        editor.remove(TEXT + "bread").commit();
        editor.remove(TEXT + "image").commit();
        //templateViewModel.deleteDraft();
    }

    private void mailWithFile(Context context, Uri uri, List<String> emails, String title,
                              String message) {
        Intent sendEmail = new Intent(Intent.ACTION_SEND);
        sendEmail.setDataAndType(Uri.parse("mailto"),"image/jpeg");

        sendEmail.putExtra(Intent.EXTRA_EMAIL, emails.toArray(new String[0]));
        sendEmail.putExtra(Intent.EXTRA_SUBJECT, title);
        sendEmail.putExtra(Intent.EXTRA_TEXT, bread);
        sendEmail.putExtra(Intent.EXTRA_STREAM, uri);

        context.startActivity(Intent.createChooser(sendEmail, "Please select your preferred app!"));

    }

    private List<String> stringToList(String string) {
        return Arrays.asList(string.split(","));
    }

    Set newSet(List strings) {
        Set temp = new HashSet();
        //temp.addAll(old);
        return temp;
    }
}