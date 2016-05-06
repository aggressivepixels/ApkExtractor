package com.android.apk.extractor.util;

import android.view.View;

/**
 * Simple class that manages empty, loading and normal states of a List.
 * The list can be any view.
 * To change the states, just call {@code setState(int state)}, it can be one of
 * {@code STATE_NORMAL}, {@code STATE_EMPTY} or {@code STATE_LOADING}.
 */
public class ListStateManager {

    public static final int STATE_NORMAL = 0;
    public static final int STATE_EMPTY = 1;
    public static final int STATE_LOADING = 2;

    private View mList;
    private View mLoading;
    private View mEmpty;

    private int mCurrentState;

    /**
     * @param list The list view. (It can be any kind of view)
     * @param loading The view that shows the loading indicator.
     * @param empty The empty view.
     */
    public ListStateManager(View list, View loading, View empty) {
        mList = list;
        mLoading = loading;
        mEmpty = empty;
    }

    /**
     * Sets the state of the list.
     *
     * @param state The state to set.
     */
    public void setState(int state) {
        if (mCurrentState == state || state > STATE_LOADING) {
            return;
        }
        mCurrentState = state;
        switch (mCurrentState) {
            case STATE_NORMAL:
                ViewUtils.fadeOut(mEmpty);
                ViewUtils.fadeOut(mLoading);
                ViewUtils.fadeIn(mList);
                break;
            case STATE_LOADING:
                ViewUtils.fadeOut(mEmpty);
                ViewUtils.fadeOut(mList);
                ViewUtils.fadeIn(mLoading);
                break;
            case STATE_EMPTY:
                ViewUtils.fadeOut(mLoading);
                ViewUtils.fadeIn(mEmpty);
                break;
        }
    }

    /**
     * @return The current state.
     */
    public int getCurrentState() {
        return mCurrentState;
    }
}
