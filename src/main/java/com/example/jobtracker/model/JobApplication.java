package com.example.jobtracker.model;

import java.io.Serializable;

/**
 * Represents a single job application entry.
 */
public class JobApplication implements Serializable {

    private int id;
    private String company;
    private String role;
    private String status; // Applied, Interview, Offer, Rejected
    private String appliedDate; // yyyy-MM-dd
    private String notes;
    private String priority = "Medium"; // Low, Medium, High
    private String jobUrl; // optional link to the job posting

    public JobApplication() {
    }

    public JobApplication(int id, String company, String role, String status, String appliedDate, String notes) {
        this(id, company, role, status, appliedDate, notes, "Medium", null);
    }

    public JobApplication(int id, String company, String role, String status, String appliedDate, String notes,
                           String priority, String jobUrl) {
        this.id = id;
        this.company = company;
        this.role = role;
        this.status = status;
        this.appliedDate = appliedDate;
        this.notes = notes;
        this.priority = priority;
        this.jobUrl = jobUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAppliedDate() {
        return appliedDate;
    }

    public void setAppliedDate(String appliedDate) {
        this.appliedDate = appliedDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getJobUrl() {
        return jobUrl;
    }

    public void setJobUrl(String jobUrl) {
        this.jobUrl = jobUrl;
    }

    @Override
    public String toString() {
        return "JobApplication{id=" + id + ", company='" + company + "', role='" + role +
                "', status='" + status + "', appliedDate='" + appliedDate + "', notes='" + notes +
                "', priority='" + priority + "', jobUrl='" + jobUrl + "'}";
    }
}
