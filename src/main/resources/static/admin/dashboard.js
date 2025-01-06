// dashboard.js
document.addEventListener('DOMContentLoaded', function() {
    setupNavigation();
    loadTab('dashboard');
});

function setupNavigation() {
    const navLinks = document.querySelectorAll('.nav-link, .navbar-brand');
    navLinks.forEach(link => {
        link.addEventListener('click', (e) => {
            e.preventDefault();
            const tab = e.target.getAttribute('data-tab');
            loadTab(tab);
        });
    });
}

async function loadTab(tab) {
    const contentArea = document.getElementById('content-area');
    contentArea.innerHTML = '<div class="text-center"><div class="spinner-border" role="status"><span class="visually-hidden">Загрузка...</span></div></div>';

    try {
        switch(tab) {
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
                        <a href="#" class="btn btn-primary" data-tab="users">Перейти</a>
                    </div>
                </div>
            </div>
            <div class="col-md-3 mb-4">
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">Мастера</h5>
                        <p class="card-text">Управление мастерами и их расписанием</p>
                        <a href="#" class="btn btn-primary" data-tab="masters">Перейти</a>
                    </div>
                </div>
            </div>
            <div class="col-md-3 mb-4">
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">Услуги</h5>
                        <p class="card-text">Управление услугами салона</p>
                        <a href="#" class="btn btn-primary" data-tab="services">Перейти</a>
                    </div>
                </div>
            </div>
            <div class="col-md-3 mb-4">
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">Отчеты</h5>
                        <p class="card-text">Просмотр и генерация отчетов</p>
                        <a href="#" class="btn btn-primary" data-tab="reports">Перейти</a>
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
                            Роль: ${user.userRoles && user.userRoles[0] ? user.userRoles[0] : 'Не назначена'}
                        </p>
                        <select class="form-select mb-2" onchange="updateUserRole(${user.id}, this.value)">
                            <!--// <option value="CLIENT" ${user.roles && user.roles[0] === 'CLIENT' ? 'selected' : ''}>Клиент</option>
                            <option value="MASTER" ${user.roles && user.roles[0] === 'MASTER' ? 'selected' : ''}>Мастер</option>
                            <option value="ADMIN" ${user.roles && user.roles[0] === 'ADMIN' ? 'selected' : ''}>Администратор</option> 
                            <option value="SYSTEM_ADMIN" ${user.roles && user.roles[0] === 'SYSTEM_ADMIN' ? 'selected' : ''}>Системный администратор</option>!-->
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
                <label for="email" class="form-label">Email</label>
                <input type="email" class="form-control" id="email" required>
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
            password: document.getElementById('password').value
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
                <div class="mb-3">
                    <label for="firstName" class="form-label">Имя</label>
                    <input type="text" class="form-control" id="firstName" value="${user.firstName}" required>
                </div>
                <div class="mb-3">
                    <label for="lastName" class="form-label">Фамилия</label>
                    <input type="text" class="form-control" id="lastName" value="${user.lastName}" required>
                </div>
                <div class="mb-3">
                    <label for="email" class="form-label">Email</label>
                    <input type="email" class="form-control" id="email" value="${user.email}" required>
                </div>
                <div class="mb-3">
                    <label for="password" class="form-label">Новый пароль (оставьте пустым, если не хотите менять)</label>
                    <input type="password" class="form-control" id="password">
                </div>
                <button type="submit" class="btn btn-primary">Сохранить изменения</button>
            </form>
        `;

        document.getElementById('edit-user-form').addEventListener('submit', async (e) => {
            e.preventDefault();
            const updatedUserData = {
                firstName: document.getElementById('firstName').value,
                lastName: document.getElementById('lastName').value,
                email: document.getElementById('email').value,
                password: document.getElementById('password').value
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
            body: JSON.stringify({ role: newRole })
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
            ${masters.map(master => `
                <div class="card mb-3">
                    <div class="card-body">
                        <h5 class="card-title">${master.firstName} ${master.lastName}</h5>
                        <p class="card-text">
                            Специализация: ${master.specialization}<br>
                            Опыт работы: ${master.experience} лет
                        </p>
                        <button class="btn btn-primary" onclick="editMaster(${master.id})">Редактировать</button>
                        <button class="btn btn-danger" onclick="deleteMaster(${master.id})">Удалить</button>
                    </div>
                </div>
            `).join('')}
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
                <label for="experience" class="form-label">Опыт работы (лет)</label>
                <input type="number" class="form-control" id="experience" required>
            </div>
            <button type="submit" class="btn btn-primary">Добавить</button>
        </form>
    `;

    document.getElementById('add-master-form').addEventListener('submit', async (e) => {
        e.preventDefault();
        const masterData = {
            userId: document.getElementById('userId').value,
            specialization: document.getElementById('specialization').value,
            experience: document.getElementById('experience').value
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
                    <label for="experience" class="form-label">Опыт работы (лет)</label>
                    <input type="number" class="form-control" id="experience" value="${master.experience}" required>
                </div>
                <button type="submit" class="btn btn-primary">Сохранить изменения</button>
            </form>
        `;

        document.getElementById('edit-master-form').addEventListener('submit', async (e) => {
            e.preventDefault();
            const updatedMasterData = {
                specialization: document.getElementById('specialization').value,
                experience: document.getElementById('experience').value
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
                            Цена: ${service.price} руб.<br>
                            Длительность: ${service.duration} мин.
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
            <button type="submit" class="btn btn-primary">Добавить</button>
        </form>
    `;

    document.getElementById('add-service-form').addEventListener('submit', async (e) => {
        e.preventDefault();
        const serviceData = {
            name: document.getElementById('name').value,
            price: document.getElementById('price').value,
            duration: document.getElementById('duration').value
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
                <button type="submit" class="btn btn-primary">Сохранить изменения</button>
            </form>
        `;

        document.getElementById('edit-service-form').addEventListener('submit', async (e) => {
            e.preventDefault();
            const updatedServiceData = {
                name: document.getElementById('name').value,
                price: document.getElementById('price').value,
                duration: document.getElementById('duration').value
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

function loadReports() {
    const contentArea = document.getElementById('content-area');
    contentArea.innerHTML = `
        <h2>Отчеты</h2>
        <div class="mb-3">
            <label for="startDate" class="form-label">Начальная дата</label>
            <input type="date" class="form-control" id="startDate">
        </div>
        <div class="mb-3">
            <label for="endDate" class="form-label">Конечная дата</label>
            <input type="date" class="form-control" id="endDate">
        </div>
        <button class="btn btn-primary" onclick="generateServicesReport()">Сгенерировать отчет по услугам</button>
    `;
}

async function generateServicesReport() {
    const startDate = document.getElementById('startDate').value;
    const endDate = document.getElementById('endDate').value;

    if (!startDate || !endDate) {
        alert('Пожалуйста, выберите начальную и конечную даты');
        return;
    }

    try {
        const response = await fetch(`/api/admin/reports/services?startDate=${startDate}&endDate=${endDate}`);
        const blob = await response.blob();
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.style.display = 'none';
        a.href = url;
        a.download = 'services-report.pdf';
        document.body.appendChild(a);
        a.click();
        window.URL.revokeObjectURL(url);
    } catch (error) {
        console.error('Ошибка при генерации отчета:', error);
        alert('Произошла ошибка при генерации отчета');
    }
}