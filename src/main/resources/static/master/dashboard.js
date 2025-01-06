document.addEventListener('DOMContentLoaded', function() {
    loadTodayAppointments();
    loadAllAppointments();
    loadStatistics();
});

async function loadTodayAppointments() {
    try {
        const response = await fetch('/api/master/appointments/today');
        const appointments = await response.json();
        displayAppointments(appointments, 'today-appointments');
    } catch (error) {
        console.error('Ошибка при загрузке записей на сегодня:', error);
    }
}

async function loadAllAppointments() {
    try {
        const response = await fetch('/api/master/appointments');
        const appointments = await response.json();
        displayAppointments(appointments, 'all-appointments');
    } catch (error) {
        console.error('Ошибка при загрузке всех записей:', error);
    }
}

function displayAppointments(appointments, containerId) {
    const container = document.getElementById(containerId);
    if (!container) return;

    container.innerHTML = appointments.map(appointment => `
        <div class="card mb-3">
            <div class="card-body">
                <h5 class="card-title">
                    ${new Date(appointment.appointmentDate).toLocaleString()} - 
                    ${appointment.servicee.name}
                </h5>
                <p class="card-text">
                    Клиент: ${appointment.user.firstName} ${appointment.user.lastName}
                </p>
                <select class="form-select mb-2" 
                        onchange="updateStatus(${appointment.id}, this.value)"
                        ${appointment.status === 'COMPLETED' ? 'disabled' : ''}>
                    <option value="PENDING" ${appointment.status === 'PENDING' ? 'selected' : ''}>Ожидает</option>
                    <option value="IN_PROGRESS" ${appointment.status === 'IN_PROGRESS' ? 'selected' : ''}>В процессе</option>
                    <option value="COMPLETED" ${appointment.status === 'COMPLETED' ? 'selected' : ''}>Завершено</option>
                </select>
            </div>
        </div>
    `).join('');
}

async function updateStatus(appointmentId, newStatus) {
    try {
        const response = await fetch(`/api/master/appointments/${appointmentId}/status`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ status: newStatus })
        });
        if (!response.ok) {
            throw new Error('Ошибка при обновлении статуса');
        }
        await loadTodayAppointments();
        await loadAllAppointments();
    } catch (error) {
        console.error('Ошибка при обновлении статуса:', error);
        alert('Не удалось обновить статус. Пожалуйста, попробуйте еще раз.');
    }
}

async function loadStatistics() {
    try {
        const response = await fetch('/api/master/statistics');
        const statistics = await response.json();
        displayStatistics(statistics);
    } catch (error) {
        console.error('Ошибка при загрузке статистики:', error);
    }
}

function displayStatistics(statistics) {
    const container = document.getElementById('master-stats');
    if (!container) return;

    container.innerHTML = `
        <div class="list-group">
            <div class="list-group-item">
                <h6 class="mb-1">Всего записей</h6>
                <p class="mb-1">${statistics.totalAppointments}</p>
            </div>
            <div class="list-group-item">
                <h6 class="mb-1">Завершено</h6>
                <p class="mb-1">${statistics.completedAppointments}</p>
            </div>
            <div class="list-group-item">
                <h6 class="mb-1">Средняя оценка</h6>
                <p class="mb-1">${statistics.averageRating.toFixed(1)}</p>
            </div>
        </div>
    `;
}