package me.oriley.vista;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Simple helper class that helps you using the Vista library
 * without using the components directly in XML. You can use it overriding
 * {@link android.support.v4.app.FragmentActivity#onCreateView(String, Context, AttributeSet) FragmentActivity.onCreateView(String, Context, AttributeSet)}
 * and returning the result of {@link VistaViewFactoryHelper#onCreateView(String, Context, AttributeSet)} if not null,
 * or simply creating your own {@link android.view.LayoutInflater.Factory LayoutInflater.Factory} and return the result of {@link VistaViewFactoryHelper#onCreateView(String, Context, AttributeSet)} if not null.
 *
 * @author Jonathan Hernández
 */
public class VistaViewFactoryHelper {

    private static final String GRID_VIEW = "GridView";
    private static final String LIST_VIEW = "ListView";
    private static final String SCROLL_VIEW = "ScrollView";
    private static final String VIEW_PAGER = "android.support.v4.view.ViewPager";
    private static final String NESTED_SCROLL_VIEW = "android.support.v4.widget.NestedScrollView";
    private static final String RECYCLER_VIEW = "android.support.v7.widget.RecyclerView";

    /**
     * @param name    The name of the {@link View}.
     * @param context The {@link Context} that should be used to create the {@link View}.
     * @param attrs   The {@link AttributeSet} that should be passed in the creation of the {@link View}.
     * @return A new Vista {@link View} if the class name matches with a replacement
     * in the library, null otherwise.
     */
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
