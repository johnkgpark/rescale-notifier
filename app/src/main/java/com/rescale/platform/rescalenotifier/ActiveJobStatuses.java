package com.rescale.platform.rescalenotifier;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class ActiveJobStatuses extends Activity {
    private ProgressBar spinner;
    private Token token = new Token();
    SharedPreferenceService sps = new SharedPreferenceService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_job_statuses);
        spinner = (ProgressBar)findViewById(R.id.progressBar2);
        spinner.setVisibility(View.GONE);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            token.setToken(extras.getString("token"));
            sps.setSharedPreferenceAsString(this, "token", extras.getString("token"));
        } else {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }

        getActiveJobs();
    }

    public void refreshActiveJobs(View view) {
        getActiveJobs();
    }

    public void getActiveJobs() {
        final Activity context = this;
        final ArrayList<Job> jobs = new ArrayList<Job>();

        spinner.setVisibility(View.VISIBLE);

        ApiCommunication ac = new ApiCommunication();
        ApiCommunication.JobsInterface jobsService = ac.rescaleAdapter.create(ApiCommunication.JobsInterface.class);

        jobsService.getJobs(token.getToken(), 1, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject data, Response response) {
                JsonArray results = data.getAsJsonArray("results");
                Iterator resultsIterator = results.iterator();

                while (resultsIterator.hasNext()) {
                    JsonObject tmpJsonObj = (JsonObject) resultsIterator.next();
                    Job job = new Job();
                    job.name = tmpJsonObj.get("name").getAsString();
                    job.id = tmpJsonObj.get("id").getAsString();
                    job.owner = tmpJsonObj.get("owner").getAsString();
                    JsonObject jobStatusObj = (JsonObject) tmpJsonObj.get("jobStatus");
                    job.jobStatus = jobStatusObj.get("content").getAsString();
                    JsonObject clusterStatusObj = (JsonObject) tmpJsonObj.get("clusterStatusDisplay");
                    job.clusterStatus = clusterStatusObj.get("content").getAsString();

                    jobs.add(job);
                }

                ListView listview = (ListView) context.findViewById(android.R.id.list);
                JobListAdapter adapter = new JobListAdapter(context, R.id.list_item, jobs);
                listview.setAdapter(adapter);

                spinner.setVisibility(View.GONE);

                Toast.makeText(context, "List has been updated.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                spinner.setVisibility(View.GONE);
                Toast.makeText(context, "Rescale could not be reached.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
