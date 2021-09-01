package com.example.hustlers.models;

public class JobModel {

    String job_id;
    String job_experience;
    String job_description;
    String job_date;
    String job_location;
    String job_qualification;
    String job_salary;

    public JobModel() {
       //empty constructor
    }

    public JobModel(String job_experience, String job_description, String job_date,
                    String job_location, String job_qualification,String job_salary, String job_id) {
        this.job_experience = job_experience;
        this.job_description = job_description;
        this.job_date = job_date;
        this.job_location = job_location;
        this.job_qualification = job_qualification;
        this.job_salary = job_salary;
        this.job_id = job_id;
    }

    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

    public String getJob_experience() {
        return job_experience;
    }

    public void setJob_experience(String job_experience) {
        this.job_experience = job_experience;
    }

    public String getJob_description() {
        return job_description;
    }

    public void setJob_description(String job_description) {
        this.job_description = job_description;
    }

    public String getJob_date() {
        return job_date;
    }

    public void setJob_date(String job_date) {
        this.job_date = job_date;
    }

    public String getJob_location() {
        return job_location;
    }

    public void setJob_location(String job_location) {
        this.job_location = job_location;
    }

    public String getJob_qualification() {
        return job_qualification;
    }

    public void setJob_qualification(String job_qualification) {
        this.job_qualification = job_qualification;
    }

    public String getJob_salary() {
        return job_salary;
    }

    public void setJob_salary(String job_salary) {
        this.job_salary = job_salary;
    }
}
