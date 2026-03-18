if (Session.isLoggedIn()) window.location.href = 'dashboard.html';

function togglePw() {
  const inp = document.getElementById('password');
  const btn = document.getElementById('pwToggle');
  inp.type = inp.type === 'password' ? 'text' : 'password';
  btn.textContent = inp.type === 'password' ? 'Show' : 'Hide';
}

function clearAll() {
  ['email', 'password'].forEach(f => {
    document.getElementById(f + 'Err').classList.remove('show');
    document.getElementById(f).classList.remove('input-error');
  });
  document.getElementById('errAlert').classList.remove('show');
}

function fieldError(id, msg) {
  document.getElementById(id + 'Err').textContent = msg;
  document.getElementById(id + 'Err').classList.add('show');
  document.getElementById(id).classList.add('input-error');
}

function validate(email, password) {
  let valid = true;
  if (!email.trim()) {
    fieldError('email', 'Email is required'); valid = false;
  } else if (!/^[A-Za-z0-9._%+\-]+@[A-Za-z0-9.\-]+\.[A-Za-z]{2,6}$/.test(email)) {
    fieldError('email', 'Enter a valid email address'); valid = false;
  }
  if (!password) { fieldError('password', 'Password is required'); valid = false; }
  return valid;
}

async function doLogin() {
  clearAll();
  const email    = document.getElementById('email').value.trim();
  const password = document.getElementById('password').value;
  if (!validate(email, password)) return;

  const btn = document.getElementById('loginBtn');
  btn.disabled = true;
  btn.innerHTML = '<span class="spinner"></span> Logging in...';

  try {
    const res  = await apiFetch('/api/students/login', {
      method: 'POST',
      body: JSON.stringify({ email, password })
    });
    const data = await res.json();

    // Check data.status because business errors now return 200 OK
    // AFTER
if (res.ok) {
    Session.set(data.studentId, data.name, email);
    window.location.href = 'dashboard.html';
} else {
    document.getElementById('errAlert').textContent = '✗ ' + data.message;
    document.getElementById('errAlert').classList.add('show');
}
  } catch (e) {
    document.getElementById('errAlert').textContent = '✗ Cannot reach server. Is backend running on port 9091?';
    document.getElementById('errAlert').classList.add('show');
  } finally {
    btn.disabled = false;
    btn.innerHTML = 'Login';
  }
}

document.addEventListener('keydown', e => { if (e.key === 'Enter') doLogin(); });