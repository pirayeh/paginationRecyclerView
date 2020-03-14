package ir.pirayeh1485.paginationrecyclerview.sample;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

import ir.pirayeh1485.paginationrecyclerview.PaginationAdapter;
import ir.pirayeh1485.paginationrecyclerview.PaginationRecyclerView;

public class MainActivity extends AppCompatActivity {

    private static final int TOTAL_DATA_COUNT = 160;
    private int row_per_page = 25;

    private PaginationRecyclerView recyclerView;
    private PaginationAdapter adapter;

    private View loading;

    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeRefresh = findViewById(R.id.swipeRefresh);
        loading = findViewById(R.id.loading);
        recyclerView = findViewById(R.id.mainRecycler);

        initRecyclerView();

        initSwipeRefresh();

        requestData(1);
    }

    private void initSwipeRefresh() {
        swipeRefresh.setOnRefreshListener(() -> {
            recyclerView.refresh();
            requestData(1);
        });
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setStartLoadingFromLast(4);
        recyclerView.setRowPerPage(row_per_page);
        recyclerView.setOnLoadMoreListener(new PaginationRecyclerView.OnLoadMoreListener() {
            @Override
            public void onRequestLoadMore(int page, int rowPerPage) {
                requestData(page);
            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
                loading.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            }
        });

        adapter = new MyAdapter(this, null);
        recyclerView.setAdapter(adapter);
    }


    private void requestData(int page) {

        new Thread(() -> {

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            ArrayList<String> data = new ArrayList<>();
            for (int i = 1; i <= row_per_page; i++) {
                int releasedDataCount = i + ((page - 1) * row_per_page);
                if (releasedDataCount > TOTAL_DATA_COUNT)
                    break;

                data.add("Row " + releasedDataCount);
            }

            runOnUiThread(() -> {
                adapter.addAllItems(data);
                swipeRefresh.setRefreshing(false);
            });

        }).start();
    }
}
