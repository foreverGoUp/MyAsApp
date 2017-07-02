

package com.kc.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.daShen.recyclerView.itemDecorationIml.DividerGridItemDecoration;
import com.daShen.recyclerView.tool.RVTestAdapter;
import com.kc.myasapp.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewTestActivity extends AppCompatActivity {

    private RecyclerView mRv;
    private List<String> mList;
    private RVTestAdapter mAdapter;

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
        //实现Gridview
//        mRv.setLayoutManager(new GridLayoutManager(this, 2));
        //实现水平/垂直滑动Gridview,水平滚动listview，瀑布流gridview
        mRv.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        //增加Gridview的分割线
        mRv.addItemDecoration(new DividerGridItemDecoration(this));
        //添加增加删除item动画
        mRv.setItemAnimator(new DefaultItemAnimator());
        //初始化适配器
        mAdapter = new RVTestAdapter(this, mList);
        initAdapter(mAdapter);
        mRv.setAdapter(mAdapter);
    }

    private void initAdapter(RVTestAdapter adapter) {
        adapter.setOnItemClickLitener(new RVTestAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(RecyclerViewTestActivity.this, position + " click",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(RecyclerViewTestActivity.this, position + "long click",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_rv_test, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.id_action_add:
                mAdapter.addData(1);
                break;
            case R.id.id_action_delete:
                mAdapter.removeData(1);
                break;
        }
        return true;
    }
}
