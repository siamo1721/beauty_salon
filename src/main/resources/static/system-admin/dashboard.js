document.addEventListener('DOMContentLoaded', function() {
    loadAllAppointments();
});

async function loadAllAppointments() {
    try {
        const response = await fetch('/api/system-admin/appointments');
        const appointments = await response.json();
        displayAppointments(appointments);
    } catch (error) {
        console.error('Ошибка при загрузке записей:', error);
    }
}

function displayAppointments(appointments) {
    const container = document.getElementById('all-appointments');
    if (!container) return;

    container.innerHTML = appointments.map(appointment => {
        const appointmentDate = new Date(appointment.appointmentDate);
        const mskTime = appointmentDate.toLocaleTimeString('ru-RU', { timeZone: 'Europe/Moscow', hour: '2-digit', minute: '2-digit', hour12: false });

        return `
            <div class="card mb-3">
                <div class="card-body">
                    <h5 class="card-title">
                        ${mskTime} - ${appointment.servicee.name}
                    </h5>
                    <p class="card-text">
                        Клиент: ${appointment.user.firstName} ${appointment.user.lastName}<br>
                        Мастер: ${appointment.master.user.firstName} ${appointment.master.user.lastName}
                    </p>
                    <button class="btn btn-primary" onclick="showEditModal(${JSON.stringify(appointment).replace(/"/g, '&quot;')})">
                        Редактировать
                    </button>
                </div>
            </div>
        `;
    }).join('');
}

function showEditModal(appointment) {
    const modal = document.getElementById('editModal');
    const form = document.getElementById('editForm');

    form.elements['id'].value = appointment.id;
    form.elements['appointmentDate'].value = new Date(appointment.appointmentDate).toISOString().slice(0, 16);
    form.elements['serviceId'].value = appointment.servicee.id;
    form.elements['userId'].value = appointment.user.id;
    form.elements['masterId'].value = appointment.master.id;

    const bootstrapModal = new bootstrap.Modal(modal);
    bootstrapModal.show();
}

async function saveAppointment(event) {
    event.preventDefault();
    const form = event.target;
    const appointmentId = form.elements['id'].value;

    const updatedAppointment = {
        appointmentDate: form.elements['appointmentDate'].value,
        serviceId: parseInt(form.elements['serviceId'].value),
        userId: parseInt(form.elements['userId'].value),
        masterId: parseInt(form.elements['masterId'].value)
    };

    try {
        const response = await fetch(`/api/system-admin/appointments/${appointmentId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(updatedAppointment),
        });

        if (!response.ok) {
            throw new Error('Ошибка при обновлении записи');
        }

        const modal = bootstrap.Modal.getInstance(document.getElementById('editModal'));
        modal.hide();
        await loadAllAppointments();
    } catch (error) {
        console.error('Ошибка при сохранении записи:', error);
        alert('Не удалось сохранить изменения. Пожалуйста, попробуйте еще раз.');
    }
}