package com.example.hustlers.models;

public class ApplicationModel {

    String applicantId;
    String job_key;
    String job_title;
    String job_date;


    public ApplicationModel(String applicantId,String job_key, String job_title, String job_date) {
        this.applicantId = applicantId;
        this.job_key = job_key;
        this.job_title = job_title;
        this.job_date = job_date;
    }

    public ApplicationModel() {
        //empty constructor
    }

    public String getJob_key() {
        return job_key;
    }

    public void setJob_key(String job_key) {
        this.job_key = job_key;
    }

    public String getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(String applicantId) {
        this.applicantId = applicantId;
    }

    public String getJob_title() {
        return job_title;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }

    public String getJob_date() {
        return job_date;
    }

    public void setJob_date(String job_date) {
        this.job_date = job_date;
    }
}
