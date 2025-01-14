document.addEventListener('DOMContentLoaded', function () {
    setupNavigation();
    loadTab('dashboard');
});

function setupNavigation() {
    const navLinks = document.querySelectorAll('.nav-link, .navbar-brand, [data-tab]');
    navLinks.forEach(link => {
        link.addEventListener('click', (e) => {
            e.preventDefault();
            const tab = e.target.getAttribute('data-tab');
            if (tab) {
                loadTab(tab);
            }
        });
    });
}
async function loadTab(tab) {
    const contentArea = document.getElementById('content-area');
    contentArea.innerHTML = '<div class="text-center"><div class="spinner-border" role="status"><span class="visually-hidden">Загрузка...</span></div></div>';

    try {
        switch (tab) {
            case 'dashboard':
                loadDashboard();
                break;
            case 'users':
                await loadUsers();
                break;
            case 'masters':
                await loadMasters();
                break;
            case 'services':
                await loadServices();
                break;
            case 'reports':
                loadReports();
                break;
            default:
                contentArea.innerHTML = '<div class="alert alert-danger">Неизвестная вкладка</div>';
        }
    } catch (error) {
        console.error(`Ошибка при загрузке вкладки ${tab}:`, error);
        contentArea.innerHTML = `<div class="alert alert-danger">Ошибка при загрузке данных</div>`;
    }
}

function loadDashboard() {
    const contentArea = document.getElementById('content-area');
    contentArea.innerHTML = `
        <h2 class="mb-4">Панель управления</h2>
        <div class="row">
            <div class="col-md-3 mb-4">
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">Пользователи</h5>
                        <p class="card-text">Управление пользователями и ролями</p>
                        <button class="btn btn-primary" data-tab="users">Перейти</button>
                    </div>
                </div>
            </div>
            <div class="col-md-3 mb-4">
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">Мастера</h5>
                        <p class="card-text">Управление мастерами и их расписанием</p>
                        <button class="btn btn-primary" data-tab="masters">Перейти</button>
                    </div>
                </div>
            </div>
            <div class="col-md-3 mb-4">
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">Услуги</h5>
                        <p class="card-text">Управление услугами салона</p>
                        <button class="btn btn-primary" data-tab="services">Перейти</button>
                    </div>
                </div>
            </div>
            <div class="col-md-3 mb-4">
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">Отчеты</h5>
                        <p class="card-text">Просмотр и генерация отчетов</p>
                        <button class="btn btn-primary" data-tab="reports">Перейти</button>
                    </div>
                </div>
            </div>
        </div>
    `;
    setupNavigation();
}

async function loadUsers() {
    const response = await fetch('/api/admin/users');
    const users = await response.json();
    displayUsers(users);
}

function displayUsers(users) {
    const contentArea = document.getElementById('content-area');
    if (!users || users.length === 0) {
        contentArea.innerHTML = `
            <h2 class="mb-4">Управление пользователями</h2>
            <div class="alert alert-info" role="alert">
                Пользователи не найдены.
            </div>
            <button class="btn btn-success mb-3" onclick="showAddUserForm()">Добавить пользователя</button>
        `;
        return;
    }

    contentArea.innerHTML = `
        <h2 class="mb-4">Управление пользователями</h2>
        <button class="btn btn-success mb-3" onclick="showAddUserForm()">Добавить пользователя</button>
        <div id="users-list">
            ${users.map(user => `
                <div class="card mb-3">
                    <div class="card-body">
                        <h5 class="card-title">${user.firstName || ''} ${user.lastName || ''}</h5>
                        <p class="card-text">
                            Email: ${user.email || 'Не указан'}<br>
                            Роль: ${user.userRoles && user.userRoles.length > 0 ? user.userRoles[0].userAuthority : 'Не назначена'}
                        </p>
                        <select class="form-select mb-2" onchange="updateUserRole(${user.id}, this.value)">
                            <option value="CLIENT" ${user.userRoles[0]?.userAuthority === 'CLIENT' ? 'selected' : ''}>Клиент</option>
                            <option value="MASTER" ${user.userRoles[0]?.userAuthority === 'MASTER' ? 'selected' : ''}>Мастер</option>
                            <option value="ADMIN" ${user.userRoles[0]?.userAuthority === 'ADMIN' ? 'selected' : ''}>Администратор</option>
                            <option value="SYSTEM_ADMIN" ${user.userRoles[0]?.userAuthority === 'SYSTEM_ADMIN' ? 'selected' : ''}>Системный администратор</option>
                        </select>
                        <button class="btn btn-primary" onclick="editUser(${user.id})">Редактировать</button>
                        <button class="btn btn-danger" onclick="deleteUser(${user.id})">Удалить</button>
                    </div>
                </div>
            `).join('')}
        </div>
    `;
}

