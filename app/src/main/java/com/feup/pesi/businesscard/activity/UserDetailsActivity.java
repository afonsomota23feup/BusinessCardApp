package com.feup.pesi.businesscard.activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.feup.pesi.businesscard.R;
import com.feup.pesi.businesscard.User;
import com.feup.pesi.businesscard.helper.DBHelper;

public class UserDetailsActivity extends AppCompatActivity {

    private ImageView imageViewUser;
    private TextView textViewUserName, textViewUserProfession, textViewUserActivity,
            textViewUserEmail, textViewUserPhone, textViewUserSocialMedia, textViewUserPersonalPage;

    private DBHelper dbHelper;
    private byte[] selectedImageBytes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        initializeViews();

        long userId = getIntent().getLongExtra("userId", -1);

        displayUserData(userId);

        Button btEdit = findViewById(R.id.buttonEdit);
        btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Adicione a lógica para editar as informações do usuário
                Toast.makeText(UserDetailsActivity.this, "Edit button clicked", Toast.LENGTH_SHORT).show();
            }
        });
        Button buttonBusinessCard = findViewById(R.id.buttonBusinessCard);

        buttonBusinessCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Adicione a lógica para abrir o e-Business Card
                Toast.makeText(UserDetailsActivity.this, "e-Business Card button clicked", Toast.LENGTH_SHORT).show();
                // Substitua BusinessCardActivity.class pela sua atividade de cartão de visita
                Intent intent = new Intent(UserDetailsActivity.this, BusinessCardActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });
    }

    private void initializeViews() {
        imageViewUser = findViewById(R.id.imageViewUser);
        textViewUserName = findViewById(R.id.textViewTextUserName);
        textViewUserProfession = findViewById(R.id.textViewTextUserProfession);
        textViewUserActivity = findViewById(R.id.textViewTextUserActivity);
        textViewUserEmail = findViewById(R.id.textViewTextUserEmail);
        textViewUserPhone = findViewById(R.id.textViewTextUserPhone);
        textViewUserSocialMedia = findViewById(R.id.textViewTextUserSocialMedia);
        textViewUserPersonalPage = findViewById(R.id.textViewTextUserPageLink);
    }

    private void displaySelectedImage(byte[] selectedImageBytes) {
        Glide.with(this).load(selectedImageBytes).into(imageViewUser);
    }

    private void displayUserData(long userId) {
        if (userId != -1) {
            dbHelper = new DBHelper(this);
            User user = dbHelper.getUserFromDatabase(userId);

            if (user != null && user.getImageBytes() != null) {
                selectedImageBytes = user.getImageBytes();
                displaySelectedImage(selectedImageBytes);
            } else {
                Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show();
            }

            textViewUserName.setText(user.getName());
            textViewUserProfession.setText(user.getProfession());
            textViewUserActivity.setText(user.getCurrentActivity());
            textViewUserEmail.setText(user.getEmailAddress());
            textViewUserPhone.setText(user.getPhoneNumber());
            textViewUserSocialMedia.setText(user.getSocialMediaAffiliation());
            textViewUserPersonalPage.setText(user.getPersonalPageLink());
        } else {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

}
