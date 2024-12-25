document.addEventListener('DOMContentLoaded', function() {
    loadServices();
});

async function loadServices() {
    try {
        const response = await fetch('/api/services');
        const services = await response.json();
        displayServices(services);
    } catch (error) {
        console.error('Ошибка при загрузке услуг:', error);
    }
}

function displayServices(services) {
    const container = document.getElementById('services-container');
    container.innerHTML = '';

    services.forEach(service => {
        const serviceCard = document.createElement('div');
        serviceCard.className = 'col-md-4 mb-4';
        serviceCard.innerHTML = `
            <div class="card h-100">
                <div class="card-body">
                    <h5 class="card-title">${service.name}</h5>
                    <p class="card-text">${service.description}</p>
                    <p class="card-text">
                        <small class="text-muted">
                            Длительность: ${service.duration} мин<br>
                            Цена: ${service.price} ₽
                        </small>
                    </p>
                    <button onclick="bookService(${service.id})" class="btn btn-primary">Записаться</button>
                </div>
            </div>
        `;
        container.appendChild(serviceCard);
    });
}

async function bookService(serviceId) {
    const isAuthenticated = await checkAuth();
    if (!isAuthenticated) {
        window.location.href = '/login.html';
        return;
    }
    window.location.href = `/appointment.html?service=${serviceId}`;
}