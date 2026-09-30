<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
    <title>Job Application Form</title>
    <link rel="stylesheet" href="assets/app.css" />
</head>
<body>
    <div class="top-bar">
        <h1><s:if test="jobApplication.id > 0">✏️ Edit</s:if><s:else>➕ Add</s:else> Job Application</h1>
    </div>

    <s:form action="saveJob" method="post" cssClass="job-form">
        <s:hidden name="jobApplication.id" />

        <label>Company</label>
        <input type="text" name="jobApplication.company" value="<s:property value='jobApplication.company' />" required />

        <label>Role</label>
        <input type="text" name="jobApplication.role" value="<s:property value='jobApplication.role' />" required />

        <label>Status</label>
        <select name="jobApplication.status">
            <s:iterator value="{'Applied','Interview','Offer','Rejected'}" var="st">
                <option value="<s:property value='#st' />"
                    <s:if test="jobApplication.status == #st">selected</s:if>>
                    <s:property value="#st" />
                </option>
            </s:iterator>
        </select>

        <label>Priority</label>
        <select name="jobApplication.priority">
            <s:iterator value="{'Low','Medium','High'}" var="pr">
                <option value="<s:property value='#pr' />"
                    <s:if test="jobApplication.priority == #pr">selected</s:if>>
                    <s:property value="#pr" />
                </option>
            </s:iterator>
        </select>

        <label>Applied Date</label>
        <input type="date" name="jobApplication.appliedDate"
               value="<s:property value='jobApplication.appliedDate' />" />

        <label>Job Posting Link (optional)</label>
        <input type="url" name="jobApplication.jobUrl" placeholder="https://company.com/careers/job123"
               value="<s:property value='jobApplication.jobUrl' />" />

        <label>Notes</label>
        <textarea name="jobApplication.notes" rows="3"><s:property value="jobApplication.notes" /></textarea>

        <div class="actions">
            <button type="submit" class="btn btn-save">Save</button>
            <a class="btn btn-cancel" href="jobs.action">Cancel</a>
        </div>
    </s:form>

    <%@ include file="/WEB-INF/jsp/common/loader.jspf" %>
</body>
</html>
