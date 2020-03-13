package ir.pirayeh1485.paginationrecyclerview;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class PaginationAdapter<T, H extends PaginationViewHolder<T>> extends RecyclerView.Adapter<H> {

    public static final int ROW_PER_PAGE_NO_LOADING = -1;
    public static int DEFAULT_ROW_PER_PAGE = 10;

    protected List<T> dataSet;
    protected Context context;
    protected int currentPosition = 0;
    private PaginationRecyclerView container;

    private int lastPage = 0;
    private int page = 1;
    private int rowPerPage = DEFAULT_ROW_PER_PAGE;

    protected OnItemClickListener<T> mItemClickListener;

    public PaginationAdapter(Context context, List<T> dataSet) {
        this.context = context;
        if (dataSet == null) {
            this.dataSet = new ArrayList<>();
        } else {
            this.dataSet = dataSet;
        }
    }

    public PaginationAdapter<T, H> setOnItemClickListener(OnItemClickListener<T> listener) {
        mItemClickListener = listener;
        return this;
    }

    public PaginationAdapter<T, H> removeItem(int position) {
        dataSet.remove(position);
        notifyItemRemoved(position);
        return this;
    }

    public PaginationAdapter<T, H> addItems(List<T> items) {
        this.container.setLoading(false);

        for (T t : items) {
            this.dataSet.add(t);
            notifyItemInserted(this.dataSet.size() - 1);
        }

        lastPage = page;
        if (items.size() == rowPerPage) {
            page++;
        }
        return this;
    }

    public PaginationAdapter<T, H> addAllItems(List<T> items) {
        this.dataSet.addAll(items);
        this.container.setLoading(false);

        lastPage = page;
        if (items.size() == rowPerPage) {
            page++;
        }

        notifyDataSetChanged();
        return this;
    }

    public PaginationAdapter<T, H> addItem(T item) {
        this.dataSet.add(item);
        this.container.setLoading(false);

        lastPage = page;
        page = (dataSet.size() / rowPerPage) + 1;

        notifyDataSetChanged();
        return this;
    }


    public int getCurrentPosition() {
        return currentPosition;
    }

    public int getRowPerPage() {
        return rowPerPage;
    }

    public PaginationAdapter<T, H> setRowPerPage(int rowPerPage) {
        this.rowPerPage = rowPerPage;
        return this;
    }

    public int getPage() {
        return page;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void disableLoadMore() {
        rowPerPage = ROW_PER_PAGE_NO_LOADING;
        if (container != null) {
            container.disableLoadMore();
        }
    }

    public boolean isLoadMoreEnabled() {
        return rowPerPage > 0;
    }

    public PaginationAdapter<T, H> refresh() {
        this.page = 1;
        this.lastPage = 0;
        this.currentPosition = 0;
        this.dataSet.clear();
        notifyDataSetChanged();
        return this;
    }

    public PaginationAdapter<T, H> endPagination() {
        lastPage = page;
        return this;
    }

    boolean hasNextPage() {
        return page > lastPage;
    }

    public List<T> getItems() {
        return dataSet;
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        this.container = (PaginationRecyclerView) recyclerView;
        if (!isLoadMoreEnabled()) {
            this.container.disableLoadMore();
        }
    }

    @Override
    public void onBindViewHolder(@NonNull H holder, int position) {
        currentPosition = holder.getAdapterPosition();
        holder.bindData(dataSet.get(position));
    }
}
