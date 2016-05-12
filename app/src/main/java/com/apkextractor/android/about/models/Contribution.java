package com.apkextractor.android.about.models;

/**
 * Represents a Contribution.
 *
 * @author Jonathan Hernández
 */
public class Contribution {

    private String name, contributorName, contributorLink;

    /**
     * @param name            The name of the contribution.
     * @param contributorName The name of the contributor.
     * @param contributorLink Link of any social media of the contributor.
     */
    public Contribution(String name, String contributorName, String contributorLink) {
        this.name = name;
        this.contributorName = contributorName;
        this.contributorLink = contributorLink;
    }

    /**
     * @return The name of the contribution.
     */
    public String getName() {
        return name;
    }

    /**
     * @return The name of the contributor.
     */
    public String getContributorName() {
        return contributorName;
    }

    /**
     * @return A link of any social media of the contributor.
     */
    public String getContributorLink() {
        return contributorLink;
    }

}
