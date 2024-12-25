document.addEventListener('DOMContentLoaded', function() {
    loadUserProfile();
    loadUserAppointments();
});

async function loadUserProfile() {
    try {
        const response = await fetch('/api/clients/current');
        const profile = await response.json();
        displayProfile(profile);
    } catch (error) {
        console.error('Ошибка при загрузке профиля:', error);
    }
}

async function loadUserAppointments() {
    try {
        const response = await fetch('/api/appointments/my');
        const appointments = await response.json();
        displayAppointments(appointments);
    } catch (error) {
        console.error('Ошибка при загрузке записей:', error);
    }
}

function displayProfile(profile) {
    const profileContainer = document.getElementById('profile-info');
    profileContainer.innerHTML = `
        <h3>${profile.user.firstName} ${profile.user.lastName}</h3>
        <p>Телефон: ${profile.user.phone}</p>
        <p>Email: ${profile.user.email}</p>
        <p>Адрес: ${profile.address.city}, ${profile.address.street}, ${profile.address.house}</p>
    `;
}

function displayAppointments(appointments) {
    const container = document.getElementById('appointments-container');
    container.innerHTML = '';

    appointments.forEach(appointment => {
        const card = document.createElement('div');
        card.className = 'card mb-3';
        card.innerHTML = `
            <div class="card-body">
                <h5 class="card-title">Запись на ${new Date(appointment.appointmentDate).toLocaleString()}</h5>
                <p class="card-text">
                    Услуга: ${appointment.service.name}<br>
                    Мастер: ${appointment.master.user.firstName} ${appointment.master.user.lastName}<br>
                    Статус: ${appointment.status}
                </p>
                ${appointment.status === 'COMPLETED' && !appointment.review ? `
                    <button onclick="showReviewForm(${appointment.id})" class="btn btn-primary">Оставить отзыв</button>
                ` : ''}
            </div>
        `;
        container.appendChild(card);
    });
}