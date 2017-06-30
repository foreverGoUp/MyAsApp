

package com.kc.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daShen.recyclerView.itemDecorationIml.DividerGridItemDecoration;
import com.kc.myasapp.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewTest extends AppCompatActivity {

    private RecyclerView mRv;
    private List<String> mList;
    private HomeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_test);

        mRv = (RecyclerView) findViewById(R.id.rv);
        mList = new ArrayList<>();
        for (int i = 'A'; i < 'Z'; i++) {
            mList.add("" + (char) i);
        }
        //实现listview
//        mRv.setLayoutManager(new LinearLayoutManager(this));
        //增加listview的分割线
//        mRv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRv.setLayoutManager(new GridLayoutManager(this, 2));
        mRv.addItemDecoration(new DividerGridItemDecoration(this));
        mRv.setAdapter(mAdapter = new HomeAdapter());
    }

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from(RecyclerViewTest.this).inflate(R.layout.item_recycler_view, parent, false));
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.mTv.setText(mList.get(position));
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            private TextView mTv;

            public MyViewHolder(View itemView) {
                super(itemView);
                mTv = (TextView) itemView.findViewById(R.id.tv_item_rv);
            }
        }
    }
}
