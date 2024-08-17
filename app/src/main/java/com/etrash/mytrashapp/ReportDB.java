package com.etrash.mytrashapp;

public class ReportDB {
    String name;
    String mailId;
    String PhoneNo;
    String Address;
    String Place;
    String Desc;
    String Refercode;

    public ReportDB(String name, String mailId, String phoneNo, String address, String place, String desc) {
        this.name = name;
        this.mailId = mailId;
        this.PhoneNo = phoneNo;
        this.Address = address;
        this.Place = place;
        this.Desc = desc;
    }

    public String getName() {
        return name;
    }

    public String getMailId() {
        return mailId;
    }

    public String getPhoneNo() {
        return PhoneNo;
    }

    public String getAddress() {
        return Address;
    }

    public String getPlace() {
        return Place;
    }

    public String getDesc() {
        return Desc;
    }
}
