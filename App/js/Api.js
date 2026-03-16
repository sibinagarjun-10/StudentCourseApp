// ── Backend URL ───────────────────────────────────────────
const BASE_URL = 'http://localhost:9090';

// ── CORS-aware fetch: always sends session cookie ─────────
async function apiFetch(path, options = {}) {
  return fetch(BASE_URL + path, {
    credentials: 'include',
    headers: { 'Content-Type': 'application/json', ...(options.headers || {}) },
    ...options,
  });
}

// ── Session: stores student info in browser sessionStorage ─
const Session = {
  set(studentId, name, email) {
    sessionStorage.setItem('studentId', studentId);
    sessionStorage.setItem('studentName', name);
    sessionStorage.setItem('studentEmail', email);
  },
  get() {
    return {
      studentId: sessionStorage.getItem('studentId'),
      name:      sessionStorage.getItem('studentName'),
      email:     sessionStorage.getItem('studentEmail'),
    };
  },
  clear()     { sessionStorage.clear(); },
  isLoggedIn(){ return !!sessionStorage.getItem('studentId'); }
};

// ── Auth guard: redirect to login if not logged in ────────
function requireAuth() {
  if (!Session.isLoggedIn()) { window.location.href = 'login.html'; return false; }
  return true;
}

// ── Logout: clear session + redirect ─────────────────────
async function logout() {
  try { await apiFetch('/api/students/logout', { method: 'POST' }); } catch (e) {}
  Session.clear();
  window.location.href = 'login.html';
}

// ── Load shared navbar from navbar.html ───────────────────
async function loadNavbar() {
  const res  = await fetch('./navbar.html');
  const html = await res.text();
  document.getElementById('navbar-placeholder').innerHTML = html;
  // Set student name after navbar is injected into the DOM
  const { name } = Session.get();
  const el = document.getElementById('navName');
  if (el) el.textContent = name || '';
}

// ── Toast notification ────────────────────────────────────
function showToast(msg, type = 'success') {
  const t = document.getElementById('toast');
  if (!t) return;
  t.textContent = msg;
  t.className = `toast toast-${type} show`;
  setTimeout(() => t.classList.remove('show'), 3200);
}

// ── Escape HTML to prevent XSS ────────────────────────────
function escHtml(s) {
  return String(s || '').replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;');
}