package com.rescale.platform.rescalenotifier;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by johnpark on 12/19/14.
 */

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Iterator;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by johnpark on 12/18/14.
 */
public class JobListAdapter extends ArrayAdapter<Job> {
    private final Context context;
    private ArrayList<Job> jobs;
    private static LayoutInflater inflater = null;
    SharedPreferenceService sps = new SharedPreferenceService();

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
        Button btnTerminate = (Button) rowView.findViewById(R.id.terminate_button);

        textJobName.setText(job.name);
        textJobStatus.setText(job.jobStatus);
        textClusterStatus.setText(job.clusterStatus);

        textJobName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ApiCommunication ac = new ApiCommunication();
                String url = ac.baseUrl + "/jobs/" + job.id + "/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                context.startActivity(i);
            }
        });

        if (job.clusterStatus.equals("Started")) {
            btnTerminate.setVisibility(View.VISIBLE);
            btnTerminate.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Activity activity = (Activity) context;
                    Toast.makeText(context, "Terminating...", Toast.LENGTH_SHORT).show();
                    String token = sps.getSharedPreferenceAsString(activity, "token");

                    ApiCommunication ac = new ApiCommunication();
                    ApiCommunication.StopJobInterface stopJobService = ac.rescaleAdapter.create(ApiCommunication.StopJobInterface.class);
                    Token t = new Token();
                    t.setToken(token);

                    stopJobService.getJobs(t.getToken(), job.id, new Callback<JsonObject>() {
                        @Override
                        public void success(JsonObject data, Response response) {
                            Toast.makeText(context, "Termination has been started.", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void failure(RetrofitError retrofitError) {
                            Toast.makeText(context, "The job could not be terminated. Try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }

        return rowView;
    }
}