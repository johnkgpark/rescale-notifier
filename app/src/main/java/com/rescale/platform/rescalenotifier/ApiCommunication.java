package com.rescale.platform.rescalenotifier;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by johnpark on 12/19/14.
 */
public class ApiCommunication {
    public final String baseUrl = "http://192.168.128.236:8000";
    private String token = "";

    public ApiCommunication(String token) {
        this.token = token;
    }

    public ApiCommunication() {}

    public RestAdapter rescaleAdapter = new RestAdapter.Builder()
            .setEndpoint(baseUrl)
           .build();

    public interface LogInInterface {
        @POST("/api-token-auth/")
        void getToken(
            @Body User user,
            Callback<Token> callback
        );
    }

    public interface JobsInterface {
        @GET("/api/jobs/")
        void getJobs(
            @Header("Authorization") String token,
            @Query("t") int type,
            Callback<JsonObject> callback
        );
    }

    public interface StopJobInterface {
        @GET("/api/jobs/{jobId}/stop/")
        void getJobs(
            @Header("Authorization") String token,
            @Path("jobId") String jobId,
            Callback<JsonObject> callback
        );
    }

    public String getToken() {
        return this.token;
    }
}
