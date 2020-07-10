package com.zercey.paint.interfaces;

import java.util.List;

public interface OnGetRVListListener<T, BOTTOM_ITEM> {
    void onGetList(List<T> l, BOTTOM_ITEM bottomItem);

    void onListEmpty();

    void onFail(Exception e);
}
