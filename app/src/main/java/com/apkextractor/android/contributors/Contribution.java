package com.apkextractor.android.contributors;

/**
 * Represents a Contribution.
 */
public class Contribution {

    private String name, contributorName, contributorLink;

    /**
     * @param name The name of the contribution.
     * @param contributorName The name of the person who did the contribution.
     * @param contributorLink Link of any social media of the person.
     */
    public Contribution(String name, String contributorName, String contributorLink) {
        this.name = name;
        this.contributorName = contributorName;
        this.contributorLink = contributorLink;
    }

    public String getName() {
        return name;
    }

    public String getContributorName() {
        return contributorName;
    }

    public String getContributorLink() {
        return contributorLink;
    }

}
