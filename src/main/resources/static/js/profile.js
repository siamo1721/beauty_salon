document.addEventListener('DOMContentLoaded', function() {
    loadUserProfile();
    loadUserAppointments();
    document.getElementById('photo-upload').addEventListener('change', uploadPhoto);
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
            <img src="/api/users/${profile.id}/photo" class="card-img-top" alt="Фото профиля" onerror="this.src='/img/default-avatar.jpg'">
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

async function uploadPhoto(event) {
    const file = event.target.files[0];
    if (!file) return;

    const formData = new FormData();
    formData.append('file', file);

    try {
        const response = await fetch(`/api/users/${getCurrentUserId()}/photo`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            },
            body: formData
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        alert('Фото успешно загружено');
        loadUserProfile();
    } catch (error) {
        console.error('Ошибка при загрузке фото:', error);
        alert('Не удалось загрузить фото');
    }
}
async function loadUserAppointments() {
    try {
        const response = await fetch('/api/appointments/my', {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`,
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const appointments = await response.json();
        console.log('Полученные записи:', appointments);
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
                    ${appointment.status !== 'COMPLETED' && appointment.status !== 'CANCELED' ?
            `<button class="btn btn-danger btn-sm" onclick="cancelAppointment(${appointment.id})">Отменить запись</button>` :
            ''}
                </p>
            </div>
        `;
        container.appendChild(card);
    });
}
async function cancelAppointment(appointmentId) {
    if (!confirm('Вы уверены, что хотите отменить эту запись?')) {
        return;
    }

    try {
        const response = await fetch(`/api/appointments/${appointmentId}/cancel`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`,
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        alert('Запись успешно отменена');
        loadUserAppointments(); // Перезагрузка списка записей
    } catch (error) {
        console.error('Ошибка при отмене записи:', error);
        alert('Не удалось отменить запись. Пожалуйста, попробуйте еще раз.');
    }
}
