package com.feup.pesi.businesscard.helper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.feup.pesi.businesscard.User;

import java.util.*;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "user_db";
    private static final int DATABASE_VERSION = 1;

    // Tabela de usuários
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PROFESSION = "profession";
    public static final String COLUMN_ACTIVITY = "activity";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_IMAGE_PATH = "image_path";

    public static final String COLUMN_IMAGE_BYTES = "image_bytes"; // Adicionada nova coluna

    // Tabela de redes sociais
    public static final String TABLE_SOCIAL_MEDIA = "social_media";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_SOCIAL_MEDIA_NAME = "social_media_name";
    public static final String COLUMN_SOCIAL_MEDIA_LINK = "social_media_link";

    // Comando SQL para criar a tabela de usuários
    private static final String TABLE_CREATE_USERS =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_IMAGE_BYTES + " BLOB, " +  // Nova coluna para armazenar a imagem como array de bytes
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_PROFESSION + " TEXT, " +
                    COLUMN_ACTIVITY + " TEXT, " +
                    COLUMN_EMAIL + " TEXT, " +
                    COLUMN_PHONE + " TEXT)";

    // Comando SQL para criar a tabela de redes sociais
    private static final String TABLE_CREATE_SOCIAL_MEDIA =
            "CREATE TABLE " + TABLE_SOCIAL_MEDIA + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USER_ID + " INTEGER, " +
                    COLUMN_SOCIAL_MEDIA_NAME + " TEXT, " +
                    COLUMN_SOCIAL_MEDIA_LINK + " TEXT, " +
                    "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + "))";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Criação das tabelas
        db.execSQL(TABLE_CREATE_USERS);
        db.execSQL(TABLE_CREATE_SOCIAL_MEDIA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Se você precisar fazer alguma atualização do esquema do banco de dados, faça aqui
    }

    public long insertUser(User user) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues userValues = new ContentValues();
        userValues.put(COLUMN_IMAGE_BYTES, user.getImageBytes()); // Armazena a imagem como array de bytes
        userValues.put(COLUMN_NAME, user.getName());
        userValues.put(COLUMN_PROFESSION, user.getProfession());
        userValues.put(COLUMN_ACTIVITY, user.getCurrentActivity());
        userValues.put(COLUMN_EMAIL, user.getEmailAddress());
        userValues.put(COLUMN_PHONE, user.getPhoneNumber());

        long userId = db.insert(TABLE_USERS, null, userValues);

        Log.d("DBHelper", "Inserted user with ID: " + userId);


        if (userId != -1 && user.getSocialMediaLinks() != null) {
            for (Map.Entry<String, String> entry : user.getSocialMediaLinks().entrySet()) {
                ContentValues socialMediaValues = new ContentValues();
                socialMediaValues.put(COLUMN_USER_ID, userId);
                socialMediaValues.put(COLUMN_SOCIAL_MEDIA_NAME, entry.getKey());
                socialMediaValues.put(COLUMN_SOCIAL_MEDIA_LINK, entry.getValue());
                long socialMediaId = db.insert(TABLE_SOCIAL_MEDIA, null, socialMediaValues);
                Log.d("DBHelper", "Inserted social media with ID: " + socialMediaId);

            }
        }

        return userId;
    }


    @SuppressLint("Range")
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_USERS, null, null, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                User user = new User();
                user.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
                user.setImageBytes(cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE_BYTES))); // Obtém a imagem como array de bytes
                user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                user.setProfession(cursor.getString(cursor.getColumnIndex(COLUMN_PROFESSION)));
                user.setCurrentActivity(cursor.getString(cursor.getColumnIndex(COLUMN_ACTIVITY)));
                user.setEmailAddress(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)));
                user.setPhoneNumber(cursor.getString(cursor.getColumnIndex(COLUMN_PHONE)));

                // Recuperar redes sociais associadas ao usuário
                List<Map<String, String>> socialMediaList = getSocialMediaForUser(user.getId());
                for (Map<String, String> socialMedia : socialMediaList) {
                    user.setSocialMediaAffiliation(COLUMN_SOCIAL_MEDIA_NAME);
                    user.setPersonalPageLink(COLUMN_SOCIAL_MEDIA_LINK);
                    user.addSocialMediaLink(socialMedia.get(COLUMN_SOCIAL_MEDIA_NAME), socialMedia.get(COLUMN_SOCIAL_MEDIA_LINK));
                }


                userList.add(user);
            }
            cursor.close();
        }
        return userList;
    }

    public User getUserFromDatabase(long userId) {
        SQLiteDatabase db = getReadableDatabase();
        User user = null;

        String[] projection = {COLUMN_ID, COLUMN_IMAGE_BYTES, COLUMN_NAME, COLUMN_PROFESSION, COLUMN_ACTIVITY, COLUMN_EMAIL, COLUMN_PHONE};
        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(userId)};

        Cursor cursor = db.query(TABLE_USERS, projection, selection, selectionArgs, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                @SuppressLint("Range") long id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
                @SuppressLint("Range") byte[] imageBytes = cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE_BYTES));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                @SuppressLint("Range") String profession = cursor.getString(cursor.getColumnIndex(COLUMN_PROFESSION));
                @SuppressLint("Range") String activity = cursor.getString(cursor.getColumnIndex(COLUMN_ACTIVITY));
                @SuppressLint("Range") String email = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL));
                @SuppressLint("Range") String phone = cursor.getString(cursor.getColumnIndex(COLUMN_PHONE));


                // Recuperar redes sociais associadas ao usuário
                List<Map<String, String>> socialMediaList = getSocialMediaForUser(userId);
                for (Map<String, String> socialMedia : socialMediaList) {
                    String socialname = socialMedia.get(COLUMN_SOCIAL_MEDIA_NAME);
                    String sociallink = socialMedia.get(COLUMN_SOCIAL_MEDIA_LINK);
                    //user.addSocialMediaLink(socialMedia.get(COLUMN_SOCIAL_MEDIA_NAME), socialMedia.get(COLUMN_SOCIAL_MEDIA_LINK));
                    user = new User(id, imageBytes, name, profession, activity, email, phone, socialname, sociallink);

                }

            }

            cursor.close();
        }

        return user;
    }
    @SuppressLint("Range")
    private List<Map<String, String>> getSocialMediaForUser(long userId) {
        List<Map<String, String>> socialMediaList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {COLUMN_SOCIAL_MEDIA_NAME, COLUMN_SOCIAL_MEDIA_LINK};
        String selection = COLUMN_USER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(userId)};

        Cursor cursor = db.query(TABLE_SOCIAL_MEDIA, projection, selection, selectionArgs, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Map<String, String> socialMedia = new HashMap<>();
                socialMedia.put(COLUMN_SOCIAL_MEDIA_NAME, cursor.getString(cursor.getColumnIndex(COLUMN_SOCIAL_MEDIA_NAME)));
                socialMedia.put(COLUMN_SOCIAL_MEDIA_LINK, cursor.getString(cursor.getColumnIndex(COLUMN_SOCIAL_MEDIA_LINK)));
                socialMediaList.add(socialMedia);
            }
            cursor.close();
        }

        return socialMediaList;
    }
}
