package com.apkextractor.android.about.viewholders;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.apkextractor.android.R;
import com.apkextractor.android.about.models.Contribution;
import com.apkextractor.android.common.utils.ViewUtils;

/**
 * This {@link android.support.v7.widget.RecyclerView.ViewHolder RecyclerView.ViewHolder}
 * represents a {@link Contribution}.
 *
 * @author Jonathan Hernández
 */
public class ContributionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    //Views of this ViewHolder
    private TextView name, contributorName;
    private View contributorLinkButton;

    //The Intent that must get triggered after a click
    private Intent linkIntent;

    /**
     * {@inheritDoc}
     */
    public ContributionViewHolder(View itemView) {
        super(itemView);
        name = ViewUtils.findViewById(itemView, R.id.contribution_name);
        contributorName = ViewUtils.findViewById(itemView, R.id.contribution_contributor_name);
        contributorLinkButton = itemView.findViewById(R.id.contribution_contributor_link_button);

        itemView.setOnClickListener(this);
        contributorLinkButton.setOnClickListener(this);
    }

    /**
     * Sets the new {@link Contribution} that this
     * {@link ContributionViewHolder} should represent.
     *
     * @param contribution The {@link Contribution} to set.
     */
    public void setContribution(Contribution contribution) {
        name.setText(contribution.getName());
        contributorName.setText(contribution.getContributorName());
        linkIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(contribution.getContributorLink()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClick(View v) {
        v.getContext().startActivity(linkIntent);
    }
}
