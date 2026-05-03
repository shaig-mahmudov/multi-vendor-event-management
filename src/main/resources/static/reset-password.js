const token = new URLSearchParams(window.location.search).get('token');
const msg = document.getElementById('message');

async function resetPassword() {
    const newPassword = document.getElementById('newPassword').value;
    const confirmPassword = document.getElementById('confirmPassword').value;

    if (!newPassword || newPassword !== confirmPassword) {
        msg.style.color = 'red';
        msg.textContent = 'Passwords do not match.';
        return;
    }

    try {
        const res = await fetch('/api/auth/reset-password', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ token, newPassword, confirmPassword })
        });

        const data = await res.json();

        if (res.ok) {
            msg.style.color = 'green';
            msg.textContent = 'Password reset successful! You can now log in.';
        } else {
            msg.style.color = 'red';
            msg.textContent = data.message || 'Something went wrong.';
        }
    } catch (err) {
        msg.style.color = 'red';
        msg.textContent = 'Server error. Please try again.';
    }
}

document.getElementById('resetBtn').addEventListener('click', resetPassword);