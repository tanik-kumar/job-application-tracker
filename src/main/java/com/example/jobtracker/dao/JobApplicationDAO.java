package com.example.jobtracker.dao;

import com.example.jobtracker.model.JobApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Simple in-memory store for JobApplication records.
 * Data is lost on server restart - swap this class out for a real DB later.
 */
public class JobApplicationDAO {

    private static final Logger LOG = LoggerFactory.getLogger(JobApplicationDAO.class);

    private static final Map<Integer, JobApplication> STORE = new LinkedHashMap<>();
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(0);

    // Seed with a couple of sample rows so the list page isn't empty on first run.
    static {
        save(new JobApplication(0, "Acme Corp", "Backend Developer", "Applied", "2026-09-20",
                "Referred by a friend", "High", "https://acme.example.com/careers/backend-dev"));
        save(new JobApplication(0, "Globex", "Full Stack Engineer", "Interview", "2026-09-15",
                "Round 2 scheduled", "Medium", "https://globex.example.com/jobs/full-stack"));
        LOG.info("Seeded in-memory store with {} sample job applications", STORE.size());
    }

    public static synchronized JobApplication save(JobApplication app) {
        boolean isNew = app.getId() == 0;
        if (isNew) {
            app.setId(ID_GENERATOR.incrementAndGet());
        }
        STORE.put(app.getId(), app);
        LOG.info("{} job application id={} company='{}' role='{}' status='{}'",
                isNew ? "Created" : "Updated", app.getId(), app.getCompany(), app.getRole(), app.getStatus());
        LOG.debug("Full record saved: {}", app);
        return app;
    }

    public static synchronized void delete(int id) {
        JobApplication removed = STORE.remove(id);
        if (removed != null) {
            LOG.info("Deleted job application id={} company='{}'", id, removed.getCompany());
        } else {
            LOG.debug("Delete requested for id={} but no record was found", id);
        }
    }

    public static synchronized JobApplication findById(int id) {
        JobApplication found = STORE.get(id);
        LOG.debug("findById({}) -> {}", id, found != null ? "found" : "not found");
        return found;
    }

    public static synchronized Map<Integer, JobApplication> findAll() {
        LOG.debug("findAll() -> returning {} record(s)", STORE.size());
        return new LinkedHashMap<>(STORE);
    }
}
