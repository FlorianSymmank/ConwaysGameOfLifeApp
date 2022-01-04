package de.floriansymmank.conwaysgameoflife.adapter;

public interface ListAdapterListener<T> {
    void onRowClick(T item);
    void onRowLongClick(T item);
    void onButtonClick(T item);
}
