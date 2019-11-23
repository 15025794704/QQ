package com.aclass.android.qq;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.CompletionInfo;
import android.widget.TextView;

import com.aclass.android.qq.entity.User;
import com.aclass.android.qq.tools.MyDateBase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Administrator on 2019/11/23 0023.
 */

public class LinearAdapter extends RecyclerView.Adapter<LinearAdapter.LinearViewHolder> {

    private Context mContext;
    private List<String> list=new ArrayList<>();

    public LinearAdapter(Context context,List list){
        this.mContext=context;
        this.list=list;
    }
    @Override
    public LinearAdapter.LinearViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       LinearViewHolder linearViewHolder=  new LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.school_linear_item,parent,false));
        return linearViewHolder;
    }

    @Override
    public void onBindViewHolder(LinearAdapter.LinearViewHolder holder, int position) {
         holder.tvSchool.setText(list.get(position));
    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    class LinearViewHolder extends RecyclerView.ViewHolder{

        private TextView tvSchool,tvGrade;
        public LinearViewHolder(View itemView) {
            super(itemView);
            tvSchool=itemView.findViewById(R.id.tv_school);
            tvGrade=itemView.findViewById(R.id.tv_Grade);
        }
    }
}
