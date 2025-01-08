async function loadMasters() {
    try {
        const response = await fetch('/api/masters');
        const masters = await response.json();
        displayMasters(masters);
    } catch (error) {
        console.error('Ошибка при загрузке мастеров:', error);
    }
}

function displayMasters(masters) {
    const container = document.getElementById('masters-container');
    container.innerHTML = '';

    masters.forEach(master => {
        const masterCard = document.createElement('div');
        masterCard.className = 'col-md-4 mb-4';
        masterCard.innerHTML = `
            <div class="card master-card h-100">
                <div class="card-body text-center">
                    <img src="${master.user.photo || 'img/default-avatar.jpg'}" 
                         alt="${master.user.firstName} ${master.user.lastName}"
                         class="mb-3">
                    <h5 class="card-title">${master.user.firstName} ${master.user.lastName}</h5>
                    <p class="card-text">
                        <strong>Специализация:</strong> ${master.specialization}<br>
                        <strong>График работы:</strong> ${master.work_schedule}
                    </p>
                    <button onclick="bookMaster(${master.id})" class="btn btn-primary">
                        Записаться
                    </button>
                </div>
            </div>
        `;
        container.appendChild(masterCard);
    });
}

async function bookMaster(masterId) {
    const isAuthenticated = await checkAuth();
    if (!isAuthenticated) {
        window.location.href = '/login';
        return;
    }
    window.location.href = `/appointment?master=${masterId}`;
}