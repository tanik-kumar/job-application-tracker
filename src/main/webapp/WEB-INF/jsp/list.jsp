<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
    <title>Job Application Tracker</title>
    <link rel="stylesheet" href="assets/app.css" />
</head>
<body>
    <div class="top-bar">
        <h1><span class="emoji">🧭</span>Job Application Tracker</h1>
        <a class="btn btn-add" href="jobForm.action">+ Add Application</a>
    </div>

    <s:if test="flashMessage != null">
        <div class="flash"><s:property value="flashMessage" /></div>
    </s:if>

    <!-- Dashboard summary -->
    <div class="dashboard">
        <div class="card total">
            <div class="card-count"><s:property value="totalCount" /></div>
            <div class="card-label">Total</div>
        </div>
        <div class="card Applied">
            <div class="card-count"><s:property value="appliedCount" /></div>
            <div class="card-label">Applied</div>
        </div>
        <div class="card Interview">
            <div class="card-count"><s:property value="interviewCount" /></div>
            <div class="card-label">Interview</div>
        </div>
        <div class="card Offer">
            <div class="card-count"><s:property value="offerCount" /></div>
            <div class="card-label">Offer</div>
        </div>
        <div class="card Rejected">
            <div class="card-count"><s:property value="rejectedCount" /></div>
            <div class="card-label">Rejected</div>
        </div>
    </div>

    <!-- Search / filter bar -->
    <s:form action="jobs" method="get" cssClass="filter-bar">
        <input type="text" name="companyFilter" placeholder="Search by company..."
               value="<s:property value='companyFilter' />" />

        <select name="statusFilter">
            <option value="">All Statuses</option>
            <s:iterator value="{'Applied','Interview','Offer','Rejected'}" var="st">
                <option value="<s:property value='#st' />"
                    <s:if test="statusFilter == #st">selected</s:if>>
                    <s:property value="#st" />
                </option>
            </s:iterator>
        </select>

        <input type="hidden" name="sortBy" value="<s:property value='sortBy' />" />
        <input type="hidden" name="sortDir" value="<s:property value='sortDir' />" />

        <button type="submit" class="btn btn-filter">Filter</button>
        <a class="btn btn-reset" href="jobs.action">Reset</a>
    </s:form>

    <div class="table-wrap">
        <s:if test="emptyResult">
            <div class="empty-state">
                <span class="emoji">📭</span>
                No job applications found. Try adjusting your filters, or add a new one.
            </div>
        </s:if>
        <s:else>
        <table>
            <tr>
                <th>
                    <s:url var="sortCompany" action="jobs">
                        <s:param name="companyFilter" value="%{companyFilter}" />
                        <s:param name="statusFilter" value="%{statusFilter}" />
                        <s:param name="sortBy" value="%{'company'}" />
                        <s:param name="sortDir" value="%{nextSortDir('company')}" />
                    </s:url>
                    <a href="<s:property value='sortCompany' escapeHtml='false' />">Company</a>
                </th>
                <th>
                    <s:url var="sortRole" action="jobs">
                        <s:param name="companyFilter" value="%{companyFilter}" />
                        <s:param name="statusFilter" value="%{statusFilter}" />
                        <s:param name="sortBy" value="%{'role'}" />
                        <s:param name="sortDir" value="%{nextSortDir('role')}" />
                    </s:url>
                    <a href="<s:property value='sortRole' escapeHtml='false' />">Role</a>
                </th>
                <th>
                    <s:url var="sortStatus" action="jobs">
                        <s:param name="companyFilter" value="%{companyFilter}" />
                        <s:param name="statusFilter" value="%{statusFilter}" />
                        <s:param name="sortBy" value="%{'status'}" />
                        <s:param name="sortDir" value="%{nextSortDir('status')}" />
                    </s:url>
                    <a href="<s:property value='sortStatus' escapeHtml='false' />">Status</a>
                </th>
                <th>
                    <s:url var="sortPriority" action="jobs">
                        <s:param name="companyFilter" value="%{companyFilter}" />
                        <s:param name="statusFilter" value="%{statusFilter}" />
                        <s:param name="sortBy" value="%{'priority'}" />
                        <s:param name="sortDir" value="%{nextSortDir('priority')}" />
                    </s:url>
                    <a href="<s:property value='sortPriority' escapeHtml='false' />">Priority</a>
                </th>
                <th>
                    <s:url var="sortDate" action="jobs">
                        <s:param name="companyFilter" value="%{companyFilter}" />
                        <s:param name="statusFilter" value="%{statusFilter}" />
                        <s:param name="sortBy" value="%{'appliedDate'}" />
                        <s:param name="sortDir" value="%{nextSortDir('appliedDate')}" />
                    </s:url>
                    <a href="<s:property value='sortDate' escapeHtml='false' />">Applied Date</a>
                </th>
                <th>Notes</th>
                <th>Link</th>
                <th>Actions</th>
            </tr>
            <s:iterator value="jobApplications">
                <tr>
                    <td><s:property value="company" /></td>
                    <td><s:property value="role" /></td>
                    <td><span class="status <s:property value='status' />"><s:property value="status" /></span></td>
                    <td><span class="priority-dot priority-<s:property value='priority' />"></span><s:property value="priority" /></td>
                    <td><s:property value="appliedDate" /></td>
                    <td><s:property value="notes" /></td>
                    <td>
                        <s:if test="jobUrl != null && jobUrl != ''">
                            <a href="<s:property value='jobUrl' />" target="_blank" rel="noopener">🔗 View</a>
                        </s:if>
                    </td>
                    <td>
                        <a class="btn btn-edit btn-sm" href="jobForm.action?id=<s:property value='id' />">Edit</a>
                        <a class="btn btn-delete btn-sm" href="deleteJob.action?id=<s:property value='id' />"
                           onclick="return confirmAndProceed('Delete this application?');">Delete</a>
                    </td>
                </tr>
            </s:iterator>
        </table>
        </s:else>
    </div>

    <%@ include file="/WEB-INF/jsp/common/loader.jspf" %>
</body>
</html>
