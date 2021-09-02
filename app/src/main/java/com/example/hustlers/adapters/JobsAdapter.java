package com.example.hustlers.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hustlers.R;
import com.example.hustlers.models.JobModel;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class JobsAdapter extends RecyclerView.Adapter<JobsAdapter.ViewHolder> {

    private Context mContext;
    private List<JobModel> mList = new ArrayList<>();

    private List<DataHolder> mDataHolder = new ArrayList<>();

    private ClickListener clickListener;

    public JobsAdapter(Context context, List<JobModel> list, ClickListener clickListener) {
        this.mContext = context;
        this.mList = list;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public JobsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.single_job_view, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobsAdapter.ViewHolder holder, int position) {

        JobModel jobs = mList.get(position);

        holder.job_desc_tv.setText(jobs.getJob_description());
        holder.salary_range_tv.setText(jobs.getJob_salary());
        holder.closing_date_tv.setText(jobs.getJob_date());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private MaterialTextView job_desc_tv;
        private MaterialTextView salary_range_tv;
        private MaterialTextView closing_date_tv;
        private MaterialTextView view_job;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            job_desc_tv = itemView.findViewById(R.id.job_tv);
            salary_range_tv = itemView.findViewById(R.id.salary_tv);
            closing_date_tv = itemView.findViewById(R.id.closing_date_tv);
            view_job = itemView.findViewById(R.id.view_job_tv);

            view_job.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            JobModel model = mList.get(getAdapterPosition());

            if (view.getId() == view_job.getId()) {
                clickListener.viewJob(model.getJob_id(),model.getJob_description(),model.getJob_date());
            }
        }
    }

    public interface ClickListener {
        void viewJob(String pos, String title, String date);
    }

    public void updateList(List<DataHolder> list){
        mDataHolder = list;
        notifyDataSetChanged();
    }
}
