package com.apkextractor.android.common.itemdecorations;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};

    private Drawable divider;

    /**
     * Default divider will be used
     */
    public SimpleDividerItemDecoration(Context context) {
        final TypedArray styledAttributes = context.obtainStyledAttributes(ATTRS);
        divider = styledAttributes.getDrawable(0);
        styledAttributes.recycle();
    }

    /**
     * Custom divider will be used
     */
    public SimpleDividerItemDecoration(Context context, int resId) {
        divider = ContextCompat.getDrawable(context, resId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        DividerAdapter adapter =
                parent.getAdapter() instanceof DividerAdapter ?
                        ((DividerAdapter) parent.getAdapter()) : null;

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (adapter != null && !adapter.isDividerEnabledForItem(i)) {
                continue;
            }
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + divider.getIntrinsicHeight();

            divider.setBounds(left, top, right, bottom);
            divider.draw(c);
        }
    }

    /**
     * Implement this in your {@link android.support.v7.widget.RecyclerView.Adapter RecyclerView.Adapter} to chose
     * what items should have a divider.
     */
    public interface DividerAdapter {

        /**
         * @param position The position of the item.
         * @return True if the item should have a divider, false otherwise.
         */
        boolean isDividerEnabledForItem(int position);
    }
}