requireAuth();
loadNavbar();

const { studentId } = Session.get();

const openRows = new Set();

function renderTable(list) {
  const el = document.getElementById('content');
  if (!list.length) {
    el.innerHTML = `<div class="empty-msg">
      You have no enrollments yet. <a href="courses.html">Browse courses &#8594;</a>
    </div>`;
    return;
  }

  const rows = list.map(c => `
    <tr id="row-${c.id}">
      <td><strong>#${c.id}</strong></td>
      <td><strong>${escHtml(c.name)}</strong></td>
      <td>
        <button class="btn btn-sm btn-toggle" id="tbtn-${c.id}"
          onclick="toggleDetails(${c.id})">
          &#9660; View Details
        </button>
      </td>
      <td>
        <button class="btn btn-sm btn-unenroll" id="ubtn-${c.id}"
          onclick="doUnenroll(${c.id})">
          &#10005; Unenroll
        </button>
      </td>
    </tr>
    <tr class="details-row" id="drow-${c.id}" style="display:none;">
      <td colspan="4">
        <div class="details-inner">
          <span class="d-label">Course ID</span>     <span class="d-val">${c.id}</span>
          <span class="d-label">Course Name</span>   <span class="d-val">${escHtml(c.name)}</span>
          <span class="d-label">Duration</span>      <span class="d-val">${escHtml(c.duration || 'N/A')}</span>
          <span class="d-label">Description</span>   <span class="d-val">${escHtml(c.description || 'No description available.')}</span>
        </div>
      </td>
    </tr>`
  ).join('');

  el.innerHTML = `
    <div class="table-wrap">
      <table id="enrollTable">
        <thead>
          <tr>
            <th>Course ID</th>
            <th>Course Name</th>
            <th>Details</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>${rows}</tbody>
      </table>
    </div>`;
}

function toggleDetails(courseId) {
  const drow = document.getElementById('drow-' + courseId);
  const tbtn = document.getElementById('tbtn-' + courseId);
  const isOpen = openRows.has(courseId);
  if (isOpen) {
    drow.style.display = 'none';
    tbtn.innerHTML = '&#9660; View Details';
    openRows.delete(courseId);
  } else {
    drow.style.display = 'table-row';
    tbtn.innerHTML = '&#9650; Hide Details';
    openRows.add(courseId);
  }
}

async function doUnenroll(courseId) {
  const btn = document.getElementById('ubtn-' + courseId);
  btn.disabled = true;
  btn.innerHTML = '<span class="spinner spinner-dark"></span>';

  try {
    const res  = await apiFetch(`/api/enroll/${courseId}`, {
      method: 'DELETE',
      body: JSON.stringify({ studentId: Number(studentId) })
    });
    const data = await res.json();

    if (res.ok) {
    showToast('✓ ' + data.message, 'success');
    document.getElementById('row-' + courseId)?.remove();
    document.getElementById('drow-' + courseId)?.remove();
    openRows.delete(courseId);
    const remaining = document.querySelectorAll('#enrollTable tbody tr:not(.details-row)').length;
    if (remaining === 0) {
        document.getElementById('content').innerHTML = `
          <div class="empty-msg">No enrollments yet. <a href="courses.html">Browse courses &#8594;</a></div>`;
    }
} else {
    showToast('✗ ' + data.message, 'error');
    btn.disabled = false;
    btn.innerHTML = '&#10005; Unenroll';
}
  } catch (e) {
    showToast('✗ Server error. Please try again.', 'error');
    btn.disabled = false;
    btn.innerHTML = '&#10005; Unenroll';
  }
}

async function loadEnrollments() {
  try {
    const res = await apiFetch(`/api/students/${studentId}/enrollments`);
    if (!res.ok) throw new Error();
    const data = await res.json();
    renderTable(data);
  } catch (e) {
    document.getElementById('content').innerHTML =
      '<div class="empty-msg">Could not load enrollments. Is the backend running on port 9091?</div>';
  }
}

loadEnrollments();