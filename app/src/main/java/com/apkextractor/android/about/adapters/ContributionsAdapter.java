package com.apkextractor.android.about.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.apkextractor.android.R;
import com.apkextractor.android.about.models.Contribution;
import com.apkextractor.android.about.viewholders.ContributionViewHolder;
import com.apkextractor.android.about.viewholders.HeaderViewHolder;
import com.apkextractor.android.common.itemdecorations.SimpleDividerItemDecoration;

import java.util.List;

/**
 * {@link android.support.v7.widget.RecyclerView.Adapter RecyclerView.Adapter} that shows a list of {@link Contribution Contributions}.
 * It also shows some information about the app as a header.
 *
 * @author Jonathan Hernández
 */
public class ContributionsAdapter extends RecyclerView.Adapter implements SimpleDividerItemDecoration.DividerAdapter {

    //View types of this Adapter.
    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_CONTRIBUTION = 1;

    private List<Contribution> contributions;
    private LayoutInflater inflater;

    public ContributionsAdapter(Context context, List<Contribution> contributions) {
        this.contributions = contributions;
        inflater = LayoutInflater.from(context);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_HEADER;
        }
        return VIEW_TYPE_CONTRIBUTION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                return new HeaderViewHolder(
                        inflater.inflate(
                                R.layout.about_header,
                                parent,
                                false));
            case VIEW_TYPE_CONTRIBUTION:
                return new ContributionViewHolder(
                        inflater.inflate(
                                R.layout.item_contribution,
                                parent,
                                false));
            default:
                return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_CONTRIBUTION) {
            ((ContributionViewHolder) holder).setContribution(contributions.get(position - 1));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getItemCount() {
        //We add one to make space for the header.
        return contributions.size() + 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDividerEnabledForItem(int position) {
        //This way the divider doesn't shows up for the first (header)
        //and last items.
        return position > 0 && position < getItemCount() - 1;
    }
}
