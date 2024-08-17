package com.etrash.mytrashapp;

public class ProfileDB {
    private String Name, MailId, Phoneno, profile, referCode;
    private int coins;

    public ProfileDB(String name, String mailId, String phoneno, String profile, String referCode, int coins) {
        Name = name;
        MailId = mailId;
        Phoneno = phoneno;
        this.profile = profile;
        this.referCode = referCode;
        this.coins = coins;
    }

    public ProfileDB() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMailId() {
        return MailId;
    }

    public void setMailId(String mailId) {
        MailId = mailId;
    }

    public String getPhoneno() {
        return Phoneno;
    }

    public void setPhoneno(String phoneno) {
        Phoneno = phoneno;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getReferCode() {
        return referCode;
    }

    public void setReferCode(String referCode) {
        this.referCode = referCode;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }
}
