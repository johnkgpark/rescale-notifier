package com.rescale.platform.rescalenotifier;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

/**
 * Created by johnpark on 12/19/14.
 */

import java.util.ArrayList;

/**
 * Created by johnpark on 12/18/14.
 */
public class JobListAdapter extends ArrayAdapter<Job> {
    private final Context context;
    private ArrayList<Job> jobs;
    private static LayoutInflater inflater = null;

    public JobListAdapter(Context context, int viewResourceId, ArrayList<Job> _jobs) {
        super(context, viewResourceId, _jobs);
        this.context = context;
        this.jobs = _jobs;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = inflater.inflate(R.layout.list_item, parent, false);
        final Job job = jobs.get(position);

        TextView textJobName = (TextView) rowView.findViewById(R.id.textJobName);
        TextView textJobStatus = (TextView) rowView.findViewById(R.id.textJobStatus);
        TextView textClusterStatus = (TextView) rowView.findViewById(R.id.textClusterStatus);

        textJobName.setText(job.name);
        textJobStatus.setText(job.jobStatus);
        textClusterStatus.setText(job.clusterStatus);

        RelativeLayout rl = (RelativeLayout)rowView.findViewById(R.id.rlContainer);

        if (job.jobStatus == "Executing") {
            rl.setBackgroundColor(context.getResources().getColor(R.color.executing));
        }

        if (job.jobStatus == "Started") {
            rl.setBackgroundColor(context.getResources().getColor(R.color.started));
        }

        if (job.jobStatus == "Validated") {
            rl.setBackgroundColor(context.getResources().getColor(R.color.validated));
        }

        rl.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                ApiCommunication ac = new ApiCommunication();
                String url = ac.baseUrl + "/jobs/" + job.id + "/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                context.startActivity(i);
            }
        });

        return rowView;
    }
}