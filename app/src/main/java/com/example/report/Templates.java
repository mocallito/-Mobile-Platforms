package com.example.report;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Templates extends AppCompatActivity {

    private FloatingActionButton btncreate_template;
    private LinearLayout layout;
    private TemplateViewModel templateViewModel;
    private TemplateAdapter adapter;
    AlertDialog dialog;
    private LiveData<List<Template>> allTemplates;
    private List<String> names;
    EditText email;
    ImageView inuse;
    private List<Integer> deck;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_templates);

        initView();

        btncreate_template.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                //Intent intent = new Intent(Templates.this, Create_template.class);
                //startActivity(intent);
            }
        });
    }

    private void initView() {
        btncreate_template = findViewById(R.id.create_template);
        //layout = findViewById(R.id.template_container);
        recyclerView = findViewById(R.id.template_container);
        recyclerView.setLayoutManager(new LinearLayoutManager(Templates.this));
        recyclerView.setHasFixedSize(true);
        deck = new ArrayList<>();

        adapter = new TemplateAdapter(new TemplateAdapter.CardClickedListener() {
            @Override
            public void onDeleteClicked(Template template) {
                templateViewModel.delete(template);
                templateViewModel.onTemplateDelete(template.getName());
            }
            @Override
            public void onTemplateClicked(Template template) {
                //templateViewModel.setSelected();
                templateViewModel.selectCurrent(template.getName());
                Intent intent = new Intent(Templates.this, Create_template.class);
                startActivity(intent);
            }
            @Override
            public void onSetDefault(Template template, View v) {
                //inuse = recyclerView.getChildAt(template.getId()).findViewById(R.id.inuse);
                //Iterator<Integer> it = deck.iterator();
                /*
                int i = 0;
                while(i<recyclerView.getChildCount()){
                    ViewCompat.setBackgroundTintList(
                            recyclerView.getChildAt(i).findViewById(R.id.inuse),
                            ContextCompat.getColorStateList(Templates.this,android.R.color.holo_blue_bright));
                    //recyclerView.getChildAt(i).findViewById(R.id.inuse).setBackgroundColor(Color.parseColor("#ffaa66c"));
                    i++;
                }
                
                 */
                templateViewModel.selectDefault(template.getName());
                //inuse.setBackgroundColor(17170456);
                //v.findViewById(R.id.inuse).setBackgroundColor(Color.parseColor("#ffffbb33"));
                ViewCompat.setBackgroundTintList(
                        v.findViewById(R.id.inuse),
                        ContextCompat.getColorStateList(Templates.this,android.R.color.holo_blue_dark));
            }
        });
        recyclerView.setAdapter(adapter);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(Templates.this, MainActivity.class);
                startActivity(intent);
            }
        };
        this.getOnBackPressedDispatcher().addCallback(Templates.this, callback);

        templateViewModel = new ViewModelProvider(Templates.this).get(TemplateViewModel.class);
        allTemplates = templateViewModel.getLiveTemplate();
        names = new ArrayList<>();
        if(allTemplates.getValue() != null) {
            Iterator<Template> it = allTemplates.getValue().iterator();
            while(it.hasNext()){
                names.add(it.next().getName());
            }
        }

        allTemplates.observe(Templates.this, new Observer<List<Template>>() {
            @Override
            public void onChanged(@Nullable List<Template> templates) {
                //List<Template> newList = new ArrayList<Template>(templates);
                //adapter.submitList(newList);
                //adapter.setNotes(templates, layout);
                Iterator<Template> it = templates.iterator();
                deck.clear();
                while(it.hasNext()){
                    Template node = it.next();
                    names.add(node.getName());
                    deck.add(node.getId());
                    //deck.add(recyclerView.getChildAt(node.getId()).findViewById(R.id.inuse));
                }
                adapter.setNotes(templates, Templates.this);//send context
                //adapter.notifyDataSetChanged();//maybe not neccesary
                //addCard(templates.get(0).getEmail());
                //addCard("@");
                //Toast.makeText(Templates.this, templates.get(0).getEmail(), Toast.LENGTH_SHORT).show();
            }
        });
        buildDialog();
        //templateViewModel.insert(new Template("broxigar@orgimar.com", "empty", true, true, true));
    }
    private void buildDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.fill_contact, null);
        email = view.findViewById(R.id.email);
        builder.setView(view);
        builder.setTitle("Enter name")
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //addCard(email.getText().toString());//get from viewmodel
                        if(names == null) {
                            addNote();
                        }
                        else if(!names.contains(email.getText().toString())) {
                            addNote();
                        }
                        else {
                            Toast.makeText(Templates.this,"Name is taken, choose another", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        dialog = builder.create();
    }

    private void addNote(){
        /*
        int i = 0;
        while(i<recyclerView.getChildCount()){
            ViewCompat.setBackgroundTintList(
                    recyclerView.getChildAt(i).findViewById(R.id.inuse),
                    ContextCompat.getColorStateList(Templates.this,android.R.color.holo_blue_bright));
            //recyclerView.getChildAt(i).findViewById(R.id.inuse).setBackgroundColor(Color.parseColor("#ffaa66c"));
            i++;
        }*/
        templateViewModel.setSelected();
        templateViewModel.setDefault();
        templateViewModel.insert(new Template(email.getText().toString(), "empty", true, true, true, true, true));
        Intent intent = new Intent(Templates.this, Create_template.class);
        startActivity(intent);
    }
    private void addCard(String field) {
        View view = getLayoutInflater().inflate(R.layout.card, null);
        TextView emailView = view.findViewById(R.id.emailview);
        Button delete = view.findViewById(R.id.remove);

        emailView.setText(field);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.removeView(view);
            }
        });
        layout.addView(view);
    }

/*
    @Override
    public void onDeleteClicked(Template template){
        templateViewModel.delete(template);
        adapter.notifyDataSetChanged();//was not here
    }*/
}