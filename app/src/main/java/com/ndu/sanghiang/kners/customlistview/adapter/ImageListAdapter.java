package com.ndu.sanghiang.kners.customlistview.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.ndu.sanghiang.kners.R;
import com.ndu.sanghiang.kners.customlistview.model.Image;
import com.ndu.sanghiang.kners.projecttrackerfi.fragment.TemaFragment;

import java.util.List;

public class ImageListAdapter extends ArrayAdapter<Image> {

    private List<Image> customListProject;
    private Context mContext;
    private int resource;
    private DatabaseReference projectRef;


    public ImageListAdapter(Context mContext, int resource, List<Image> customListProject) {
        super(mContext, resource, customListProject);
        this.mContext = mContext;
        this.resource = resource;
        this.customListProject = customListProject;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        @SuppressLint("ViewHolder") View view = layoutInflater.inflate(resource, null, false);

        LinearLayout listItem = view.findViewById(R.id.customListProject);
        ImageView listImage = view.findViewById(R.id.imageProject);
        TextView listImageName = view.findViewById(R.id.textProjectName);
        TextView listStatus = view.findViewById(R.id.textStatus);
        TextView listCreated = view.findViewById(R.id.textCreated);
        TextView listPercentage = view.findViewById(R.id.textPercentage);
        ProgressBar statuspBar = view.findViewById(R.id.statusPBar);
        TextView listPid = view.findViewById(R.id.textPid);
        // Firebase
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        // get current userId
        String userId = mAuth.getCurrentUser().getUid();
        projectRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("projects");

        final Image image = customListProject.get(position);

        listItem.setOnClickListener(view1 -> {
            Snackbar.make(view1, "Click On " + image.getPid(), Snackbar.LENGTH_LONG)
                    .setAction("No action", null).show();
            //Pass data to fragment
            if (image.getPid() != null) {
                Bundle bundle = new Bundle();
                //String title = projectRef.child("pid").getKey(); //pid
                //String pid = projectRef.child("project_title").getKey(); //project_title
                String title = projectRef.child("pid").toString();
                String pid = projectRef.child("project_title").toString();
                String TAG = "Nandur93";
                Log.i(TAG, title+" "+pid);
                bundle.putString("judul_thema", title);
                bundle.putString("pid", pid);
                // set Fragmentclass Arguments
                TemaFragment fragobj = new TemaFragment();
                fragobj.setArguments(bundle);
            }
        });

        listItem.setOnLongClickListener(view1 -> {
            DatabaseReference projectRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("projects");
            Query queryRef = projectRef.orderByChild("project_title").equalTo(image.getName());
            queryRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String previousChild) {
                    dataSnapshot.getRef().setValue(null);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            return true;
        });

        listImage.setImageDrawable(mContext.getResources().getDrawable(image.getImage()));
        listImageName.setText(image.getName());
        listStatus.setText(image.getStatus());
        listCreated.setText(image.getCreated());
        listPercentage.setText(String.format("%s%%", image.getPercent()));
        listPid.setText(image.getPid());
        float percent = image.getPercent();
        statuspBar.setProgress((int) percent);
        return view;
    }

}
