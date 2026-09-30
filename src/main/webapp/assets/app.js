/**
 * Shows a full-screen loading overlay whenever the user submits a form
 * or navigates via an action link (Add / Edit / Delete), and keeps it
 * visible until the browser finishes loading the next (server-rendered) page.
 */
function showLoader() {
    var overlay = document.getElementById('loading-overlay');
    if (overlay) {
        overlay.style.display = 'flex';
    }
}

/**
 * Used by delete links: only shows the loader if the user confirms the action.
 */
function confirmAndProceed(message) {
    var confirmed = window.confirm(message);
    if (confirmed) {
        showLoader();
    }
    return confirmed;
}

document.addEventListener('DOMContentLoaded', function () {
    document.querySelectorAll('form').forEach(function (form) {
        form.addEventListener('submit', showLoader);
    });

    // Delete links call confirmAndProceed() inline (see list.jsp) so they are
    // excluded here to avoid showing the loader before the confirm dialog.
    document.querySelectorAll('a.btn:not(.btn-delete)').forEach(function (link) {
        link.addEventListener('click', showLoader);
    });
});
