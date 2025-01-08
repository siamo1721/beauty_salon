document.addEventListener('DOMContentLoaded', function() {
    updateAuthUI();
    loadUserAppointments();
    loadReviews();
});

async function loadUserAppointments() {
    try {
        const response = await fetch('/api/appointments/my');
        const appointments = await response.json();
        displayUserAppointments(appointments);
    } catch (error) {
        console.error('Ошибка при загрузке записей пользователя:', error);
    }
}

function displayUserAppointments(appointments) {
    const container = document.getElementById('user-appointments');
    container.innerHTML = '';

    appointments.forEach(appointment => {
        const card = document.createElement('div');
        card.className = 'card mb-3';
        card.innerHTML = `
            <div class="card-body">
                <h5 class="card-title">Запись на ${new Date(appointment.appointmentDate).toLocaleString()}</h5>
                <p class="card-text">Услуга: ${appointment.servicee.name}</p>
                <p class="card-text">Статус: ${appointment.status}</p>
                ${appointment.status === 'COMPLETED' && !appointment.review ? `
                    <button class="btn btn-primary" onclick="showReviewModal(${appointment.id})">Оставить отзыв</button>
                ` : ''}
            </div>
        `;
        container.appendChild(card);
    });
}

function showReviewModal(appointmentId) {
    document.getElementById('appointmentId').value = appointmentId;
    const modal = new bootstrap.Modal(document.getElementById('reviewModal'));
    modal.show();
}

document.getElementById('reviewForm').addEventListener('submit', async function(e) {
    e.preventDefault();
    const appointmentId = document.getElementById('appointmentId').value;
    const rating = document.getElementById('rating').value;
    const reviewText = document.getElementById('reviewText').value;

    try {
        const response = await fetch('/api/reviews/create', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                appointmentId: appointmentId,
                rating: rating,
                reviewText: reviewText
            })
        });

        if (response.ok) {
            alert('Спасибо за ваш отзыв!');
            const modal = bootstrap.Modal.getInstance(document.getElementById('reviewModal'));
            modal.hide();
            loadUserAppointments();
            loadReviews();
        } else {
            const errorData = await response.json();
            alert(`Ошибка при отправке отзыва: ${errorData.message}`);
        }
    } catch (error) {
        console.error('Ошибка:', error);
        alert('Произошла ошибка при отправке отзыва');
    }
});

async function loadReviews() {
    try {
        const response = await fetch('/api/reviews');
        const reviews = await response.json();
        displayReviews(reviews);
    } catch (error) {
        console.error('Ошибка при загрузке отзывов:', error);
    }
}

function displayReviews(reviews) {
    const container = document.getElementById('reviews-container');
    container.innerHTML = '';

    reviews.forEach(review => {
        const master = review.appointment.master;
        const service = review.appointment.servicee;

        const card = document.createElement('div');
        card.className = 'card mb-3';
        card.innerHTML = `
            <div class="card-body">
                <h5 class="card-title">Отзыв от: ${review.appointment.user.firstName} ${review.appointment.user.lastName}</h5>
                <p><strong>Мастер:</strong> ${master.user.firstName} ${master.user.lastName} (${master.specialization})</p>
                <p><strong>Услуга:</strong> ${service.name}</p>
                <p class="card-text">${review.reviewText}</p>
                <div class="rating">
                    ${'★'.repeat(review.rating)}${'☆'.repeat(5 - review.rating)}
                </div>
                <small class="text-muted">
                    Дата отзыва: ${new Date(review.reviewDate).toLocaleDateString()}
                </small>
            </div>
        `;
        container.appendChild(card);
    });
}