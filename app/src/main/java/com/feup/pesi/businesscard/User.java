package com.feup.pesi.businesscard;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

public class User implements Parcelable {
    private long id;
    private String imagePath;
    private byte[] imageBytes; // Nova representação da imagem como array de bytes
    private String name;
    private String profession;
    private String currentActivity;
    private String emailAddress;
    private String phoneNumber;
    private String socialMediaAffiliation;
    private String personalPageLink;
    private Map<String, String> socialMediaLinks = new HashMap<>();



    // Métodos para Parcelable
    protected User(Parcel in) {
        id = in.readLong();
        imageBytes = in.createByteArray(); // Lê os bytes da imagem
        //imagePath = in.readString();
        name = in.readString();
        profession = in.readString();
        currentActivity = in.readString();
        emailAddress = in.readString();
        phoneNumber = in.readString();
        socialMediaAffiliation = in.readString();
        personalPageLink = in.readString();
    }


    public User(long id, byte[] imageBytes, String name, String profession, String currentActivity, String emailAddress, String phoneNumber, String socialname, String sociallink) {
        this.id = id;
        this.name = name;
        this.profession = profession;
        this.currentActivity = currentActivity;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.imageBytes = imageBytes; // Armazena a imagem como array de bytes
        this.socialMediaAffiliation = socialname;
        this.personalPageLink = sociallink;
    }
    public User() {
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(String currentActivity) {
        this.currentActivity = currentActivity;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSocialMediaAffiliation() {
        return socialMediaAffiliation;
    }

    public void setSocialMediaAffiliation(String socialMediaAffiliation) {
        this.socialMediaAffiliation = socialMediaAffiliation;
    }

    public String getPersonalPageLink() {
        return personalPageLink;
    }

    public void setPersonalPageLink(String personalPageLink) {
        this.personalPageLink = personalPageLink;
    }


    public void addSocialMediaLink(String socialMedia, String link) {
        socialMediaLinks.put(socialMedia, link);
    }

    public String getSocialMediaLink(String socialMedia) {
        return socialMediaLinks.get(socialMedia);
    }

    public Map<String, String> getSocialMediaLinks() {
        return socialMediaLinks;
    }

    public void setSocialMediaLinks(Map<String, String> socialMediaLinks) {
        this.socialMediaLinks = socialMediaLinks;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByteArray(imageBytes); // Grava os bytes da imagem
        dest.writeString(name);
        dest.writeString(profession);
        dest.writeString(currentActivity);
        dest.writeString(emailAddress);
        dest.writeString(phoneNumber);
        dest.writeString(socialMediaAffiliation);
        dest.writeString(personalPageLink);
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }
}