function showAddUserForm() {
    const contentArea = document.getElementById('content-area');
    contentArea.innerHTML = `
        <h2>Добавить пользователя</h2>
        <form id="add-user-form">
            <div class="mb-3">
                <label for="firstName" class="form-label">Имя</label>
                <input type="text" class="form-control" id="firstName" required>
            </div>
            <div class="mb-3">
                <label for="lastName" class="form-label">Фамилия</label>
                <input type="text" class="form-control" id="lastName" required>
            </div>
             <div class="mb-3">
                <label for="phone" class="form-label">Телефон</label>
                <input type="tel" class="form-control" id="phone" required>
            </div>
            <div class="mb-3">
                <label for="email" class="form-label">Email</label>
                <input type="email" class="form-control" id="email" required>
            </div>
             <div class="mb-3">
                <label for="username" class="form-label">Имя пользователя</label>
                <input type="text" class="form-control" id="username" required>
            </div>
            <h4 class="mb-3">Адрес</h4>
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="city" class="form-label">Город</label>
                                <input type="text" class="form-control" id="city" required>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="street" class="form-label">Улица</label>
                                <input type="text" class="form-control" id="street" required>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="house" class="form-label">Дом</label>
                                <input type="text" class="form-control" id="house" required>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="apartment" class="form-label">Квартира</label>
                                <input type="text" class="form-control" id="apartment" required>
                            </div>
                        </div>
            <div class="mb-3">
                <label for="birth_date" class="form-label">День рождения</label>
                <input type="date" class="form-control" id="birth_date" required>
            </div>
            <div class="mb-3">
                <label for="password" class="form-label">Пароль</label>
                <input type="password" class="form-control" id="password" required>
            </div>
            <button type="submit" class="btn btn-primary">Добавить</button>
        </form>
    `;

    document.getElementById('add-user-form').addEventListener('submit', async (e) => {
        e.preventDefault();
        const userData = {
            firstName: document.getElementById('firstName').value,
            lastName: document.getElementById('lastName').value,
            email: document.getElementById('email').value,
            password: document.getElementById('password').value,
            username: document.getElementById('username').value,
            phone: document.getElementById('phone').value,
            birthDate: document.getElementById('birth_date').value,
            address: {
                city: document.getElementById('city').value,
                street: document.getElementById('street').value,
                house: document.getElementById('house').value,
                apartment: document.getElementById('apartment').value
            }
        };
        await addUser(userData);
    });
}

async function addUser(userData) {
    try {
        const response = await fetch('/api/auth/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(userData)
        });
        if (response.ok) {
            await loadUsers();
        } else {
            throw new Error('Failed to add user');
        }
    } catch (error) {
        console.error('Error adding user:', error);
        alert('Ошибка при добавлении пользователя');
    }
}

