package com.deepak.railscommits;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Sharma on 6/2/2016.
 */
public class CommitsAdapter  extends RecyclerView.Adapter<CommitsAdapter.MyViewHolder>  {
    private Context context;
    public MyOnclickListener myOnclickListener;
    private ArrayList<CommitsData> commitsList;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView userName, commit_number;
        public ImageView like;
        public Button message;
        public MyViewHolder(View view) {
            super(view);
            userName = (TextView) view.findViewById(R.id.userName);
            commit_number= (TextView) view.findViewById(R.id.commit_number);
            message= (Button) view.findViewById(R.id.message);
            like = (ImageView) view.findViewById(R.id.like);
        }
    }


    public CommitsAdapter(ArrayList<CommitsData> commitsList, Context ctx) {
        this.commitsList = commitsList;
        this.context = ctx;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row, parent, false);

        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        CommitsData commitsData = commitsList.get(position);
        final int p=position;
        holder.userName.setText(commitsData.getUserName());
        holder.commit_number.setText(commitsData.getCommitNumber());
        holder.like.setImageResource(R.drawable.heart_off);
        if(!commitsData.isLiked())
        {
            holder.like.setImageResource(R.drawable.heart_off);
        }
        else
        {
            holder.like.setImageResource(R.drawable.heart_on);
        }
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!commitsList.get(p).isLiked())
                {
                    commitsList.get(p).setLiked(true);
                    holder.like.setImageResource(R.drawable.heart_on);
                }
                else
                {
                    commitsList.get(p).setLiked(false);
                    holder.like.setImageResource(R.drawable.heart_off);
                }
            }
        });
        holder.message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myOnclickListener = ((MainActivity)context);
                myOnclickListener.openMessage(p);
            }
        });
    }
    public interface MyOnclickListener {
        public void openMessage(int position);
    }
    @Override
    public int getItemCount() {
        return commitsList.size();
    }
    public void setFilter(ArrayList<CommitsData> commitsListFiltered){
        commitsList = new ArrayList<>();
        commitsList.addAll(commitsListFiltered);
        notifyDataSetChanged();
    }
}
