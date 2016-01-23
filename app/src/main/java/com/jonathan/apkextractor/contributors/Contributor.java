package com.jonathan.apkextractor.contributors;

public class Contributor {

    private String mTitle, mPerson, mGooglePlusUrl;

    public Contributor(String title, String person, String googlePlusUrl) {
        mTitle = title;
        mPerson = person;
        mGooglePlusUrl = googlePlusUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getPerson() {
        return mPerson;
    }

    public String getGooglePlusUrl() {
        return mGooglePlusUrl;
    }

}
