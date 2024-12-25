document.addEventListener('DOMContentLoaded', function() {
    const urlParams = new URLSearchParams(window.location.search);
    const serviceId = urlParams.get('service');

    if (serviceId) {
        document.querySelector('select[name="serviceId"]').value = serviceId;
    }

    loadMasters();
    initializeAppointmentForm();
});

async function loadMasters() {
    try {
        const response = await fetch('/api/masters');
        const masters = await response.json();
        const select = document.querySelector('select[name="masterId"]');

        masters.forEach(master => {
            const option = document.createElement('option');
            option.value = master.id;
            option.textContent = `${master.user.firstName} ${master.user.lastName} - ${master.specialization}`;
            select.appendChild(option);
        });
    } catch (error) {
        console.error('Ошибка при загрузке мастеров:', error);
    }
}

function initializeAppointmentForm() {
    const form = document.getElementById('appointmentForm');
    form.addEventListener('submit', async (event) => {
        event.preventDefault();

        const formData = new FormData(form);
        const appointmentData = {
            serviceId: formData.get('serviceId'),
            masterId: formData.get('masterId'),
            appointmentDate: formData.get('appointmentDate')
        };

        try {
            const response = await fetch('/api/appointments', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(appointmentData)
            });

            if (response.ok) {
                alert('Запись успешно создана!');
                window.location.href = '/profile.html';
            } else {
                alert('Ошибка при создании записи');
            }
        } catch (error) {
            console.error('Ошибка:', error);
        }
    });
}