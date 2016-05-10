package me.oriley.vista;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author Jonathan.
 */
public class VistaViewFactoryHelper {

    private static final String GRID_VIEW = "GridView";
    private static final String LIST_VIEW = "ListView";
    private static final String SCROLL_VIEW = "ScrollView";
    private static final String VIEW_PAGER = "android.support.v4.view.ViewPager";
    private static final String NESTED_SCROLL_VIEW = "android.support.v4.widget.NestedScrollView";
    private static final String RECYCLER_VIEW = "android.support.v7.widget.RecyclerView";

    public static View onCreateView(String name, Context context, AttributeSet attrs) {
        switch (name) {
            case GRID_VIEW:
                return new VistaGridView(context, attrs);
            case LIST_VIEW:
                return new VistaListView(context, attrs);
            case SCROLL_VIEW:
                return new VistaScrollView(context, attrs);
            case VIEW_PAGER:
                return new VistaViewPager(context, attrs);
            case NESTED_SCROLL_VIEW:
                return new VistaNestedScrollView(context, attrs);
            case RECYCLER_VIEW:
                return new VistaRecyclerView(context, attrs);
            default:
                return null;
        }
    }
}