async function editUser(userId) {
    try {
        const response = await fetch(`/api/admin/users/${userId}`);
        const user = await response.json();
        const contentArea = document.getElementById('content-area');
        contentArea.innerHTML = `
            <h2>Редактировать пользователя</h2>
            <form id="edit-user-form">
                <label for="firstName" class="form-label">Имя</label>
                <input type="text" class="form-control" id="firstName" required>
            </div>
            <div class="mb-3">
                <label for="lastName" class="form-label">Фамилия</label>
                <input type="text" class="form-control" id="lastName" required>
            </div>
             <div class="mb-3">
                <label for="phone" class="form-label">Телефон</label>
                <input type="tel" class="form-control" id="phone" required>
            </div>
            <div class="mb-3">
                <label for="email" class="form-label">Email</label>
                <input type="email" class="form-control" id="email" required>
            </div>
             <div class="mb-3">
                <label for="username" class="form-label">Имя пользователя</label>
                <input type="text" class="form-control" id="username" required>
            </div>
            <h4 class="mb-3">Адрес</h4>
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="city" class="form-label">Город</label>
                                <input type="text" class="form-control" id="city" required>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="street" class="form-label">Улица</label>
                                <input type="text" class="form-control" id="street" required>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="house" class="form-label">Дом</label>
                                <input type="text" class="form-control" id="house" required>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="apartment" class="form-label">Квартира</label>
                                <input type="text" class="form-control" id="apartment" required>
                            </div>
                        </div>
            <div class="mb-3">
                <label for="birth_date" class="form-label">День рождения</label>
                <input type="date" class="form-control" id="birth_date" required>
            </div>
            <div class="mb-3">
                <label for="registration_date" class="form-label">Дата регистрации</label>
                <input type="date" class="form-control" id="registration_date" required>
            </div>
                <div class="mb-3">
                    <label for="password" class="form-label">Новый пароль (оставьте пустым, если не хотите менять)</label>
                    <input type="password" class="form-control" id="password">
                </div>
                <button type="submit" class="btn btn-primary">Сохранить изменения</button>
            </form>
        `;
        const today = new Date();
        const offset = today.getTimezoneOffset();
        const moscowOffset = 180;
        today.setMinutes(today.getMinutes() + offset + moscowOffset);

        const formattedDate = today.toISOString().split('T')[0];

        document.getElementById('registration_date').value = formattedDate;

        document.getElementById('edit-user-form').addEventListener('submit', async (e) => {
            e.preventDefault();
            const updatedUserData = {
                firstName: document.getElementById('firstName').value,
                lastName: document.getElementById('lastName').value,
                email: document.getElementById('email').value,
                password: document.getElementById('password').value || null,
                username: document.getElementById('username').value,
                phone: document.getElementById('phone').value,
                birthDate: document.getElementById('birth_date').value,
                registrationDate: document.getElementById('registration_date').value,
                address: {
                    city: document.getElementById('city').value,
                    street: document.getElementById('street').value,
                    house: document.getElementById('house').value,
                    apartment: document.getElementById('apartment').value
                }
            };
            await updateUser(userId, updatedUserData);
        });
    } catch (error) {
        console.error('Error fetching user data:', error);
        alert('Ошибка при загрузке данных пользователя');
    }
}

