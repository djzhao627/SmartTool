package com.djzhao.smarttool.activity.chooseproblem;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.djzhao.smarttool.R;
import com.djzhao.smarttool.activity.base.BaseActivity;
import com.djzhao.smarttool.adapter.chooseproblem.ItemListAdapter;
import com.djzhao.smarttool.db.chooseproblem.ChooseProblemItem;
import com.djzhao.smarttool.dialog.chooseproblem.AddItemDialog;

import org.litepal.crud.DataSupport;

import java.util.List;

public class ChooseProblemDetailListActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private Button backBtn;
    private TextView title, nothingHint, addBtn;

    private AddItemDialog addItemDialog;
    private int parentId;

    @Override
    protected void findViewById() {
        backBtn = $(R.id.title_layout_back_button);
        addBtn = $(R.id.title_layout_option_button);
        title = $(R.id.title_layout_title_text);
        nothingHint = $(R.id.choose_problem_no_item_hint);
        recyclerView = $(R.id.choose_problem_recycler_view);
    }

    @Override
    protected void initView() {
        backBtn.setOnClickListener(this);
        addBtn.setOnClickListener(this);
        addBtn.setVisibility(View.VISIBLE);
        addBtn.setText("添加");

        Intent intent = getIntent();
        parentId = intent.getIntExtra("parentId", 0);
        title.setText(intent.getStringExtra("title"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshMenuData();
    }

    private void refreshMenuData() {
        final List<ChooseProblemItem> items = DataSupport.where("parentId = ?", parentId + "").find(ChooseProblemItem.class);
        if (items == null || items.size() == 0) {
            nothingHint.setVisibility(View.VISIBLE);
        } else {
            nothingHint.setVisibility(View.GONE);
            final ItemListAdapter adapter = new ItemListAdapter(items, true);
            adapter.setOnItemClickListener(new ItemListAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, int data) {
                }
            });
            adapter.setOnItemLongClickListener(new ItemListAdapter.OnRecyclerItemLongListener() {
                @Override
                public void onItemLongClick(View view, final int position) {
                    new AlertDialog.Builder(mContext)
                            .setTitle("删除提示")
                            .setMessage("你确定需要删除:  " + items.get(position).getTitle())
                            .setPositiveButton("是的，删除", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DataSupport.delete(ChooseProblemItem.class, items.get(position).getId());
                                    items.remove(position);
                                    adapter.notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();
                }
            });
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_problem_list_activity);
        findViewById();
        initView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_layout_option_button:
                addItem();
                break;
            case R.id.title_layout_back_button:
                finish();
                break;
        }
    }

    private void addItem() {
        addItemDialog = new AddItemDialog(mContext);
        addItemDialog.setOnCancelClickedListener(new AddItemDialog.onCancelClickedListener() {
            @Override
            public void onClick() {
                addItemDialog.dismiss();
            }
        });
        addItemDialog.setOnAddClickedListener(new AddItemDialog.onAddClickedListener() {
            @Override
            public void onClick(String title) {
                ChooseProblemItem item = new ChooseProblemItem();
                item.setParentId(parentId);
                item.setTitle(title);
                if (item.save()) {
                    refreshMenuData();
                    addItemDialog.dismiss();
                }
            }
        });
        addItemDialog.show();
    }

}
