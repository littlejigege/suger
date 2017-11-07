package com.qgmobile.suger;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by jimji on 2017/11/7.
 */

public class LoadMoreRecyclerView extends RecyclerView {

    interface OnLoadMoreListener {
        void onLoadMore();
    }

    private OnLoadMoreListener onLoadMoreListener;

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public LoadMoreRecyclerView(Context context) {
        super(context);
    }

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
        if (isSlideToBottom()) {
            if (onLoadMoreListener != null) {
                onLoadMoreListener.onLoadMore();
            }
        }
    }

    public boolean isSlideToBottom() {
        return computeVerticalScrollExtent() + computeVerticalScrollOffset()
                >= computeVerticalScrollRange();
    }
}
