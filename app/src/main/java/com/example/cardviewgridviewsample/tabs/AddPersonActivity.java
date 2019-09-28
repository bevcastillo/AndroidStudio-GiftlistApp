package com.example.cardviewgridviewsample.tabs;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cardviewgridviewsample.R;
import com.example.cardviewgridviewsample.objects.Person;
import com.example.cardviewgridviewsample.objects.Users;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class AddPersonActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView person_image, checkImage;
    Uri imageUri;
    TextInputEditText editText_pname, editText_pbudget;
    TextInputLayout pname_layout, pbudget_layout;
    private static final int PICK_IMAGE = 100;
    double person_budget;
    String PersonId;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);

        checkImage = (ImageView) findViewById(R.id.imageViewAdd);
        person_image = (ImageView) findViewById(R.id.person_image);
        pname_layout = (TextInputLayout) findViewById(R.id.person_name_layout);
        editText_pname = (TextInputEditText) findViewById(R.id.person_name);
        pbudget_layout = (TextInputLayout) findViewById(R.id.person_budget_layout);
        editText_pbudget = (TextInputEditText) findViewById(R.id.person_budget);


        checkImage.setOnClickListener(this);
        person_image.setOnClickListener(this);

        //
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("wishes_data");
        storageReference = FirebaseStorage.getInstance().getReference("person_image");
        PersonId = databaseReference.push().getKey();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.imageViewAdd:
                insertPerson();
                break;
            case R.id.person_image:
                uploadPersonImage();
                break;
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    private void insertPerson() {
        final String person_name = editText_pname.getText().toString();
        if (!editText_pbudget.getText().toString().equals("")){
            person_budget = Double.parseDouble(editText_pbudget.getText().toString());
        }

        addPerson(person_name, person_budget);
        Toast.makeText(AddPersonActivity.this, "Person successfully added!", Toast.LENGTH_SHORT).show();
        finish();

    }

    private void addPerson(final String person_name, final double person_budget){

        if (imageUri!=null){
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    +"."+getFileExtension(imageUri));

            uploadTask = fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            final Person person = new Person(person_name, person_budget);
                            person.setPerson_image(uri.toString()); //add the image Uri

                            SharedPreferences userPreference = getSharedPreferences("UserPref", MODE_PRIVATE);
                            final String username = (userPreference.getString("uname",""));

                            databaseReference.child("users").orderByChild("user_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()){
                                        for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                                            String usersKey = dataSnapshot2.getKey();
                                            Users users = dataSnapshot2.getValue(Users.class);
                                            databaseReference.child("users/"+usersKey+"/personlist").child(PersonId).setValue(person);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    });
                }
            });
        }

    }


    private void uploadPersonImage() {
        Intent intent  = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            person_image.setImageURI(imageUri);
        }else {
            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            person_image.setImageBitmap(bitmap);
        }

    }
}
