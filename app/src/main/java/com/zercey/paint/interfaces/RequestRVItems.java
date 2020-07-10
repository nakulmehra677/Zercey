package com.zercey.paint.interfaces;

public interface RequestRVItems<BOTTOM_ITEM> {
    void getFirstPage();

    void getNextPage(BOTTOM_ITEM bottomItem);
}
