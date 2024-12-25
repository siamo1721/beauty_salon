// Check authentication status
async function checkAuth() {
    try {
        const response = await fetch('/api/auth/check', {
            credentials: 'include'
        });
        if (!response.ok) {
            return false;
        }
        const data = await response.json();
        return data.authenticated;
    } catch (error) {
        console.error('Authentication check error:', error);
        return false;
    }
}

// Handle login
async function handleLogin(event) {
    event.preventDefault();
    const form = event.target;
    const formData = new FormData(form);

    try {
        const response = await fetch('/api/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            credentials: 'include',
            body: JSON.stringify({
                username: formData.get('username'),
                password: formData.get('password')
            })
        });

        if (response.ok) {
            // const dropdownMenu = document.getElementById('dropdown-menu');
            // if (dropdownMenu) {
            //     dropdownMenu.classList.remove('hidden'); // Показываем меню
            //     dropdownMenu.classList.add('dropdown'); // Добавляем класс оформления
            // }
            window.location.href = '/';
        } else {
            const error = await response.json();
            alert(error.message || 'Invalid username or password');
        }
    } catch (error) {
        console.error('Login error:', error);
        alert('An error occurred during login');
    }
}

// Handle registration
async function handleRegister(event) {
    event.preventDefault();
    const form = event.target;
    const formData = new FormData(form);

    const userData = {
        username: formData.get('username'),
        password: formData.get('password'),
        firstName: formData.get('firstName'),
        lastName: formData.get('lastName'),
        email: formData.get('email'),
        phone: formData.get('phone'),
        birthDate: formData.get('birthDate'),
        address: {
            city: formData.get('city'),
            street: formData.get('street'),
            house: formData.get('house'),
            apartment: formData.get('apartment')
        }
    };

    try {
        const response = await fetch('/api/auth/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(userData)
        });

        if (response.ok) {
            alert('Registration successful! Please login.');
            window.location.href = '/login.html';
        } else {
            const error = await response.json();
            alert(error.message || 'Registration failed');
        }
    } catch (error) {
        console.error('Registration error:', error);
        alert('An error occurred during registration');
    }
}

// Handle logout
async function handleLogout() {
    try {
        const response = await fetch('/api/auth/logout', {
            method: 'POST',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (response.ok) {
            localStorage.clear();
            sessionStorage.clear();
            await updateAuthUI();
            window.location.href = '/';
        } else {
            console.error('Error during logout');
        }
    } catch (error) {
        console.error('Error:', error);
    }
}

// Check authentication for appointment
function checkAuthForAppointment(event) {
    event.preventDefault();
    const isAuthenticated = checkAuth();

    if (!isAuthenticated) {
        window.location.href = '/login.html';
        return false;
    }
    return true;
}

// Update UI based on authentication status
async function updateAuthUI() {
    const isAuthenticated = await checkAuth();
    const authButtons = document.getElementById('auth-buttons');
    const userMenu = document.getElementById('user-menu');

    if (!authButtons || !userMenu) {
        console.error('UI elements not found');
        return;
    }

    if (isAuthenticated) {
        authButtons.style.display = 'none';
        userMenu.style.display = 'block';
    } else {
        authButtons.style.display = 'block';
        userMenu.style.display = 'none';
    }
}

// Export functions for global use
window.handleLogin = handleLogin;
window.handleRegister = handleRegister;
window.handleLogout = handleLogout;
window.updateAuthUI = updateAuthUI;

// Update UI on page load
document.addEventListener('DOMContentLoaded', updateAuthUI);