document.addEventListener('DOMContentLoaded', function() {
    loadUserProfile();
    loadUserAppointments();
});

async function loadUserProfile() {
    try {
        const response = await fetch('/api/auth/current', {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`,
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const profile = await response.json();
        displayProfile(profile);
    } catch (error) {
        console.error('Ошибка при загрузке профиля:', error);
        const profileContainer = document.getElementById('profile-info');
        profileContainer.innerHTML = '<p class="text-danger">Не удалось загрузить информацию профиля</p>';
    }
}

function displayProfile(profile) {
    const profileContainer = document.getElementById('profile-info');
    if (!profile) {
        profileContainer.innerHTML = '<p>Информация о профиле недоступна</p>';
        return;
    }

    profileContainer.innerHTML = `
        <div class="card">
            <div class="card-body">
                <h3 class="card-title">${profile.firstName} ${profile.lastName}</h3>
                <p class="card-text">
                    <strong>Телефон:</strong> ${profile.phone || 'Не указан'}<br>
                    <strong>Email:</strong> ${profile.email || 'Не указан'}<br>
                    <strong>Имя пользователя:</strong> ${profile.username}
                </p>
            </div>
        </div>
    `;
}

async function loadUserAppointments() {
    try {
        const response = await fetch('/api/appointments/my', {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`, // или как у вас хранится токен
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const appointments = await response.json();
        console.log('Полученные записи:', appointments); // для отладки
        displayAppointments(appointments);
    } catch (error) {
        console.error('Подробная ошибка при загрузке записей:', error);
        const container = document.getElementById('appointments-container');
        container.innerHTML = `<p class="text-danger">Ошибка при загрузке записей: ${error.message}</p>`;
    }
}

function displayAppointments(appointments) {
    const container = document.getElementById('appointments-container');
    if (!appointments || appointments.length === 0) {
        container.innerHTML = '<p>У вас пока нет записей</p>';
        return;
    }

    container.innerHTML = '';
    appointments.forEach(appointment => {
        const card = document.createElement('div');
        card.className = 'card mb-3';
        card.innerHTML = `
            <div class="card-body">
                <h5 class="card-title">Запись на ${new Date(appointment.appointmentDate).toLocaleString()}</h5>
                <p class="card-text">
                    Услуга: ${appointment.servicee?.name || 'Не указана'}<br>
                    Мастер: ${appointment.master?.user?.firstName || 'Не указан'} ${appointment.master?.user?.lastName || ''}<br>
                    Статус: ${appointment.status || 'Не указан'}
                </p>
            </div>
        `;
        container.appendChild(card);
    });
}
