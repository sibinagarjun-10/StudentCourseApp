requireAuth();
loadNavbar();
document.getElementById('welcomeName').textContent = Session.get().name || 'Student';