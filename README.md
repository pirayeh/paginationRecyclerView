# Pagination RecyclerView
a simple library for generic data pagination.



# How to Use

* Step 1: Add the JitPack repository to your build file

  Add it in your root build.gradle at the end of repositories:

```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

* step 2: Add the dependency to your module gradle 
```
dependencies {
    implementation 'androidx.recyclerview:recyclerview:1.1.0'
    implementation 'com.github.pirayeh:paginationRecyclerView:1.0.1'
}
```

# Example:

* step 1: Extends adapter

```
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
            txtRow = itemView.findViewById(R.id.txtRow);
        }

        @Override
        public void bindData(String data) {
            txtRow.setText(data);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null)
                mItemClickListener.onItemSelect(dataSet.get(getCurrentPosition()));
        }
    }
}
```


* step 2: initialize RecyclerView


```
private PaginationRecyclerView recyclerView;
private PaginationAdapter adapter;
```

```
recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
recyclerView.setStartLoadingFromLast(4);
recyclerView.setRowPerPage(row_per_page);
recyclerView.setOnLoadMoreListener(new PaginationRecyclerView.OnLoadMoreListener() {
    @Override
    public void onRequestLoadMore(int page, int rowPerPage) {
         //fetch data from server
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
          //play and pause loading
          //loadingView.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }
});

adapter = new MyAdapter(this, null);
recyclerView.setAdapter(adapter);
```

* step 3: set data to RecyclerView on get data from server
```
adapter.addAllItems(/*list of data*/);
```


♻ if need to clear all data from adapter and fetch again call the refresh() method from recyclerView :

```
recyclerView.refresh();
```


🖱 set item click listener
```
adapter.setOnItemClickListener(data -> {
  Toast.makeText(this, data.toString(), Toast.LENGTH_SHORT).show();
});
```

👀 See sample to better understanding.



# Version

* 1.0.1
