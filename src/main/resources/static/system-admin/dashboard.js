document.addEventListener('DOMContentLoaded', function() {
    loadAllAppointments();
    loadDropdownData();
});

async function loadDropdownData() {
    try {
        // Загрузка услуг
        const servicesResponse = await fetch('/api/system-admin/services');
        const services = await servicesResponse.json();
        console.log('Загруженные услуги:', services); // Для отладки
        populateServiceDropdown(services);

        // Загрузка пользователей
        const usersResponse = await fetch('/api/system-admin/users');
        const users = await usersResponse.json();
        console.log('Загруженные пользователи:', users); // Для отладки
        populateUserDropdown(users);

        // Загрузка мастеров
        const mastersResponse = await fetch('/api/system-admin/masters');
        const masters = await mastersResponse.json();
        console.log('Загруженные мастера:', masters); // Для отладки
        populateMasterDropdown(masters);
    } catch (error) {
        console.error('Ошибка при загрузке данных для выпадающих списков:', error);
    }
}

function populateServiceDropdown(services) {
    const dropdown = document.getElementById('serviceDropdown');
    dropdown.innerHTML = '<option value="">Выберите услугу</option>' +
        services.map(service => `
            <option value="${service.id}">${service.name}</option>
        `).join('');
}

function populateUserDropdown(users) {
    const dropdown = document.getElementById('userDropdown');

    dropdown.innerHTML = '<option value="">Выберите клиента</option>' +
        users.map(user => `
            <option value="${user.id}" 
                    data-first-name="${user.firstName}" 
                    data-last-name="${user.lastName}">
                ${user.firstName} ${user.lastName}
            </option>
        `).join('');
}

function populateMasterDropdown(masters) {
    const dropdown = document.getElementById('masterDropdown');

    dropdown.innerHTML = '<option value="">Выберите мастера</option>' +
        masters.map(master => `
            <option value="${master.id}" 
                    data-first-name="${master.user.firstName}" 
                    data-last-name="${master.user.lastName}">
                ${master.user.firstName} ${master.user.lastName}
            </option>
        `).join('');
}


async function loadAllAppointments() {
    try {
        const response = await fetch('/api/system-admin/appointments');
        if (!response.ok) throw new Error(`Ошибка загрузки записей: ${response.statusText}`);
        const appointments = await response.json();
        displayAppointments(appointments);
    } catch (error) {
        console.error('Ошибка при загрузке записей:', error);
        alert('Не удалось загрузить записи.');
    }
}

function displayAppointments(appointments) {
    const container = document.getElementById('all-appointments');
    if (!container) return;

    container.innerHTML = appointments.map(appointment => {
        const appointmentDate = new Date(appointment.appointmentDate);
        return `
            <div class="card mb-3">
                <div class="card-body">
                    <h5 class="card-title">
                        ${appointmentDate.toLocaleString()} - ${appointment.servicee.name}
                    </h5>
                    <p class="card-text">
                        Клиент: ${appointment.user.firstName} ${appointment.user.lastName}<br>
                        Мастер: ${appointment.master.user.firstName} ${appointment.master.user.lastName}
                    </p>
                    <button class="btn btn-primary" onclick="editAppointment(${appointment.id})">
                        Редактировать
                    </button>
                    <button class="btn btn-secondary" onclick="downloadAppointmentForm(${appointment.id})">
                        Скачать форму
                    </button>
                </div>
            </div>
        `;
    }).join('');
}

function editAppointment(appointmentId) {
    fetch(`/api/system-admin/appointments/${appointmentId}`)
        .then(response => {
            if (!response.ok) throw new Error('Ошибка загрузки');
            return response.json();
        })
        .then(appointment => {
            const form = document.getElementById('editForm');

            // Установка значений формы
            form.elements['id'].value = appointment.id;
            form.elements['appointmentDate'].value = appointment.appointmentDate.slice(0, 16);

            // Установка значений выпадающих списков
            form.elements['serviceId'].value = appointment.servicee.id;
            form.elements['userId'].value = appointment.user.id;
            form.elements['masterId'].value = appointment.master.id;

            const modal = new bootstrap.Modal(document.getElementById('editModal'));
            modal.show();
        })
        .catch(error => {
            console.error('Ошибка:', error);
            alert('Не удалось загрузить данные для редактирования');
        });
}

async function saveAppointment(event) {
    event.preventDefault();
    const form = event.target;

    const appointmentId = form.elements['id'].value;
    const serviceSelect = form.elements['serviceId'];
    const userSelect = form.elements['userId'];
    const masterSelect = form.elements['masterId'];

    const updatedAppointment = {
        appointmentDate: form.elements['appointmentDate'].value,
        serviceName: serviceSelect.options[serviceSelect.selectedIndex].text,
        userFirstName: userSelect.options[userSelect.selectedIndex].dataset.firstName,
        userLastName: userSelect.options[userSelect.selectedIndex].dataset.lastName,
        masterFirstName: masterSelect.options[masterSelect.selectedIndex].dataset.firstName,
        masterLastName: masterSelect.options[masterSelect.selectedIndex].dataset.lastName
    };

    try {
        const response = await fetch(`/api/system-admin/appointments/${appointmentId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(updatedAppointment)
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText || 'Ошибка при обновлении записи');
        }

        bootstrap.Modal.getInstance(document.getElementById('editModal')).hide();
        await loadAllAppointments();
        alert('Запись успешно обновлена!');
    } catch (error) {
        console.error('Ошибка при сохранении записи:', error);
        alert(`Не удалось сохранить изменения: ${error.message}`);
    }
}

async function downloadAppointmentForm(appointmentId) {
    try {
        const response = await fetch(`/api/system-admin/reports/appointment?appointmentId=${appointmentId}`);
        if (!response.ok) throw new Error('Ошибка при скачивании формы');

        const blob = await response.blob();
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = 'appointment-form.pdf';
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
        window.URL.revokeObjectURL(url);
    } catch (error) {
        console.error('Ошибка при скачивании формы записи:', error);
        alert('Произошла ошибка при скачивании формы записи');
    }
}
