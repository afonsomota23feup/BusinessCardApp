package com.feup.pesi.businesscard.activity;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.feup.pesi.businesscard.MainActivity;
import com.feup.pesi.businesscard.R;
import com.feup.pesi.businesscard.helper.DBHelper;
import com.feup.pesi.businesscard.User;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class AddUserActivity extends AppCompatActivity {

    private EditText editTextName, editTextProfession, editTextActivity,
            editTextEmail, editTextPhone;
    private ImageView imageViewUser;
    private byte[] imageBytes; // Alteração para armazenar os bytes da imagem
    private LinearLayout layoutSocialMediaLinks;
    private DBHelper dbHelper;

    // Lista predefinida de redes sociais
    private String[] socialMediaOptions;
    private Map<String, EditText> socialMediaEditTextMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        editTextName = findViewById(R.id.editTextUserName);
        editTextProfession = findViewById(R.id.editTextUserProfession);
        editTextActivity = findViewById(R.id.editTextUserActivity);
        editTextEmail = findViewById(R.id.editTextUserEmail);
        editTextPhone = findViewById(R.id.editTextUserPhone);
        imageViewUser = findViewById(R.id.imageViewUser);
        layoutSocialMediaLinks = findViewById(R.id.layoutSocialMediaLinks);
        dbHelper = new DBHelper(this);

        socialMediaOptions = getResources().getStringArray(R.array.social_media_options);
        socialMediaEditTextMap = new HashMap<>();


        Button btnAddUser = findViewById(R.id.buttonSaveUser);

        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser();
            }
        });

        Button btnAddSocialMedia = findViewById(R.id.buttonAddSocialMedia);
        btnAddSocialMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSocialMediaOptionsDialog();
            }
        });

        imageViewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });
    }

    private void addUser() {
        // Obter informações do novo usuário dos campos de texto
        String name = editTextName.getText().toString();
        String profession = editTextProfession.getText().toString();
        String activity = editTextActivity.getText().toString();
        String email = editTextEmail.getText().toString();
        String phone = editTextPhone.getText().toString();

        // Validar se os campos obrigatórios foram preenchidos
        if (name.isEmpty()) {
            editTextName.setError("Campo obrigatório");
            return;
        }

        // Criar um novo objeto User
        User newUser = new User();
        newUser.setName(name);
        newUser.setProfession(profession);
        newUser.setCurrentActivity(activity);
        newUser.setEmailAddress(email);
        newUser.setPhoneNumber(phone);

        // Adicionar a imagem selecionada ao novo usuário
        if (imageBytes != null) {
            newUser.setImageBytes(imageBytes);
        }

        // Adicionar os links das redes sociais ao novo usuário
        addSocialMediaLinks(newUser);

        // Adicionar o novo usuário ao banco de dados
        dbHelper.insertUser(newUser);

        // Retornar à MainActivity após adicionar o usuário
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void addSocialMediaLinks(User newUser) {
        for (Map.Entry<String, EditText> entry : socialMediaEditTextMap.entrySet()) {
            String socialMediaName = entry.getKey();
            String link = entry.getValue().getText().toString();

            // Adicionar link apenas se o nome e o link forem não vazios
            if (!socialMediaName.isEmpty() && !link.isEmpty()) {
                newUser.addSocialMediaLink(socialMediaName, link);
            } else {
                // Adicione logs para verificar o que está a acontecer
                Log.d("AddUserActivity", "Nome da Rede Social Vazio!: " + socialMediaName);
                Log.d("AddUserActivity", "Link Vazio para " + link);
            }
        }
    }

    private void showSocialMediaOptionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Escolha uma rede social");

        builder.setItems(socialMediaOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String socialMediaName = socialMediaOptions[which];
                addSocialMediaInputField(socialMediaName);
            }
        });

        builder.show();
    }

    private void addSocialMediaInputField(String socialMediaName) {
        EditText editTextSocialMedia = new EditText(this);
        editTextSocialMedia.setHint(socialMediaName);
        layoutSocialMediaLinks.addView(editTextSocialMedia);
        socialMediaEditTextMap.put(socialMediaName, editTextSocialMedia);
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    // Constante para identificar a solicitação de seleção de imagem
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                // Converta o URI da imagem em bytes
                imageBytes = convertUriToByteArray(data.getData());
                Picasso.get().load(data.getData()).into(imageViewUser);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Método para converter o URI da imagem em bytes
    private byte[] convertUriToByteArray(Uri uri) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (inputStream != null) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
            inputStream.close();
        }
        return byteArrayOutputStream.toByteArray();
    }
}
