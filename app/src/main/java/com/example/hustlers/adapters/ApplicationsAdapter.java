package com.example.hustlers.adapters;

import android.annotation.SuppressLint;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hustlers.R;
import com.example.hustlers.models.ApplicationModel;
import com.example.hustlers.models.JobModel;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ApplicationsAdapter extends RecyclerView.Adapter<ApplicationsAdapter.ViewHolder> {

    private final List<JobModel> postList;
    private final Context context;
    private FragmentManager fm;
    private final ClickListener listener1;

    public ApplicationsAdapter(Context context, List<JobModel> postsList, ClickListener listener1) {
        this.postList = postsList;
        this.context = context;
        this.listener1 = listener1;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.application_item_row, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        JobModel jobs = postList.get(position);

        holder.job_title_tv.setText(jobs.getJob_location());
        holder.job_date_tv.setText(jobs.getJob_date());

    }

    @Override
    public int getItemCount()
    {
        return postList.size();
    }

    View mView;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private MaterialTextView job_title_tv;
        private MaterialTextView job_date_tv;
        private MaterialButton btnRemove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;

            job_title_tv = itemView.findViewById(R.id.tv_job_title);
            job_date_tv = itemView.findViewById(R.id.tv_job_date);
            btnRemove = itemView.findViewById(R.id.remove_application_btn);

            btnRemove.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            JobModel j = postList.get(getAdapterPosition());

            if (view.getId() == btnRemove.getId()){
                listener1.DeleteJobClick(j.getJob_id());
            }
        }
    }

    private void likeListener(String postId, ImageView imageView)
    {
        FirebaseFirestore
                .getInstance()
                .collection("Posts/" + postId + "/Likes")
                .document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {

                        if(!task.getResult().exists()){

                            Map<String, Object> likesMap = new HashMap<>();
                            likesMap.put("time", FieldValue.serverTimestamp());

                            FirebaseFirestore
                                    .getInstance()
                                    .collection("Posts/" + postId + "/Likes").document(FirebaseAuth.getInstance().getUid())
                                    .set(likesMap);

                            Toast.makeText(mView.getContext(), "liked", Toast.LENGTH_LONG).show();

                        } else {
                            FirebaseFirestore
                                    .getInstance()
                                    .collection("Posts/" + postId + "/Likes").document(FirebaseAuth.getInstance().getUid())
                                    .delete();

                            Toast.makeText(mView.getContext(), "Unliked", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public interface ClickListener
    {
        //void clickedLikes(int pos);
        void DeleteJobClick(String pos);
    }
}
