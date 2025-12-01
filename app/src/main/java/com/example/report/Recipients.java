package com.example.report;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Recipients extends AppCompatActivity {

    FloatingActionButton btnadd;
    AlertDialog dialog;
    AlertDialog updateDialog;
    LinearLayout layout;
    EditText email;
    private TemplateViewModel templateViewModel;
    private EmailAdapter adapter;
    List<String> names;
    Template templaite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipients);

        initView();
//add observer, call addcard for each emails
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
    }

    private void initView() {
        btnadd = findViewById(R.id.add_recipients);
        //layout = findViewById(R.id.container);
        templateViewModel = new ViewModelProvider(Recipients.this).get(TemplateViewModel.class);
        RecyclerView recyclerView = findViewById(R.id.container);
        recyclerView.setLayoutManager(new LinearLayoutManager(Recipients.this));
        recyclerView.setHasFixedSize(true);
        List<Integer> deck = new ArrayList<>();

        adapter = new EmailAdapter(new EmailAdapter.CardClickedListener() {
            @Override
            public void onDeleteClicked(Email email) {
                templateViewModel.deleteEmail(email);
            }
            @Override
            public void onTemplateClicked(Email email, View view) {
                buildDialog(email, view);
                updateDialog.show();
                //templateViewModel.setSelected();
                //templateViewModel.selectCurrent(template.getName());
                //Intent intent = new Intent(Templates.this, Create_template.class);
                //startActivity(intent);
            }

        });
        recyclerView.setAdapter(adapter);

        //taking from the template_settings
        templateViewModel.getCurrent();
        templateViewModel.getSelected().observe(Recipients.this, new Observer<Template>() {
            @Override
            public void onChanged(Template template) {
                templaite = template;
                //templateViewModel.getAllEmails(templaite.getName());
            }
        });
        LiveData<List<Email>> allEmail = templateViewModel.getAllEmails();

        names = new ArrayList<>();
        if(allEmail.getValue() != null) {
            Iterator<Email> it = allEmail.getValue().iterator();
            while(it.hasNext()){
                names.add(it.next().getEmail());
            }
        }
        allEmail.observe(Recipients.this, new Observer<List<Email>>() {
            @Override
            public void onChanged(List<Email> strings) {
                Iterator<Email> it = strings.iterator();
                deck.clear();
                while(it.hasNext()){
                    Email node = it.next();
                    names.add(node.getEmail());
                    deck.add(node.getId());
                    //deck.add(recyclerView.getChildAt(node.getId()).findViewById(R.id.inuse));
                }
                adapter.setNotes(strings);
            }
        });

        buildDialog();
    }

    private void buildDialog(Email current, View selected){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.fill_contact, null);
        email = view.findViewById(R.id.email);//move, replace with observer but just append the latest?
        builder.setView(view);
        TextView textView = selected.findViewById(R.id.emailview);
        email.setText(textView.getText().toString());
        builder.setTitle("Enter email")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        current.setEmail(email.getText().toString());
                            templateViewModel.updateEmail(current);
                            Toast.makeText(Recipients.this,"Recipient updated", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        updateDialog = builder.create();
    }

    private void buildDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.fill_contact, null);
        email = view.findViewById(R.id.email);//move, replace with observer but just append the latest?
        builder.setView(view);
        builder.setTitle("Enter email")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
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
                            Toast.makeText(Recipients.this,"Name is taken, choose another", Toast.LENGTH_SHORT).show();
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
        //templateViewModel.setSelected();
        templateViewModel.insertEmail(new Email(templaite.getName(), email.getText().toString()));
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
}