async function updateUser(userId, userData) {
    try {
        const response = await fetch(`/api/admin/users/${userId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(userData)
        });
        if (response.ok) {
            await loadUsers();
        } else {
            throw new Error('Failed to update user');
        }
    } catch (error) {
        console.error('Error updating user:', error);
        alert('Ошибка при обновлении пользователя');
    }
}

async function deleteUser(userId) {
    if (confirm('Вы уверены, что хотите удалить этого пользователя?')) {
        try {
            const response = await fetch(`/api/admin/users/${userId}`, {
                method: 'DELETE'
            });
            if (response.ok) {
                await loadUsers();
            } else {
                throw new Error('Failed to delete user');
            }
        } catch (error) {
            console.error('Error deleting user:', error);
            alert('Ошибка при удалении пользователя');
        }
    }
}

async function updateUserRole(userId, newRole) {
    try {
        await fetch(`/api/admin/users/${userId}/role`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({role: newRole})
        });
        await loadUsers();
    } catch (error) {
        console.error('Ошибка при обновлении роли:', error);
        alert('Ошибка при обновлении роли пользователя');
    }
}

async function loadMasters() {
    const response = await fetch('/api/admin/masters');
    const masters = await response.json();
    displayMasters(masters);
}

function displayMasters(masters) {
    const contentArea = document.getElementById('content-area');
    contentArea.innerHTML = `
        <h2 class="mb-4">Управление мастерами</h2>
        <button class="btn btn-success mb-3" onclick="showAddMasterForm()">Добавить мастера</button>
        <div id="masters-list">
            ${masters.map(master => {
        return `
            <div class="card mb-3">
                <div class="card-body">
                    <h5 class="card-title">${master.firstName} ${master.lastName}</h5>
                    <p class="card-text">
                        Специализация: ${master.specialization}<br>
                        Режим работы: ${master.workSchedule} 
                    </p>
                    <button class="btn btn-primary" onclick="editMaster(${master.id})">Редактировать</button>
                    <button class="btn btn-danger" onclick="deleteMaster(${master.id})">Удалить</button>
                </div>
            </div>
        `;
    }).join('')}
        </div>
    `;
}

function showAddMasterForm() {
    const contentArea = document.getElementById('content-area');
    contentArea.innerHTML = `
        <h2>Добавить мастера</h2>
        <form id="add-master-form">
            <div class="mb-3">
                <label for="userId" class="form-label">ID пользователя</label>
                <input type="number" class="form-control" id="userId" required>
            </div>
            <div class="mb-3">
                <label for="specialization" class="form-label">Специализация</label>
                <input type="text" class="form-control" id="specialization" required>
            </div>
            <div class="mb-3">
                <label for="work_schedule" class="form-label">График работы</label>
                <input type="text" class="form-control" id="workSchedule" placeholder="Например: ПН-ПТ 9:00-18:00" required pattern="^[А-Яа-яA-Za-z]+-[А-Яа-яA-Za-z]+ \\d{1,2}:\\d{2}-\\d{1,2}:\\d{2}$" required>
            </div>
            <button type="submit" class="btn btn-primary">Добавить</button>
        </form>
    `;

    document.getElementById('add-master-form').addEventListener('submit', async (e) => {
        e.preventDefault();
        const masterData = {
            userId: document.getElementById('userId').value,
            specialization: document.getElementById('specialization').value,
            workSchedule: document.getElementById('workSchedule').value
        };
        await addMaster(masterData);
    });
}

async function addMaster(masterData) {
    try {
        const response = await fetch('/api/admin/masters', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(masterData)
        });
        if (response.ok) {
            await loadMasters();
        } else {
            throw new Error('Failed to add master');
        }
    } catch (error) {
        console.error('Error adding master:', error);
        alert('Ошибка при добавлении мастера');
    }
}

async function editMaster(masterId) {
    try {
        const response = await fetch(`/api/admin/masters/${masterId}`);
        const master = await response.json();
        const contentArea = document.getElementById('content-area');
        contentArea.innerHTML = `
            <h2>Редактировать мастера</h2>
            <form id="edit-master-form">
                <div class="mb-3">
                    <label for="specialization" class="form-label">Специализация</label>
                    <input type="text" class="form-control" id="specialization" value="${master.specialization}" required>
                </div>
                <div class="mb-3">
                    <label for="work_schedule" class="form-label">Режим работы </label>
                    <input type="text" class="form-control" id="workSchedule" value="${master.workSchedule}" placeholder="Например: ПН-ПТ 9:00-18:00" required pattern="^[А-Яа-яA-Za-z]+-[А-Яа-яA-Za-z]+ \\d{1,2}:\\d{2}-\\d{1,2}:\\d{2}$" required>
                </div>
                <button type="submit" class="btn btn-primary">Сохранить изменения</button>
            </form>
        `;

        document.getElementById('edit-master-form').addEventListener('submit', async (e) => {
            e.preventDefault();
            const updatedMasterData = {
                specialization: document.getElementById('specialization').value,
                workSchedule: document.getElementById('workSchedule').value
            };
            await updateMaster(masterId, updatedMasterData);
        });
    } catch (error) {
        console.error('Error fetching master data:', error);
        alert('Ошибка при загрузке данных мастера');
    }
}

async function updateMaster(masterId, masterData) {
    try {
        const response = await fetch(`/api/admin/masters/${masterId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(masterData)
        });
        if (response.ok) {
            await loadMasters();
        } else {
            throw new Error('Failed to update master');
        }
    } catch (error) {
        console.error('Error updating master:', error);
        alert('Ошибка при обновлении мастера');
    }
}

