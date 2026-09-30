# Job Application Tracker

A simple CRUD web application built with **JSP + Struts2 + Gradle**, designed as a learning
project. It lets you track job applications (company, role, status, priority, applied date,
job posting link, notes) with a dashboard, search/filter, and sortable columns.

Data is stored **in-memory** (no database setup required) — it resets whenever the server restarts.
This keeps the project easy to read and modify while you learn Struts2/JSP fundamentals.

## Features

- **Dashboard** — summary cards showing Total / Applied / Interview / Offer / Rejected counts
- **Search & filter** — filter by company name (partial match) and/or status
- **Sortable columns** — click Company / Role / Status / Priority / Applied Date headers to
  sort ascending/descending
- **Priority field** — Low / Medium / High, shown as a colored dot indicator
- **Job posting link** — optional URL per application, opens in a new tab
- **Flash messages** — confirmation banner after create/update/delete
- **Loading overlay** — spinner shown on any action link/form submit until the page navigates
- **Native date picker** — Applied Date uses `<input type="date">` (built-in browser calendar)

## Tech Stack

- Java 11
- Struts 2.5.x (`javax.servlet` based)
- JSP + JSTL/Struts tags for views
- Gradle (via the **Gradle Wrapper** — no local Gradle install needed)
- [Gretty](https://github.com/gretty-gradle-plugin/gretty) Gradle plugin — runs an embedded
  Jetty 9.4 server, so you don't need a separately installed Tomcat/Jetty

## Requirements

- **Java 11 JDK** installed and on your `PATH` (or `JAVA_HOME` pointing to it). That's it —
  everything else (Gradle itself, Struts2, Jetty) is downloaded automatically by the Gradle
  Wrapper on first run.
- Internet access on first build (to download the Gradle distribution and dependencies).
  After that, builds work offline using the local Gradle/Maven cache.

Verify Java:
```bash
java -version
# should show version "11.x.x"
```

## Project Structure

```
job-application-tracker/
├── build.gradle                 # Gradle build config (Struts2, Gretty, dependencies)
├── settings.gradle
├── gradlew / gradlew.bat        # Gradle Wrapper scripts (use these, not a system `gradle`)
├── gradle/wrapper/              # Wrapper jar + version config
└── src/main/
    ├── java/com/example/jobtracker/
    │   ├── model/JobApplication.java      # Data model (POJO)
    │   ├── dao/JobApplicationDAO.java     # In-memory storage (swap for a real DB later)
    │   └── action/JobApplicationAction.java # Struts2 action: list/add/edit/save/delete
    ├── resources/struts.xml               # Struts2 action → JSP mappings
    └── webapp/
        └── WEB-INF/
            ├── web.xml                     # Registers the Struts2 filter
            └── jsp/
                ├── list.jsp                # Table of job applications + actions
                └── form.jsp                # Add/Edit form
```

## Build

From the project root, run:

```bash
# Linux / macOS / WSL
./gradlew build

# Windows
gradlew.bat build
```

This compiles the code and produces a WAR file at `build/libs/job-application-tracker.war`.

## Run the App (recommended: Gretty embedded server)

Start the app on an embedded Jetty server without needing Tomcat installed:

```bash
# Linux / macOS / WSL
./gradlew appStart

# Windows
gradlew.bat appStart
```

Then open: **http://localhost:8080/**

To stop the server:
```bash
./gradlew appStop
```

Alternative: `./gradlew appRun` runs the server in the foreground (blocks the terminal, stop with
Ctrl+C or any keypress) — use this if you want to see live logs while developing.

## Using the App

- **List** — `http://localhost:8080/jobs.action` (also the default landing page)
- **Dashboard cards** — at the top, click a status card to see it reflected once you filter (counts are always for the full data set)
- **Search/filter bar** — type a company name and/or pick a status, click "Filter"; "Reset" clears both
- **Sort** — click any of the Company / Role / Status / Priority / Applied Date column headers; click again to reverse direction
- **Add** — click "+ Add Application"
- **Edit** — click "Edit" on any row
- **Delete** — click "Delete" on any row (with confirmation)
- **Job link** — click "🔗 View" on a row (if a URL was provided) to open the job posting in a new tab

Fields: Company, Role, Status (Applied / Interview / Offer / Rejected), Priority (Low / Medium /
High), Applied Date (calendar picker), Job Posting Link (optional URL), Notes.

## Notes on Data Persistence

Data lives in a static in-memory map (`JobApplicationDAO`) and is **reset on every restart**.
The DAO is intentionally simple (a `LinkedHashMap`) so you can easily replace it later with a
real database (e.g., JDBC + H2/MySQL) as a learning exercise, without touching the Action or JSP
layers.

## Customization Ideas (for learning)

- Add validation (e.g., required fields, date format) using Struts2 validators.
- Add pagination to the list page.
- Replace `JobApplicationDAO` with a JDBC-backed DAO using H2 or MySQL.
- Add a "Notes/Interview History" child entity (one-to-many).
- Add more priority levels or a custom tagging system.
- Persist filter/sort preferences in a cookie or session.

## Troubleshooting

- **Port 8080 already in use**: change `httpPort` in `build.gradle` under the `gretty { }` block.
- **First build is slow**: it's downloading the Gradle distribution and dependencies; subsequent
  builds are fast and work offline.
- **`./gradlew: Permission denied`**: run `chmod +x gradlew` (Linux/macOS/WSL only).
