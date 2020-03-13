package ir.pirayeh1485.paginationrecyclerview;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class PaginationViewHolder<T> extends RecyclerView.ViewHolder implements View.OnClickListener {

    public PaginationViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public abstract void bindData(T data);
}
