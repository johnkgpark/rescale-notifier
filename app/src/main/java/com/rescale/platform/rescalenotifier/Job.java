package com.rescale.platform.rescalenotifier;

/**
 * Created by johnpark on 12/19/14.
 */
public class Job {
    public String name;
    public String id;
    public String owner;
    public String clusterStatus;
    public String jobStatus;

    public Job() {
    }

    public Job(String name, String id, String owner) {
        this.name = name;
        this.id = id;
        this.owner = owner;
    }
}
