package com.example.jobtracker.action;

import com.opensymphony.xwork2.ActionSupport;
import com.example.jobtracker.dao.JobApplicationDAO;
import com.example.jobtracker.model.JobApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * Handles CRUD actions for JobApplication: list (with search/sort/dashboard stats),
 * add form, save (create/update), edit, delete.
 */
public class JobApplicationAction extends ActionSupport {

    private static final Logger LOG = LoggerFactory.getLogger(JobApplicationAction.class);

    private int id;
    private JobApplication jobApplication = new JobApplication();
    private Collection<JobApplication> jobApplications;

    // Search / filter / sort (bound automatically from request query params)
    private String companyFilter;
    private String statusFilter;
    private String sortBy = "appliedDate";
    private String sortDir = "desc";
    private String msg;

    // Dashboard counts (computed over the full, unfiltered data set)
    private int totalCount;
    private int appliedCount;
    private int interviewCount;
    private int offerCount;
    private int rejectedCount;

    public String list() {
        LOG.info("Handling request: list job applications (companyFilter='{}', statusFilter='{}', sortBy='{}', sortDir='{}')",
                companyFilter, statusFilter, sortBy, sortDir);

        Collection<JobApplication> all = JobApplicationDAO.findAll().values();
        computeDashboardCounts(all);

        List<JobApplication> filtered = new ArrayList<>();
        for (JobApplication app : all) {
            boolean matchesCompany = isBlank(companyFilter)
                    || app.getCompany() != null && app.getCompany().toLowerCase().contains(companyFilter.toLowerCase());
            boolean matchesStatus = isBlank(statusFilter) || statusFilter.equals(app.getStatus());
            if (matchesCompany && matchesStatus) {
                filtered.add(app);
            }
        }

        Comparator<JobApplication> comparator = buildComparator(sortBy);
        if ("desc".equalsIgnoreCase(sortDir)) {
            comparator = comparator.reversed();
        }
        filtered.sort(comparator);

        jobApplications = filtered;
        LOG.debug("Returning {} of {} job application(s) to list.jsp after filtering/sorting", filtered.size(), all.size());
        return "list";
    }

    /**
     * Used by the sortable column headers in list.jsp: if the given column is the
     * currently active sort column, returns the opposite of the current direction
     * (to toggle asc/desc on next click); otherwise defaults to ascending.
     */
    public String nextSortDir(String column) {
        if (column != null && column.equalsIgnoreCase(sortBy) && "asc".equalsIgnoreCase(sortDir)) {
            return "desc";
        }
        return "asc";
    }

    public String input() {
        if (id > 0) {
            LOG.info("Handling request: load job application id={} for editing", id);
            jobApplication = JobApplicationDAO.findById(id);
        } else {
            LOG.info("Handling request: show blank form to add a new job application");
        }
        return "input";
    }

    public String save() {
        boolean isNew = jobApplication.getId() == 0;
        LOG.info("Handling request: save job application (id={}, isNew={})", jobApplication.getId(), isNew);
        JobApplicationDAO.save(jobApplication);
        msg = isNew ? "created" : "updated";
        return "toList";
    }

    public String delete() {
        LOG.info("Handling request: delete job application id={}", id);
        JobApplicationDAO.delete(id);
        msg = "deleted";
        return "toList";
    }

    private void computeDashboardCounts(Collection<JobApplication> all) {
        totalCount = all.size();
        appliedCount = 0;
        interviewCount = 0;
        offerCount = 0;
        rejectedCount = 0;
        for (JobApplication app : all) {
            if (app.getStatus() == null) {
                continue;
            }
            switch (app.getStatus()) {
                case "Applied":
                    appliedCount++;
                    break;
                case "Interview":
                    interviewCount++;
                    break;
                case "Offer":
                    offerCount++;
                    break;
                case "Rejected":
                    rejectedCount++;
                    break;
                default:
                    break;
            }
        }
    }

    private Comparator<JobApplication> buildComparator(String field) {
        String key = field == null ? "" : field;
        switch (key) {
            case "company":
                return Comparator.comparing(JobApplication::getCompany, Comparator.nullsFirst(String.CASE_INSENSITIVE_ORDER));
            case "role":
                return Comparator.comparing(JobApplication::getRole, Comparator.nullsFirst(String.CASE_INSENSITIVE_ORDER));
            case "status":
                return Comparator.comparing(JobApplication::getStatus, Comparator.nullsFirst(String.CASE_INSENSITIVE_ORDER));
            case "priority":
                return Comparator.comparingInt(app -> priorityRank(app.getPriority()));
            case "appliedDate":
            default:
                return Comparator.comparing(JobApplication::getAppliedDate, Comparator.nullsFirst(Comparator.naturalOrder()));
        }
    }

    private int priorityRank(String priority) {
        if ("High".equalsIgnoreCase(priority)) {
            return 3;
        }
        if ("Medium".equalsIgnoreCase(priority)) {
            return 2;
        }
        if ("Low".equalsIgnoreCase(priority)) {
            return 1;
        }
        return 0;
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    /** Derived, human-readable text for the flash banner shown after create/update/delete. */
    public String getFlashMessage() {        if (msg == null) {
            return null;
        }
        switch (msg) {
            case "created":
                return "Job application added successfully.";
            case "updated":
                return "Job application updated successfully.";
            case "deleted":
                return "Job application deleted.";
            default:
                return null;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public JobApplication getJobApplication() {
        return jobApplication;
    }

    public void setJobApplication(JobApplication jobApplication) {
        this.jobApplication = jobApplication;
    }

    public Collection<JobApplication> getJobApplications() {
        return jobApplications;
    }

    /** Safe emptiness check exposed as a getter, avoiding OGNL method calls on nested objects in JSP. */
    public boolean isEmptyResult() {
        return jobApplications == null || jobApplications.isEmpty();
    }

    public String getCompanyFilter() {
        return companyFilter;
    }

    public void setCompanyFilter(String companyFilter) {
        this.companyFilter = companyFilter;
    }

    public String getStatusFilter() {
        return statusFilter;
    }

    public void setStatusFilter(String statusFilter) {
        this.statusFilter = statusFilter;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortDir() {
        return sortDir;
    }

    public void setSortDir(String sortDir) {
        this.sortDir = sortDir;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getAppliedCount() {
        return appliedCount;
    }

    public int getInterviewCount() {
        return interviewCount;
    }

    public int getOfferCount() {
        return offerCount;
    }

    public int getRejectedCount() {
        return rejectedCount;
    }
}
