package ir.pirayeh1485.paginationrecyclerview;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.Collections;

public final class PaginationRecyclerView extends RecyclerView {
    public static final String TAG = "PaginationRecyclerView";

    public static final int END = -1;

    private int startLoadingFromLast = END;
    private boolean loading = false;
    private OnLoadMoreListener mLoadMoreListener;
    private int mRowPerPage;

    private ItemTouchHelper.Callback mCallback;

    public PaginationRecyclerView(Context context) {
        super(context);
        init();
    }

    public PaginationRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    public PaginationRecyclerView setDragSwipeHelper() {
        new DragSwipeHelper(initCallback()).attachToRecyclerView(this);
        return this;
    }

    private ItemTouchHelper.Callback initCallback() {

        if (mCallback != null)
            return mCallback;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;

        if ((getLayoutManager() instanceof LinearLayoutManager && ((LinearLayoutManager) getLayoutManager()).getOrientation() == LinearLayoutManager.HORIZONTAL) ||
                (getLayoutManager() instanceof StaggeredGridLayoutManager && ((StaggeredGridLayoutManager) getLayoutManager()).getOrientation() == StaggeredGridLayoutManager.HORIZONTAL)) {
            swipeFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            dragFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        }
        mCallback = new ItemTouchHelper.SimpleCallback(dragFlags, swipeFlags) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                if (getAdapter() != null) {
                    Collections.swap(((PaginationAdapter) getAdapter()).dataSet, viewHolder.getAdapterPosition(), target.getAdapterPosition());
                    // and notify the adapter that its dataset has changed
                    getAdapter().notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                }
                return getAdapter() != null;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (getAdapter() != null) {
                    ((PaginationAdapter) getAdapter()).removeItem(viewHolder.getAdapterPosition());
                }
            }
        };

        return mCallback;
    }


    private void init() {
        if (isInEditMode())
            return;

        addOnScrollListener(new EndlessScrollListener());
    }

    public void setStartLoadingFromLast(int count) {
        this.startLoadingFromLast = count;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
        if (mLoadMoreListener != null) {
            mLoadMoreListener.onLoadingChanged(loading && !canScrollVertically(1));
        }
    }

    public boolean isLoading() {
        return this.loading;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (!isInEditMode()) {
            if (!(adapter instanceof PaginationAdapter))
                throw new RuntimeException("adapter not instance of paginationrecyclerview.PaginationAdapter");
            if (mRowPerPage <= 0) {
                mRowPerPage = ((PaginationAdapter) adapter).getRowPerPage();
            } else if (((PaginationAdapter) adapter).getRowPerPage() == PaginationAdapter.DEFAULT_ROW_PER_PAGE)
                ((PaginationAdapter) adapter).setRowPerPage(mRowPerPage);
            setLoading(false);
        }
        super.setAdapter(adapter);
    }


    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        this.mLoadMoreListener = listener;
    }

    public void setRowPerPage(int rowPerPage) {
        if (getAdapter() != null)
            ((PaginationAdapter) getAdapter()).setRowPerPage(rowPerPage);
        mRowPerPage = rowPerPage;
    }

    public void refresh() {
        setLoading(false);
        if (getAdapter() != null)
            ((PaginationAdapter) getAdapter()).refresh();
    }

    public boolean hasNextPage() {
        return getAdapter() != null && ((PaginationAdapter) getAdapter()).hasNextPage();
    }

    public int getCurrentPosition() {
        if (getAdapter() != null)
            return ((PaginationAdapter) getAdapter()).getCurrentPosition();

        return 0;
    }

    public int getRowPerPage() {
        if (getAdapter() == null)
            return mRowPerPage;

        return ((PaginationAdapter) getAdapter()).getRowPerPage();
    }


    public int getPage() {
        if (getAdapter() != null)
            return ((PaginationAdapter) getAdapter()).getPage();

        return 0;
    }

    public int getLastPage() {
        if (getAdapter() != null)
            return ((PaginationAdapter) getAdapter()).getLastPage();

        return -1;
    }

    public int getItemCount() {
        if (getAdapter() != null)
            return getAdapter().getItemCount();

        return 0;
    }

    public void endPagination() {
        if (getAdapter() == null)
            throw new RuntimeException("adapter not set");

        ((PaginationAdapter) getAdapter()).endPagination();
    }

    public void disableLoadMore() {
        mRowPerPage = PaginationAdapter.ROW_PER_PAGE_NO_LOADING;
    }

    public interface OnLoadMoreListener {
        void onRequestLoadMore(int page, int rowPerPage);

        void onLoadingChanged(boolean isLoading);
    }

    boolean isEndBottom;

    private final class EndlessScrollListener extends OnScrollListener {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (mRowPerPage == PaginationAdapter.ROW_PER_PAGE_NO_LOADING) {
                return;
            }

            isEndBottom = !recyclerView.canScrollVertically(1);

            if (getItemCount() >= mRowPerPage && isEndBottom)
                setLoading(isLoading());

            if (!isLoading() &&
                    getItemCount() >= mRowPerPage &&
                    ((isEndBottom && startLoadingFromLast == END) ||
                            (isEndBottom && startLoadingFromLast >= getItemCount()) ||
                            getItemCount() - getCurrentPosition() <= startLoadingFromLast) &&
                    hasNextPage()) {

                setLoading(true);

                //onScrolledToTop
                if (mLoadMoreListener != null) {
                    mLoadMoreListener.onLoadingChanged(isEndBottom);
                    mLoadMoreListener.onRequestLoadMore(getPage(), getRowPerPage());
                }

            }
        }
    }
}
