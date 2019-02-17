package com.djzhao.smarttool.adapter.chooseproblem;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.djzhao.smarttool.R;
import com.djzhao.smarttool.db.chooseproblem.ChooseProblemItem;

import java.util.List;

/**
 * Created by djzhao on 18/05/06.
 */
public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolder> {

    private Context mContext;

    private List<ChooseProblemItem> items;

    private static boolean isMenu;

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private OnRecyclerItemLongListener mOnItemLongClickListener = null;

    public ItemListAdapter(List<ChooseProblemItem> items, boolean isMenu) {
        this.items = items;
        this.isMenu = isMenu;
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private OnRecyclerViewItemClickListener mOnItemClickListener = null;
        private OnRecyclerItemLongListener mOnItemLongClickListener = null;
        LinearLayout layout;

        TextView title;

        public ViewHolder(View itemView, OnRecyclerViewItemClickListener mClickListener, OnRecyclerItemLongListener mLongClickListener) {
            super(itemView);
            this.mOnItemClickListener = mClickListener;
            this.mOnItemLongClickListener = mLongClickListener;
            layout = (LinearLayout) itemView;
            title = itemView.findViewById(R.id.choose_problem_list_item_text);
            if (isMenu) {
                layout.setOnClickListener(this);
            }
            layout.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (mOnItemLongClickListener != null) {
                mOnItemLongClickListener.onItemLongClick(v, getAdapterPosition());
            }
            return false;
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.choose_problem_list_item, parent, false);
        return new ViewHolder(view, mOnItemClickListener, mOnItemLongClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(items.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    //define interface
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int data);

    }

    public interface OnRecyclerItemLongListener {
        void onItemLongClick(View view, int position);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnRecyclerItemLongListener listener) {
        this.mOnItemLongClickListener = listener;
    }

}
