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
        const card = document.createElement('div');
        card.className = 'card mb-3';
        card.innerHTML = `
            <div class="card-body">
                <h5 class="card-title">Отзыв от ${review.appointment.client.user.firstName}</h5>
                <p class="card-text">${review.reviewText}</p>
                <div class="rating">
                    ${'★'.repeat(review.rating)}${'☆'.repeat(5-review.rating)}
                </div>
                <small class="text-muted">
                    ${new Date(review.reviewDate).toLocaleDateString()}
                </small>
            </div>
        `;
        container.appendChild(card);
    });
}

async function submitReview(appointmentId, rating, text) {
    try {
        const response = await fetch('/api/reviews', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                appointmentId,
                rating,
                reviewText: text
            })
        });

        if (response.ok) {
            alert('Спасибо за ваш отзыв!');
            loadReviews();
        } else {
            alert('Ошибка при отправке отзыва');
        }
    } catch (error) {
        console.error('Ошибка:', error);
    }
}