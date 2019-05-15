package cz.zelgadiss.myapplication;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;

import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static com.google.firebase.storage.FirebaseStorage.getInstance;


public class ProfileActivity extends AppCompatActivity {

    private static final int STORAGE_REQUEST_CODE = 100;
    private static final int IMAGE_PICK_GALLERY_REQUEST_CODE = 300;


    String storagePermissions[];


    Uri image_uri;

    String profileImage;
    String uploadImage;

    EditText editName;
    EditText editSurname;
    EditText editCountry;
    ImageView profilePic;
    TextView textEmail;

    FirebaseUser currentUser;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference databaseRef;
    StorageReference storageReference;
    String storagePath = "userImages/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkAppTheme);
        } else
            setTheme(R.style.AppTheme);

        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Профиль");
        setSupportActionBar(toolbar);



        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference("Users");
        storageReference = getInstance().getReference();


        textEmail = findViewById(R.id.text_email);
        editName = findViewById(R.id.edit_name);
        editSurname = findViewById(R.id.edit_surname);
        editCountry = findViewById(R.id.edit_country);
        profilePic = findViewById(R.id.profile_pic);

        /** if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
         if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
         != PackageManager.PERMISSION_GRANTED &&
         ActivityCompat.checkSelfPermission
         (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
         requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,},
         10);
         return;
         }
         } */


        findViewById(R.id.button_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInformation();
            }
        });

        findViewById(R.id.profile_pic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!checkStoragePermission()) {
                    requestStoragePermission();
                } else {
                    pickFromGallery();
                }
            }
        });

        profileImage = "image";

        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

        Query query = databaseRef.orderByChild("email").equalTo(currentUser.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    String email = "" + ds.child("email").getValue();
                    String firstname = "" + ds.child("firstname").getValue();
                    String secondname = "" + ds.child("secondname").getValue();
                    String country = "" + ds.child("country").getValue();
                    String image = "" + ds.child("image").getValue();

                    textEmail.setText("Почта: " + email);
                    editName.setText(firstname);
                    editSurname.setText(secondname);
                    editCountry.setText(country);

                    try {

                        Picasso.get().load(image).into(profilePic);

                    } catch (Exception e) {
                        Picasso.get().load(R.drawable.ic_add_image).into(profilePic);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        MenuItem mi = menu.findItem(R.id.menuHome);
        mi.setVisible(false);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            MenuItem mi_profile = menu.findItem(R.id.menuProfile);
            mi_profile.setIcon(R.drawable.ic_person_white_24dp);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuAbout:
                Intent intent_about = new Intent(ProfileActivity.this, Menu_About_Activity.class);
                startActivity(intent_about);
                break;

            case R.id.menuSettings:
                Intent intent_settings = new Intent(ProfileActivity.this, Menu_Settings_Activity.class);
                startActivity(intent_settings);
                break;


        }
        return true;
    }


    private void saveUserInformation() {

        String editName_change = editName.getText().toString();
        String editSurname_change = editSurname.getText().toString();
        String editCountry_change = editCountry.getText().toString();
        String uID = currentUser.getUid();
        String email = currentUser.getEmail();

        HashMap<Object, String> hashMap = new HashMap<>();
        hashMap.put("email", email);
        hashMap.put("firstname", editName_change);
        hashMap.put("secondname", editSurname_change);
        hashMap.put("country", editCountry_change);
        hashMap.put("uID", uID);
        if (uploadImage != null) {
            hashMap.put("image", uploadImage);
        }
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Users");
        reference.child(uID).setValue(hashMap);

        Toast.makeText(ProfileActivity.this, "Профиль обновлён", Toast.LENGTH_SHORT).show();

    }

    private boolean checkStoragePermission() {

        boolean result = ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result && result1;

    }

    private void requestStoragePermission() {

        ActivityCompat.requestPermissions(ProfileActivity.this, storagePermissions, STORAGE_REQUEST_CODE);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                pickFromGallery();
            } else {

                Toast.makeText(ProfileActivity.this, "Разрешите приложению получать доступ к галерее", Toast.LENGTH_SHORT).show();
            }

        }
    }


    private void pickFromGallery() {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, IMAGE_PICK_GALLERY_REQUEST_CODE);

        /** Intent intent = new Intent();
         intent.setType("image/*");
         intent.setAction(Intent.ACTION_GET_CONTENT);
         startActivityForResult(Intent.createChooser(intent, "Выберите изображение"), IMAGE_PICK_GALLERY_REQUEST_CODE ); */

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null && requestCode == IMAGE_PICK_GALLERY_REQUEST_CODE) {

            image_uri = data.getData();

            uploadProfilePhoto(image_uri);


            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), image_uri);
                profilePic.setImageBitmap(bitmap);


            } catch (IOException e) {
                e.printStackTrace();

            }

        }

    }


    private void uploadProfilePhoto(Uri image_uri) {


        String filePathAndName = storagePath + "" + "_" + currentUser.getUid();
        final StorageReference storageReference2 = storageReference.child(filePathAndName);

        storageReference2.putFile(image_uri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return storageReference2.getDownloadUrl();
            }

        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()){
                    Uri downloadUri = task.getResult();
                    uploadImage = downloadUri.toString();
                }
                else{
                    Toast.makeText(ProfileActivity.this, "Произошла ошибка при загрузке...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}






/**
        storageReference2.putFile(image_uri).continueWith(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if(!task.isSuccessful()){
                    throw task.getException();
                }
                return storageReference2.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Task<Uri>>() {
            @Override
            public void onComplete(@NonNull Task<Task<Uri>> task) {
                if (task.isSuccessful()){

                    Task<Uri> downloadUri = task.getResult();

                    String uID = currentUser.getUid();
                    HashMap<String, Object> results = new HashMap<>();
                    results.put("image", downloadUri.toString());
                    uploadImage = downloadUri.toString();

                    databaseRef.child(currentUser.getUid()).setValue(results)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {

                                @Override
                                public void onSuccess(Void aVoid) {

                                    Toast.makeText(ProfileActivity.this, "Изображение загружено", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    Toast.makeText(ProfileActivity.this, "Ошибка при загрузке изображения", Toast.LENGTH_SHORT).show();
                                }
                            });

                }
                else{

                    Toast.makeText(ProfileActivity.this, "Ошибка", Toast.LENGTH_SHORT).show();
                }

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}

*/



  /**      storageReference2.putFile(image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();

                Uri downloadUri = uriTask.getResult();


                if (uriTask.isSuccessful()){

                    HashMap<String, Object> results = new HashMap<>();
                    results.put(profileImage, downloadUri.toString());

                    databaseRef.child(currentUser.getUid()).setValue(results)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {

                                @Override
                                public void onSuccess(Void aVoid) {

                                    Toast.makeText(ProfileActivity.this, "Изображение загружено", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    Toast.makeText(ProfileActivity.this, "Ошибка при загрузке изображения", Toast.LENGTH_SHORT).show();
                                }
                            });

                }
                else{

                    Toast.makeText(ProfileActivity.this, "Ошибка", Toast.LENGTH_SHORT).show();
                }

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
*/