async function deleteMaster(masterId) {
    if (confirm('Вы уверены, что хотите удалить этого мастера?')) {
        try {
            const response = await fetch(`/api/admin/masters/${masterId}`, {
                method: 'DELETE'
            });
            if (response.ok) {
                await loadMasters();
            } else {
                throw new Error('Failed to delete master');
            }
        } catch (error) {
            console.error('Error deleting master:', error);
            alert('Ошибка при удалении мастера');
        }
    }
}

async function loadServices() {
    const response = await fetch('/api/admin/services');
    const services = await response.json();
    displayServices(services);
}

function displayServices(services) {
    const contentArea = document.getElementById('content-area');
    contentArea.innerHTML = `
        <h2 class="mb-4">Управление услугами</h2>
        <button class="btn btn-success mb-3" onclick="showAddServiceForm()">Добавить услугу</button>
        <div id="services-list">
            ${services.map(service => `
                <div class="card mb-3">
                    <div class="card-body">
                        <h5 class="card-title">${service.name}</h5>
                        <p class="card-text">
                            Название: ${service.name} <br>
                            Цена: ${service.price} руб. <br>
                            Длительность: ${service.duration} мин. <br>
                            Описание : ${service.description}
                        </p>
                        <button class="btn btn-primary" onclick="editService(${service.id})">Редактировать</button>
                        <button class="btn btn-danger" onclick="deleteService(${service.id})">Удалить</button>
                    </div>
                </div>
            `).join('')}
        </div>
    `;
}

function showAddServiceForm() {
    const contentArea = document.getElementById('content-area');
    contentArea.innerHTML = `
        <h2>Добавить услугу</h2>
        <form id="add-service-form">
            <div class="mb-3">
                <label for="name" class="form-label">Название услуги</label>
                <input type="text" class="form-control" id="name" required>
            </div>
            <div class="mb-3">
                <label for="price" class="form-label">Цена (руб.)</label>
                <input type="number" class="form-control" id="price" required>
            </div>
            <div class="mb-3">
                <label for="duration" class="form-label">Длительность (мин.)</label>
                <input type="number" class="form-control" id="duration" required>
            </div>
            <div class="mb-3">
                <label for="description" class="from-lable">Описание </label>
                 <input type="text" class="form-control" id="description" required>
            </div>
            <button type="submit" class="btn btn-primary">Добавить</button>
        </form>
    `;

    document.getElementById('add-service-form').addEventListener('submit', async (e) => {
        e.preventDefault();
        const serviceData = {
            name: document.getElementById('name').value,
            price: document.getElementById('price').value,
            duration: document.getElementById('duration').value,
            description: document.getElementById('description').value
        };
        await addService(serviceData);
    });
}

async function addService(serviceData) {
    try {
        const response = await fetch('/api/admin/services', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(serviceData)
        });
        if (response.ok) {
            await loadServices();
        } else {
            throw new Error('Failed to add service');
        }
    } catch (error) {
        console.error('Error adding service:', error);
        alert('Ошибка при добавлении услуги');
    }
}

async function editService(serviceId) {
    try {
        const response = await fetch(`/api/admin/services/${serviceId}`);
        const service = await response.json();
        const contentArea = document.getElementById('content-area');
        contentArea.innerHTML = `
            <h2>Редактировать услугу</h2>
            <form id="edit-service-form">
                <div class="mb-3">
                    <label for="name" class="form-label">Название услуги</label>
                    <input type="text" class="form-control" id="name" value="${service.name}" required>
                </div>
                <div class="mb-3">
                    <label for="price" class="form-label">Цена (руб.)</label>
                    <input type="number" class="form-control" id="price" value="${service.price}" required>
                </div>
                <div class="mb-3">
                    <label for="duration" class="form-label">Длительность (мин.)</label>
                    <input type="number" class="form-control" id="duration" value="${service.duration}" required>
                </div>
                <div class="mb-3">
                    <label for="description" class="from-lable">Описание </label>
                    <input type="text" class="form-control" id="description" value="${service.description}" required>
                </div>
                <button type="submit" class="btn btn-primary">Сохранить изменения</button>
            </form>
        `;

        document.getElementById('edit-service-form').addEventListener('submit', async (e) => {
            e.preventDefault();
            const updatedServiceData = {
                name: document.getElementById('name').value,
                price: document.getElementById('price').value,
                duration: document.getElementById('duration').value,
                description: document.getElementById('description').value
            };
            await updateService(serviceId, updatedServiceData);
        });
    } catch (error) {
        console.error('Error fetching service data:', error);
        alert('Ошибка при загрузке данных услуги');
    }
}

