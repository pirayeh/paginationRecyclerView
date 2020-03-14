package ir.pirayeh1485.paginationrecyclerview.sample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

import ir.pirayeh1485.paginationrecyclerview.PaginationAdapter;
import ir.pirayeh1485.paginationrecyclerview.PaginationViewHolder;

class MyAdapter extends PaginationAdapter<String, MyAdapter.SampleViewHolder> {

    MyAdapter(Context context, List<String> dataSet) {
        super(context, dataSet);
    }

    @NonNull
    @Override
    public SampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_row, parent, false);
        return new SampleViewHolder(view);
    }


    class SampleViewHolder extends PaginationViewHolder<String> {

        private TextView txtRow;

        SampleViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            txtRow = itemView.findViewById(R.id.txtRow);
        }

        @Override
        public void bindData(String data) {
            txtRow.setText(data);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null)
                mItemClickListener.onItemSelect(dataSet.get(getAdapterPosition()));
        }
    }
}
