function clearAll() {
  ['name', 'email', 'password'].forEach(f => {
    document.getElementById(f + 'Err').classList.remove('show');
    document.getElementById(f).classList.remove('input-error');
  });
  document.getElementById('okAlert').classList.remove('show');
  document.getElementById('errAlert').classList.remove('show');
}

function fieldError(id, msg) {
  document.getElementById(id + 'Err').textContent = msg;
  document.getElementById(id + 'Err').classList.add('show');
  document.getElementById(id).classList.add('input-error');
}
function togglePw() {
  const input  = document.getElementById('password');
  const toggle = document.getElementById('pwToggle');
  if (input.type === 'password') {
    input.type = 'text';
    toggle.textContent = 'Hide';
  } else {
    input.type = 'password';
    toggle.textContent = 'Show';
  }
}

function validate(name, email, password) {
  let valid = true;
  if (!name.trim()) {
    fieldError('name', 'Name is required'); valid = false;
  } else if (!/^[a-zA-Z\s]+$/.test(name)) {
    fieldError('name', 'Name must contain only letters'); valid = false;
  }
  if (!email.trim()) {
    fieldError('email', 'Email is required'); valid = false;
  } else if (!/^[A-Za-z0-9._%+\-]+@[A-Za-z0-9.\-]+\.[A-Za-z]{2,6}$/.test(email)) {
    fieldError('email', 'Enter a valid email (e.g. name@example.com)'); valid = false;
  }
  if (!password) {
    fieldError('password', 'Password is required'); valid = false;
  } else if (password.length < 6) {
    fieldError('password', 'Minimum 6 characters required'); valid = false;
  } else if (!/(?=.*[a-zA-Z])(?=.*[0-9])/.test(password)) {
    fieldError('password', 'Must contain both letters and numbers'); valid = false;
  }
  return valid;
}

async function doRegister() {
  clearAll();
  const name     = document.getElementById('name').value.trim();
  const email    = document.getElementById('email').value.trim();
  const password = document.getElementById('password').value;

  if (!validate(name, email, password)) return;

  const btn = document.getElementById('regBtn');
  btn.disabled = true;
  btn.innerHTML = '<span class="spinner"></span> Registering...';

  try {
    const res  = await apiFetch('/api/students/register', {
      method: 'POST',
      body: JSON.stringify({ name, email, password })
    });
    const data = await res.json();

    if (data.status !== 'error') {
      // Success — keep button disabled, show message, redirect
      btn.innerHTML = '✓ Registered!';
      document.getElementById('okAlert').textContent = '✓ ' + data.message + ' Redirecting to login...';
      document.getElementById('okAlert').classList.add('show');
      setTimeout(() => window.location.href = 'login.html', 2000);
    } else {
      // Business error from backend — re-enable button
      document.getElementById('errAlert').textContent = '✗ ' + data.message;
      document.getElementById('errAlert').classList.add('show');
      btn.disabled = false;
      btn.innerHTML = 'Register';
    }
  } catch (e) {
    // Network/server error — re-enable button
    document.getElementById('errAlert').textContent = '✗ Cannot reach server. Is the backend running on port 9091?';
    document.getElementById('errAlert').classList.add('show');
    btn.disabled = false;
    btn.innerHTML = 'Register';
  }
}

document.addEventListener('keydown', e => { if (e.key === 'Enter') doRegister(); });