async function updateService(serviceId, serviceData) {
    try {
        const response = await fetch(`/api/admin/services/${serviceId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(serviceData)
        });
        if (response.ok) {
            await loadServices();
        } else {
            throw new Error('Failed to update service');
        }
    } catch (error) {
        console.error('Error updating service:', error);
        alert('Ошибка при обновлении услуги');
    }
}

async function deleteService(serviceId) {
    if (confirm('Вы уверены, что хотите удалить эту услугу?')) {
        try {
            const response = await fetch(`/api/admin/services/${serviceId}`, {
                method: 'DELETE'
            });
            if (response.ok) {
                await loadServices();
            } else {
                throw new Error('Failed to delete service');
            }
        } catch (error) {
            console.error('Error deleting service:', error);
            alert('Ошибка при удалении услуги');
        }
    }
}


async function downloadProceduresReport() {
    const startDate = document.getElementById('startDate').value;
    const endDate = document.getElementById('endDate').value;

    if (!startDate || !endDate) {
        alert('Пожалуйста, выберите начальную и конечную даты');
        return;
    }

    const startDateTime = new Date(startDate).toISOString();
    const endDateTime = new Date(endDate + 'T23:59:59').toISOString();

    try {
        const response = await fetch(`/api/admin/reports/procedures?startDate=${startDateTime}&endDate=${endDateTime}`);

        if (!response.ok) {
            const errorText = await response.text();
            console.error('Server error:', errorText);
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const contentType = response.headers.get('content-type');
        if (contentType && contentType.indexOf('application/pdf') === -1) {
            console.error('Received non-PDF response:', await response.text());
            throw new Error('Сервер вернул не PDF файл');
        }

        const blob = await response.blob();
        if (blob.size < 1000) {
            console.error('Received suspiciously small PDF:', await blob.text());
            throw new Error('Полученный PDF файл подозрительно мал');
        }

        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.style.display = 'none';
        a.href = url;
        a.download = 'procedures-report.pdf';
        document.body.appendChild(a);
        a.click();
        window.URL.revokeObjectURL(url);
    } catch (error) {
        console.error('Ошибка при скачивании отчета:', error);
        alert('Произошла ошибка при скачивании отчета: ' + error.message);
    }
}

async function downloadClientsReport() {
    try {
        const response = await fetch('/api/admin/reports/clients');
        const blob = await response.blob();
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.style.display = 'none';
        a.href = url;
        a.download = 'clients-report.pdf';
        document.body.appendChild(a);
        a.click();
        window.URL.revokeObjectURL(url);
    } catch (error) {
        console.error('Ошибка при скачивании отчета:', error);
        alert('Произошла ошибка при скачивании отчета');
    }
}
function loadReports() {
    const contentArea = document.getElementById('content-area');
    contentArea.innerHTML = `
            <div id="reports-section">
           <h2>Отчеты</h2>
            <div className="mb-3">
            <label htmlFor="startDate" className="form-label">Начальная дата</label>
            <input type="date" className="form-control" id="startDate"/>
        </div>
        <div className="mb-3">
            <label htmlFor="endDate" className="form-label">Конечная дата</label>
            <input type="date" className="form-control" id="endDate"/>
        </div>
        <button className="btn btn-primary" onClick="downloadProceduresReport()">Скачать отчет по процедурам</button>
        <button className="btn btn-primary" onClick="downloadClientsReport()">Скачать отчет по клиентам</button>
    </div>
    `;
}

