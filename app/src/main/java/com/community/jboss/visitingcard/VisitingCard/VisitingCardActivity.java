package com.community.jboss.visitingcard.VisitingCard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.community.jboss.visitingcard.Maps.MapsActivity;
import com.community.jboss.visitingcard.R;
import com.community.jboss.visitingcard.SettingsActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import de.hdodenhof.circleimageview.CircleImageView;

public class VisitingCardActivity extends AppCompatActivity {
    CircleImageView profilePic;

    APIInterface apiInterface;

    EditText nameEditText;
    EditText phoneEditText;
    EditText emailEditText;

    String imageUri = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visiting_card);

        // TODO: Add a ImageView and a number of EditText to get his/her Visiting Card details (Currently authenticated User)

        // TODO: Add profileImage, Name, Email, PhoneNumber, Github, LinkedIn & Twitter Fields.

        // TODO: Clicking the ImageView should invoke an implicit intent to take an image using camera / pick an image from the Gallery.
        nameEditText = findViewById(R.id.nameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        emailEditText = findViewById(R.id.emailEditText);

        profilePic = findViewById(R.id.profilePic);

        apiInterface = APIClient.getClient().create(APIInterface.class);

        Call<VisitingCard> call = apiInterface.doGetVisitingCard();
        call.enqueue(new Callback<VisitingCard>() {
            @Override
            public void onResponse(Call<VisitingCard> call, Response<VisitingCard> response) {


                Log.d("TAG",response.code()+"");

                VisitingCard resource = response.body();
                String name = resource.name;
                String email = resource.email;
                String phone = resource.phone;

                Log.i("Response", name + " " + email + " " + phone);

                nameEditText.setText(name);
                emailEditText.setText(email);
                phoneEditText.setText(phone);

            }

            @Override
            public void onFailure(Call<VisitingCard> call, Throwable t) {
                call.cancel();
            }
        });

        profilePic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new AlertDialog.Builder(VisitingCardActivity.this)
                        .setIcon(android.R.drawable.ic_menu_camera)
                        .setTitle("Choose one")
                        .setPositiveButton("CAMERA", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(takePicture, 0);
                            }
                        })

                        .setNegativeButton("GALLERY", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent pickPhoto = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(pickPhoto, 1);
                            }
                        })
                        .show();

                // TODO: Align FAB to Bottom Right and replace it's icon with a SAVE icon
                // TODO: On Click on FAB should make a network call to store the entered information in the cloud using POST method(Do this in NetworkUtils class)
                FloatingActionButton fab = findViewById(R.id.fab);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Snackbar.make(view, "Proceed to Maps Activity", Snackbar.LENGTH_LONG)
                                .setAction("Yes", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent toVisitingCard = new Intent(VisitingCardActivity.this, MapsActivity.class);
                                        startActivity(toVisitingCard);
                                    }
                                }).show();
                    }
                });

            }
        });
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK) {
                    Bitmap bitmap = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    profilePic.setImageBitmap(bitmap);
                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    profilePic.setImageURI(selectedImage);
                    imageUri = selectedImage.toString();
                }
                break;

        }
    }

    public void uploadData(View view) {
        VisitingCard card = new VisitingCard();
        card.name = nameEditText.getText().toString();
        card.email = emailEditText.getText().toString();
        card.phone = phoneEditText.getText().toString();
        card.picture = imageUri;
        Call<VisitingCard> call = apiInterface.createVisitingCard(card);
        call.enqueue(new Callback<VisitingCard>() {
            @Override
            public void onResponse(Call<VisitingCard> call, Response<VisitingCard> response) {
                Log.i("Status", "Post successful");
                Log.d("Response", response.body().toString());
            }

            @Override
            public void onFailure(Call<VisitingCard> call, Throwable t) {
                call.cancel();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(VisitingCardActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

