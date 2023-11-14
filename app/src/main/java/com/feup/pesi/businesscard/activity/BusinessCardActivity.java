package com.feup.pesi.businesscard.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.feup.pesi.businesscard.R;
import com.feup.pesi.businesscard.User;
import com.feup.pesi.businesscard.helper.DBHelper;


public class BusinessCardActivity extends AppCompatActivity {

    private DBHelper dbHelper;


    private byte[] selectedImageBytes;

    private ImageView imageViewUser;

    private TextView textViewUserName, textViewUserProfession, textViewUserActivity,
            textViewUserEmail, textViewUserPhone, textViewUserSocialMedia, textViewUserPersonalPage;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_card);

        initializeViews();
        long userId = getIntent().getLongExtra("userId", -1);

        setupBusinessCard(userId);


    }

    private void initializeViews() {
        imageViewUser = findViewById(R.id.imageViewUserbc);
        textViewUserName = findViewById(R.id.textViewUserNamebc);
        textViewUserProfession = findViewById(R.id.textViewUserProfessionbc);
        textViewUserActivity = findViewById(R.id.textViewUserActivitybc);
        textViewUserEmail = findViewById(R.id.textViewEmailbc);
        textViewUserPhone = findViewById(R.id.textViewPhonebc);

    }
    private void displaySelectedImage(byte[] selectedImageBytes) {
        Glide.with(this).load(selectedImageBytes).into(imageViewUser);
    }

    private void setupBusinessCard(long userId) {
        // Configurar a imagem do usuário
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
           // textViewUserSocialMedia.setText(user.getSocialMediaAffiliation());
           // textViewUserPersonalPage.setText(user.getPersonalPageLink());
            setupSocialMediaLinks(user);

        } else {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
            finish();
        }

    }




    private void setupSocialMediaLinks(User user) {
        LinearLayout layoutSocialMediaLinks = findViewById(R.id.layoutSocialMediaLinks);

        // Obter a lista de links das redes sociais do usuário
        String socialMediaLinks = user.getSocialMediaAffiliation();

        // Separar os links por quebra de linha
        String[] linksArray = socialMediaLinks.split("\n");

        // Adicionar logos clicáveis para cada link de rede social
        for (String link : linksArray) {
            if (!link.isEmpty()) {
                String[] parts = link.split(": ");
                if (parts.length == 2) {
                    String socialMediaName = parts[0];
                    String socialMediaLink = parts[1];

                    ImageView imageViewSocialMedia = new ImageView(this);
                    imageViewSocialMedia.setImageResource(getSocialMediaLogo(socialMediaName));
                    imageViewSocialMedia.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openSocialMediaLink(socialMediaLink);
                        }
                    });

                    // Adicionar a imagem ao layout
                    layoutSocialMediaLinks.addView(imageViewSocialMedia);
                }
            }
        }
    }

    private int getSocialMediaLogo(String socialMediaName) {
        // Mapear nomes de redes sociais para recursos de imagem
        String resourceName = "ic_" + socialMediaName.toLowerCase();
        return getResources().getIdentifier(resourceName, "drawable", getPackageName());
    }

    private void openSocialMediaLink(String link) {
        // Abre o link da rede social no navegador
        Intent intent = new Intent(Intent.ACTION_VIEW, android.net.Uri.parse(link));
        startActivity(intent);
    }
}
