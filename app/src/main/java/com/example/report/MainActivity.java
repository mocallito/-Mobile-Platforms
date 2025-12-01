package com.example.report;

import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.example.report.EntityRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton btnload;
    private Button btncapture, btnsettings;
    ImageView imageView;
    NotificationManagerCompat managerCompat;
    NotificationCompat.Builder builder;

    private static final int CAMERA_PERMISSION_CODE = 1;
    private static final int NOTIFICATION_PERMISSION_CODE = 2;
    private static final int LOCATION_PERMISSION_CODE = 3;

    Uri imageUri;
    //ActivityMainBinding mainBinding;//look for documents
    ActivityResultLauncher<Uri> takePictureLauncher;
    private File imageFile;
    private TemplateViewModel templateViewModel;
    Template templaite;
    Draft draft;
    AlertDialog dialog;
    public final String SHARED_PREFS = "sharedPrefs";
    public final String TEXT = "text";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String text;
    Calendar calendar;
    LocationManager locationManager;
    Location lastKnownLocation;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HardwareButtonReceiver receiver = new HardwareButtonReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_LOW);
        this.registerReceiver(receiver, intentFilter);

        initView();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("Wifi warning", "Wifi warning", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        if(ContextCompat.checkSelfPermission(MainActivity.this,android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    android.Manifest.permission.POST_NOTIFICATIONS
            },NOTIFICATION_PERMISSION_CODE);
        }

        btnsettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Settings.class);
                startActivity(intent);


                //NOTIFICATION
                builder = new NotificationCompat.Builder(MainActivity.this, "Wifi warning");
                builder.setContentTitle("Button test");
                builder.setContentText("Button test success");
                builder.setSmallIcon(R.drawable.ic_launcher_foreground);
                builder.setAutoCancel(true);

                managerCompat = NotificationManagerCompat.from(MainActivity.this);
                if (ActivityCompat.checkSelfPermission(MainActivity.this,
                        android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATION_PERMISSION_CODE);
                } else {
                    managerCompat.notify(1, builder.build());
                }


            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            // Request permissions if not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_CODE);
        }

        btncapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCameraPermissionAndOpenCamera();
            }
        });
    }

    private void initView() {
        btncapture = findViewById(R.id.capture);
        btnsettings = findViewById(R.id.settings);
        btnload = findViewById(R.id.load);
        calendar = Calendar.getInstance();
        imageUri = createUri();
        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        templaite = null;
        lastKnownLocation = null;
        text = "";
        buildDialog();
        //setContentView(mainBinding.getRoot());
        /*
        db =  (EntityDatabase)Room.inMemoryDatabaseBuilder(getApplicationContext(), EntityDatabase.class).allowMainThreadQueries().build();
        entityRepository = new EntityRepository(db.entityDao());
        entityViewModel = new EntityViewModel(entityRepository);

         */
        templateViewModel =  new ViewModelProvider(MainActivity.this).get(TemplateViewModel.class);
        templateViewModel.usingTemplate();
        templateViewModel.getInuse().observe(MainActivity.this, new Observer<Template>() {
            @Override
            public void onChanged(Template template) {
                templaite=template;
                //Log.d("LiveData", "called");//never called even usingTemplate called
                if(templaite == null) {
                    dialog.show();
                }
            }
        });
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                lastKnownLocation = location;
                Log.d("Location Changed", location.toString());
            }
        };
        getAddress();

        templateViewModel.getMyResponse().observe(MainActivity.this, new Observer<Response<Post>>() {
            @Override
            public void onChanged(Response<Post> postResponse) {
                if(postResponse.isSuccessful()) {
                    if (postResponse.body().getKeywords() != null) {
                        //worked for getPost
                        Log.d("Main Response", String.valueOf(postResponse.body().getKeywords().get(0).getKeyword().toString()));
                        //draft.setTitleFill(postResponse.body().getKeywords().get(0).getKeyword().toString());
                        //templateViewModel.updateDraft(draft);
                        editor.putString(TEXT + "title", postResponse.body().getKeywords().get(0).getKeyword().toString());
                        //editor.putString(TEXT + "email", emailField.getText().toString());//change to stringSet
                        editor.apply();
                        Intent intent = new Intent(MainActivity.this, Sheet.class);
                        startActivity(intent);
                    } else {
                        Log.d("Main Response", postResponse.body().toString());
                    }
                } else {
                    try {
                        Log.d("Main ErrorResponse", postResponse.errorBody().string());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
/*
        templateViewModel.getCurrentImage().observe(MainActivity.this, new Observer<Draft>() {
            @Override
            public void onChanged(Draft drafts) {
                if (drafts != null) {
                    draft = drafts;
                }
            }
        });

 */

        takePictureLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                try {
                    if(result) {
                        clearData();
                        editor.putString(TEXT + "image", imageUri.toString());
                        //templateViewModel.insertDraft(new Draft(imageUri.toString(),""));

                        //templateViewModel.usingTemplate();
                        Log.d("Main template", String.valueOf(templaite != null));
                        if(templaite != null) {

                            load(templaite.getType());
                            autoFilling(templaite.getType());
                            editor.putString(TEXT + "bread", text);
                            if(templaite.getAutofillTitle()) {
                                templateViewModel.uploadImage2(imageFile, 1);
                            } else {
                                editor.apply();
                                Intent intent = new Intent(MainActivity.this, Sheet.class);
                                startActivity(intent);
                            }
                        } /*else {
                            editor.apply();
                            Intent intent = new Intent(MainActivity.this, Sheet.class);
                            startActivity(intent);
                        }
                        */
                    }
                } catch (Exception exception) {
                    exception.getStackTrace();
                }
            }
        });

        btnload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!sharedPreferences.getAll().isEmpty()) {
                    Intent intent = new Intent(MainActivity.this, Sheet.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "No recently saved found", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private Uri createUri() {
        imageFile = new File(getApplicationContext().getFilesDir(), calendar.getTime().toString()+"camera_photo.jpg");
        return FileProvider.getUriForFile(
                getApplicationContext(),
                "com.example.report.fileProvider",
                imageFile
        );
    }
    /*
        private Bitmap toBitmap(Uri uri){
            Bitmap bitmap = null;
            ContentResolver contentResolver = getContentResolver();
            try {
                if (Build.VERSION.SDK_INT < 28) {
                    bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri);
                } else {
                    ImageDecoder.Source source = ImageDecoder.createSource(contentResolver, uri);
                    bitmap = ImageDecoder.decodeBitmap(source);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

     */
    private void checkCameraPermissionAndOpenCamera() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this,
                android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{android.Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            takePictureLauncher.launch(imageUri);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePictureLauncher.launch(imageUri);
            }
            else {

            }
        }
        if (requestCode == NOTIFICATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                managerCompat.notify(1, builder.build());
            }
            else {

            }
        }

        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //something else
                //managerCompat.notify(1, builder.build());
            }
            else {

            }
        }

    }

    private void launchApp() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
    private void clearData() {
        editor.remove(TEXT + "title").commit();
        editor.remove(TEXT + "email").commit();
        editor.remove(TEXT + "bread").commit();
        editor.remove(TEXT + "image").commit();
        //templateViewModel.deleteDraft();
    }
    @Override
    protected void onStart() {
        //templateViewModel.deleteDraft();
        super.onStart();
    }
    private void buildDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.fill_contact, null);
        EditText email = view.findViewById(R.id.email);
        email.setText("No templates selected!");
        builder.setView(view);
        builder.setTitle("ERROR")
                .setPositiveButton("GO CREATE ONE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainActivity.this, Templates.class);
                        startActivity(intent);
                    }
                })
        ;
        builder.setCancelable(false);
        dialog = builder.create();
    }
    private void load(String type) {
        if(type.equals("empty")) {
            return;
        }
        FileInputStream fis = null;
        String txt;
        try {
            InputStream input = getAssets().open(type+".txt");
            int size = input.available();
            byte[] buffer = new byte[size];
            input.read(buffer);
            input.close();

            text = new String(buffer);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void autoFilling(String type) {
        if(type.equals("empty")) {
            return;
        }
        String time = "";
        String date = "";
        String currentLocation = "";
//in the future can put in if(type.equals("incidence.txt"))
        if(templaite.getSyncedTime()) {
// Get the current hour, minute, and second
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            int second = calendar.get(Calendar.SECOND);

            // Create a string with hour, minute, and second
            time = hour + ":" + minute + ":" + second;

            // Get the current year, month, and day
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1; // Month is zero-based, so add 1
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            // Create a string with year, month, and day
            date = year + "-" + month + "-" + day;
        }

        if(templaite.getUserYourlocation()) {
            currentLocation = getAddress();
        }

        text = text.replace("[Date]",date)
                .replace("[Location]",currentLocation)
                .replace("[Time]",time);
    }
    private String getAddress() {
        String fullAddress = "";
        // Get the last known location
        //Location lastKnownLocation = getLastKnownLocation();
        //other version
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            // Request permissions if not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_CODE);
        } else {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, (long) 5000, (float) 5, locationListener);
            //end of other version

            if (lastKnownLocation != null) {
                // Get the address using Geocoder
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                Log.d("Location", "Last known location is not null");
                try {
                    List<android.location.Address> addresses = geocoder.getFromLocation(
                            lastKnownLocation.getLatitude(),
                            lastKnownLocation.getLongitude(),
                            1); // Get only one result

                    if (addresses != null && addresses.size() > 0) {
                        android.location.Address address = addresses.get(0);

                        // Access various address details
                        fullAddress = address.getAddressLine(0); // Full address string
                        Log.d("Address", "Full Address: " + fullAddress);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Log.d("Location", "Last known location is null");
            }
        }
        return fullAddress;
    }

    private Location getLastKnownLocation() {
        // Obtain the last known location using the desired method (e.g., GPS_PROVIDER)
        // Ensure you have already obtained location permissions before calling this method
        // You can customize this part based on your requirements
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            // Request permissions if not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_CODE);
        }

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (locationManager != null) {
            Location lastKnownLocationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location lastKnownLocationNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Log.d("LocationNetwork",String.valueOf(lastKnownLocationNetwork != null));
            Log.d("LocationGPS",String.valueOf(lastKnownLocationGPS != null));

            // Return the most recent known location
            if (lastKnownLocationGPS != null && lastKnownLocationNetwork != null) {
                return (lastKnownLocationGPS.getTime() > lastKnownLocationNetwork.getTime()) ?
                        lastKnownLocationGPS : lastKnownLocationNetwork;
            } else if (lastKnownLocationGPS != null) {
                return lastKnownLocationGPS;
            } else if (lastKnownLocationNetwork != null) {
                return lastKnownLocationNetwork;
            }
        }
        Log.d("LocationManger",String.valueOf(locationManager != null));

        return null;
    }
}