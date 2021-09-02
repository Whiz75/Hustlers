package com.example.hustlers.models;

public class ApplicationModel {

    String applicantId;


    public ApplicationModel(String applicantId) {
        this.applicantId = applicantId;
    }

    public ApplicationModel() {
        //empty constructor
    }

    public String getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(String applicantId) {
        this.applicantId = applicantId;
    }
}
