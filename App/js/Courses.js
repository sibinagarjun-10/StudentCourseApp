requireAuth();
loadNavbar();

const { studentId } = Session.get();

let enrolledIds = new Set();
const seatMap = {};

function availBadge(avail) {
  if (avail <= 0) return `<span class="badge badge-red">Full</span>`;
  if (avail <= 5) return `<span class="badge badge-yellow">&#9888; ${avail}</span>`;
  return              `<span class="badge badge-green">&#10003; ${avail}</span>`;
}

function renderTable(courses) {
  const el = document.getElementById('content');
  if (!courses.length) {
    el.innerHTML = '<div class="empty-msg">No courses available right now.</div>';
    return;
  }

  const rows = courses.map(c => {
    seatMap[c.id] = { total: c.totalSeats, avail: c.availableSeats };

    const isEnrolled = enrolledIds.has(c.id);
    const isFull     = c.availableSeats <= 0;

    let actionBtn;
    if (isEnrolled) {
      actionBtn = `<button class="btn btn-sm btn-enrolled" disabled>&#10003; Enrolled</button>`;
    } else if (isFull) {
      actionBtn = `<button class="btn btn-sm btn-enroll" disabled>Course Full</button>`;
    } else {
      actionBtn = `<button class="btn btn-sm btn-enroll" id="ebtn-${c.id}" onclick="doEnroll(${c.id})">+ Enroll</button>`;
    }

    return `
      <tr>
        <td><strong>#${c.id}</strong></td>
        <td><strong>${escHtml(c.name)}</strong></td>
        <td style="max-width:240px; color:#6b7280;">${escHtml(c.description || '—')}</td>
        <td>${escHtml(c.duration || '—')}</td>
        <td style="text-align:center;">${c.totalSeats}</td>
        <td style="text-align:center;" id="avail-${c.id}">${availBadge(c.availableSeats)}</td>
        <td>${actionBtn}</td>
      </tr>`;
  }).join('');

  el.innerHTML = `
    <div class="table-wrap">
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Course Name</th>
            <th>Description</th>
            <th>Duration</th>
            <th style="text-align:center;">Total Seats</th>
            <th style="text-align:center;">Available</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>${rows}</tbody>
      </table>
    </div>`;
}

async function doEnroll(courseId) {
  const btn = document.getElementById('ebtn-' + courseId);
  btn.disabled = true;
  btn.innerHTML = '<span class="spinner"></span>';

  try {
    const res  = await apiFetch(`/api/enroll/${courseId}`, {
      method: 'POST',
      body: JSON.stringify({ studentId: Number(studentId) })
    });
    const data = await res.json();

    if (res.ok) {
    enrolledIds.add(courseId);
    seatMap[courseId].avail = Math.max(0, seatMap[courseId].avail - 1);
    const availCell = document.getElementById('avail-' + courseId);
    if (availCell) availCell.innerHTML = availBadge(seatMap[courseId].avail);
    btn.className = 'btn btn-sm btn-enrolled';
    btn.innerHTML = '&#10003; Enrolled';
    btn.disabled = true;
    showToast('✓ ' + data.message, 'success');
} else {
    showToast('✗ ' + data.message, 'error');
    btn.disabled = false;
    btn.innerHTML = '+ Enroll';
}
  } catch (e) {
    showToast('✗ Server error. Please try again.', 'error');
    btn.disabled = false;
    btn.innerHTML = '+ Enroll';
  }
}

async function loadData() {
  try {
    const [cRes, eRes] = await Promise.all([
      apiFetch('/api/courses'),
      apiFetch(`/api/students/${studentId}/enrollments`)
    ]);
    if (!cRes.ok) throw new Error();
    const courses = await cRes.json();
    if (eRes.ok) {
      const enrolled = await eRes.json();
      enrolled.forEach(e => enrolledIds.add(e.id));
    }
    renderTable(courses);
  } catch (e) {
    document.getElementById('content').innerHTML =
      '<div class="empty-msg">Could not load courses. Is the backend running on port 9091?</div>';
  }
}

loadData();