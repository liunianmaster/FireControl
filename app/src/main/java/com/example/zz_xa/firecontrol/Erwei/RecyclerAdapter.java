package com.example.zz_xa.firecontrol.Erwei;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zz_xa.firecontrol.R;
import com.example.zz_xa.firecontrol.WwebView;

/**
 * Created by ZZ-XA of wxb on 2018/4/17.
 * Fix by:
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.AuthorViewHolder> {

    @Override
    public RecyclerAdapter.AuthorViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View childView = inflater.inflate(R.layout.item, parent, false);
        AuthorViewHolder viewHolder = new AuthorViewHolder(childView);
/*
        LinearLayout linearLayout = (LinearLayout) childView.findViewById(R.id.erwei_item);
        linearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(v.,WwebView.class);
                startActivity(intent);
                return false;
            }
        });
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(this,WwebView.class);

            }
        });
        */

        return viewHolder;
    }

    public static void removeData(int position) {

    }

    //AdapterView.
    public interface OnItemClickListener{
        void onItemClick(View view,int position);
        void onItemLongClick(View view, int position);
    }
    private OnItemClickListener mOnItemClickListener;

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public void onBindViewHolder(final RecyclerAdapter.AuthorViewHolder holder, final int position) {
        int a= position;
        if(position%2 == 1 ) {
            holder.mNickNameView.setText("新银泰");
            holder.mMottoView.setText("合肥市 新银泰道路\n习近平在重庆代表团参加审议");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    Intent intent = new Intent();
                    intent.setClass(v.getContext(), WwebView.class);
                    intent.putExtra("title","新银泰");
                    v.getContext().startActivity(intent);
                   // v.getContext().startActivity(new Intent(v.getContext(), WwebView.class));
                }
            });
        }else {
            holder.mNickNameView.setText("冠军建材有限公司");
            holder.mMottoView.setText("冠军建材集团创建于1972年的台湾，发展至今，总的资产...");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    Intent intent = new Intent();
                    intent.setClass(v.getContext(), WwebView.class);
                    intent.putExtra("title","冠军建材有限公司");
                    v.getContext().startActivity(intent);

                   // v.getContext().startActivity(new Intent(v.getContext(), WwebView.class));
                }
            });
        }


        if(mOnItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView,pos);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemLongClick(holder.itemView,pos);
                    return false;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return 15;
    }


    public class AuthorViewHolder extends RecyclerView.ViewHolder{
        TextView mNickNameView;
        TextView mMottoView;
        public AuthorViewHolder(View itemView) {
            super(itemView);

            mNickNameView = (TextView) itemView.findViewById(R.id.tv_nickname);
            mMottoView = (TextView) itemView.findViewById(R.id.tv_motto);
            mNickNameView.setText("新银泰");
            mMottoView.setText("合肥市 新银泰道路\n习近平在重庆代表团参加审议。会上庆代表团参加");

        }

    }

}
