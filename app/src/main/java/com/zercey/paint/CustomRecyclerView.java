package com.zercey.paint;

import android.content.Context;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.zercey.paint.interfaces.RequestRVItems;

import java.util.List;

public class CustomRecyclerView<ADAPTER, MODEL, BOTTOM_ITEM> {

    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    private ProgressBar bottomProgress;
    private TextView emptyListImage;

    private LinearLayoutManager layoutManager;
    private Context context;

    private ADAPTER adapter;
    private List<MODEL> list;

    private RequestRVItems<BOTTOM_ITEM> listener;
    private boolean isScrolling;
    private boolean isLastItemFetched;
    private BOTTOM_ITEM bottomItem;

    public CustomRecyclerView(Context context, ADAPTER adapter, List<MODEL> list, RequestRVItems<BOTTOM_ITEM> listener) {
        this.context = context;
        this.adapter = adapter;
        this.list = list;
        this.listener = listener;

        isScrolling = false;
        isLastItemFetched = false;
    }

    public void initRecyclerView(
            RecyclerView recyclerView,
            SwipeRefreshLayout swipeRefreshLayout,
            ProgressBar progressBar,
            ProgressBar bottomProgress,
            TextView emptyListImage) {

        this.swipeRefreshLayout = swipeRefreshLayout;
        this.progressBar = progressBar;
        this.bottomProgress = bottomProgress;
        this.emptyListImage = emptyListImage;

        layoutManager = new LinearLayoutManager(context);

        recyclerView.setAdapter((RecyclerView.Adapter<RecyclerView.ViewHolder>) adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        );
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                long firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                long visibleItemCount = layoutManager.getChildCount();
                long totalItemCount = layoutManager.getItemCount();

                if (!recyclerView.canScrollVertically(1)) {

                    if (isScrolling && firstVisibleItem + visibleItemCount == totalItemCount && !isLastItemFetched) {
                        isScrolling = false;
                        bottomProgress.setVisibility(View.VISIBLE);
                        getItems();
                    }
                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(this::clearList);

        if (list.isEmpty()) {
            getItems();
        }
    }

    private void getItems() {
        if (Internet.isConnected(context)) {
            if (bottomItem == null) {
                progressBar.setVisibility(View.VISIBLE);
                listener.getFirstPage();
            } else {
                listener.getNextPage(bottomItem);
            }
        } else {
            removeAllProgress();
            Toast.makeText(context, "Internet not connected", Toast.LENGTH_SHORT).show();
        }
    }

    private void removeAllProgress() {
        swipeRefreshLayout.setRefreshing(false);
        progressBar.setVisibility(View.GONE);
        bottomProgress.setVisibility(View.GONE);
    }

    public void onFail() {
        removeAllProgress();
        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
    }

    public List<MODEL> getList() {
        return list;
    }

    public MODEL getItem(int position) {
        return list.get(position);
    }

    public void setItem(MODEL item, int position) {
        list.set(position, item);
        ((RecyclerView.Adapter) adapter).notifyDataSetChanged();
    }

    public void removeItem(int position) {
        list.remove(position);
        ((RecyclerView.Adapter) adapter).notifyDataSetChanged();
    }

    public void addAll(List<MODEL> l, BOTTOM_ITEM bottomItem) {
        list.addAll(l);
        this.bottomItem = bottomItem;

        if (l.size() < 20)
            isLastItemFetched = true;

        removeAllProgress();
        ((RecyclerView.Adapter) adapter).notifyDataSetChanged();
    }

    public void setListEmpty() {
        list.clear();
        removeAllProgress();
        emptyListImage.setVisibility(View.VISIBLE);
        ((RecyclerView.Adapter) adapter).notifyDataSetChanged();
    }

    public void clearList() {
        list.clear();
        bottomItem = null;
        isLastItemFetched = false;
        ((RecyclerView.Adapter) adapter).notifyDataSetChanged();
        getItems();
    }

    public int getSize() {
        return list.size();
    }

    public ADAPTER getAdapter() {
        return adapter;
    }

    public boolean isListEmpty() {
        return list.isEmpty();
    }
}