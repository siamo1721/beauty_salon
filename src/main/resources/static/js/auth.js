async function handleLogin(event) {
    event.preventDefault();

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    try {
        const response = await fetch('/api/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            credentials: 'include',
            body: JSON.stringify({
                username: username,
                password: password
            })
        });

        const data = await response.json();

        if (response.ok && data.authenticated) {
            // Проверяем роль пользователя
            const roles = data.roles || [];
            if (roles.includes('ADMIN')) {
                window.location.href = '/admin/index.html';
            }
            else if (roles.includes('MASTER')) {
                window.location.href = '/master/index.html';}
            else if (roles.includes('SYSTEM_ADMIN')) {
                window.location.href = '/system-admin/index.html';}
            else {
                window.location.href = '/';
            }
        } else {
            const errorMessage = data.message || 'Неверное имя пользователя или пароль';
            alert(errorMessage);
        }
    } catch (error) {
        console.error('Login error:', error);
        alert('Произошла ошибка при входе');
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
        photo: formData.get('photo'),
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


async function updateAuthUI() {
    const isAuthenticated = await checkAuth();
    console.log('Authentication status:', isAuthenticated); // Для отладки

    const authButtons = document.getElementById('auth-buttons');
    const userMenu = document.getElementById('user-menu');

    if (!authButtons || !userMenu) {
        console.error('UI elements not found');
        return;
    }

    if (isAuthenticated) {
        console.log('User is authenticated, updating UI...'); // Для отладки
        authButtons.style.display = 'none';
        userMenu.style.display = 'block';
    } else {
        console.log('User is not authenticated, updating UI...'); // Для отладки
        authButtons.style.display = 'block';
        userMenu.style.display = 'none';
    }
}

async function checkAuth() {
    try {
        const response = await fetch('/api/auth/check', {
            method: 'GET',
            credentials: 'include',
            headers: {
                'Accept': 'application/json'
            }
        });

        if (!response.ok) {
            return false;
        }

        const data = await response.json();
        return data.authenticated === true;
    } catch (error) {
        console.error('Authentication check error:', error);
        return false;
    }
}


// Export functions for global use
window.handleLogin = handleLogin;
window.handleRegister = handleRegister;
window.handleLogout = handleLogout;
window.updateAuthUI = updateAuthUI;

// Update UI on page load
// document.addEventListener('DOMContentLoaded', updateAuthUI);
// Добавляем в конец файла
document.addEventListener('DOMContentLoaded', async () => {
    await updateAuthUI();
});

// Добавляем обработчик для случаев, когда страница уже загружена
if (document.readyState === 'complete') {
    updateAuthUI();
}
