// Variables globales
const API_BASE_URL = 'http://localhost:8080';
let currentOption = 'welcome'; // Pestaña actual

// Funciones para manejar el servidor

// Función para mostrar mensajes de estado
function showStatus(message, type) {
    const statusElement = document.getElementById('status-message');
    statusElement.textContent = message;
    statusElement.className = 'status ' + type;
    
    // Ocultar el mensaje después de 5 segundos
    setTimeout(() => {
        statusElement.className = 'status';
        statusElement.textContent = '';
    }, 5000);
}

// Función para iniciar sesión
async function loginUser(cedula, password) {
    const response = await fetch(`${API_BASE_URL}/login`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            cedula,
            password
        })
    });
    
    const data = await response.json();
    
    
    currentOption = 'welcome';
    return data;
}
// Funciones para registrar un nuevo usuario
async function registerUserData(nombre, cedula,password, tipo) {
    const response = await fetch(`${API_BASE_URL}/register`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            nombre,
            cedula,
            password,
            tipo
        })
    });
    
    const data = await response.json();
    
    
    if (!response.ok || data.error) {
        throw new Error(data.error || 'El usuario con la cedula ' + cedula + ' ya existe');
    }
    
    return data;
}
// Función para eliminar un usuario
async function deleteUser(cedula,accion) {
    const response = await fetch(`${API_BASE_URL}/deleteUser`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            cedula,
            accion
        })
    });
    
    const data = await response.json();
    
    showStatus('Operacion exitosamente', 'success');
    fillTableUsers();
    
    if (!response.ok || data.error) {
        throw new Error(data.error || 'Error al eliminar usuario');
    }
    
    return data;
}
async function changePasswordUserData(cedula, password) {
    const response = await fetch(`${API_BASE_URL}/changePassword`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            cedula,
            password
        })
    });
    console.log(response);
    const data = await response.json();
    
    if (!response.ok || data.error) {
        throw new Error(data.error || 'Error al cambiar la contrasena');
    }
    
    return data;
}
//Funcion para cambiar la contrasena de un usuario
function changePasswordUser(cedula) {
    document.getElementById('change-password').classList.add('active');
    document.getElementById('change-password-form').addEventListener('submit', async function(e) {
        e.preventDefault();
        
        const password = document.getElementById('new-password').value;
        console.log(password)
        const passwordConfirm = document.getElementById('confirm-new-password').value;
        
        try {
            while (password !== passwordConfirm) {
                throw new Error('Las contraseñas no coinciden');
            }
            const response = await changePasswordUserData(cedula, password);
            if(response.success){
                showStatus('Contrasena cambiada exitosamente.', 'success');
                document.getElementById('change-password-form').reset();
                document.getElementById('change-password').classList.remove('active');
            }
        } catch (error) {
            showStatus('Error al cambiar la contrasena: ' + error.message, 'error');
        }
    });
    document.getElementById('cancel-change-password').addEventListener('click', function() {
        document.getElementById('change-password').classList.remove('active');
        document.getElementById('change-password-form').reset();
    })
    
    }

// Función para llenar la tabla de usuarios
async function fillTableUsers() {
    let accionEstado='';
    try {
        const response = await fetch(`${API_BASE_URL}/users`,{
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        const data = await response.json();
        if (!response.ok || data.error) {
            throw new Error(data.error || 'Error al cargar usuarios');
        }
        // Limpiar tabla
        const tbody = document.querySelector('#users-table tbody');
        tbody.innerHTML = '';

        // Agregar usuarios a la tabla
        if (data.data && data.data.length > 0) {
            data.data.forEach(user => {
                if(user.estado ==='Activo'){
                    accionEstado = 'Desactivar';
                }else{
                    accionEstado = 'Activar';
                }
                const tr = document.createElement('tr');
                tr.innerHTML = `
                    <td>${user.cedula}</td>
                    <td>${user.nombre || 'N/A'}</td>
                    <td>${user.estado || 'N/A'}</td>
                    <td>${user.tipo || 'N/A'}</td>
                    <td><button class="table-button delete-button" data-cedula="${user.cedula}" onclick="changePasswordUser(${user.cedula})"><img src="draft-line.svg" alt="Cambiar contraseña"></button>
                    <button class="table-button desactivate-button" data-cedula="${user.cedula}" onclick="deleteUser(${user.cedula}, '${accionEstado}')"><img src="user-forbid-line.svg" alt="${accionEstado}"></button>
                    <button class="table-button delete-button" data-cedula="${user.cedula}" onclick="deleteUser(${user.cedula}, 'Eliminar')"><img src="delete-bin-line.svg" alt="Borrar"></button></td>
                `;
                tbody.appendChild(tr);
            });
        } else {
            const tr = document.createElement('tr');
            tr.innerHTML = '<td colspan="5" style="text-align: center;">No hay usuarios registrados</td>';
            tbody.appendChild(tr);
        }
        //showStatus('Usuarios cargados exitosamente', 'success');
    } catch (error) {
        showStatus('Error al cargar usuarios: ' + error.message, 'error');
    }
    
}

//Funcion para obtener el numero de ip
async function getIP() {
    try {
        const response = await fetch(`${API_BASE_URL}/ipKey`,{
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        const data = await response.json();
        if (!response.ok || data.error) {
            throw new Error(data.error || 'Error al obtener la IP');
        }
        
        return data;

    } catch (error) {
        showStatus('Error al cargar usuarios: ' + error.message, 'error');
    }
}

// Función para exportar datos a un archivo CSV
async function exportToCsv() {
    const userData = JSON.parse(sessionStorage.getItem('userAuth'));
    try {
        const response = await fetch(`${API_BASE_URL}/exportCSV`,{
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
            "nombre": userData.nombre,
            "cedula": userData.cedula,
            "tipo": userData.tipo
        })
        });
        const data = await response.json();
        if (!response.ok || data.error) {
            throw new Error(data.error || 'Error al exportar datos');
        }
        
        // Crear y descargar el archivo CSV
        const blob = new Blob([data.data], { type: 'text/csv' });
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = 'Datos_de_formularios.csv';
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
        URL.revokeObjectURL(url);
        
        showStatus('Datos exportados exitosamente', 'success');
    } catch (error) {
        showStatus('Error al exportar datos: ' + error.message, 'error');
    }
}

async function getForms(){
    try {
        const response = await fetch(`${API_BASE_URL}/getForms`,{
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        const data = await response.json();
        if (!response.ok || data.error) {
            throw new Error(data.error || 'Error al cargar usuarios');
        }
        document.getElementById('export-message').innerHTML = 'El numero de formularios en el sistemas es: '+ data.forms;
        return data;
    } catch (error) {
        showStatus('Error al cargar usuarios: ' + error.message, 'error');
    }
}

async function deleteFormsDB() {
    try {
        const response = await fetch(`${API_BASE_URL}/deleteFormsDB`,{
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        const data = await response.json();
        if (!response.ok || data.error) {
            throw new Error(data.error || 'Error al obtener los formularios');
        }
        getForms();
        return data;
    } catch (error) {
        showStatus('Error al obtener los formularios: ' + error.message, 'error');
    }
}
async function openedTime() {
    try {
        const response = await fetch(`${API_BASE_URL}/openedTime`,{
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        const data = await response.json();
        
        return data;
    } catch (error) {
        showStatus('Error al obtener los formularios: ' + error.message, 'error');
    }
}
//Enlace con el HTML

setInterval(async () => {
    const data = await openedTime();
    console.log(data);
}, 5000);

// Esperar a que el DOM esté cargado
document.addEventListener('DOMContentLoaded', function() {
    //history.pushState({}, '', '/');
    // Manejar cambios de pestaña
    document.querySelectorAll('.option-menu').forEach(optionMenu => {
        optionMenu.addEventListener('click', async function() {
            // Mejorar la logica de cerrar sesión

            if (this.dataset.option ==="logout") {
                document.getElementById('logout-section').classList.add('active');
                document.getElementById('logout-button').addEventListener('click', function() {
                    window.location.href = 'login.html';
                    sessionStorage.removeItem('userAuth');
                });
                document.getElementById('cancel-logout-button').addEventListener('click', function() {
                    document.getElementById('logout-section').classList.remove('active');
                });
            }else if(this.dataset.option ==="code-sync"){
                document.getElementById('code-sync').classList.add('active');
                const data = await getIP();
                document.getElementById('code-sync-message').innerHTML = data.key;
                document.getElementById('cancel-code-sync-button').addEventListener('click', function() {
                    document.getElementById('code-sync').classList.remove('active');
                });
            }else if(this.dataset.option ==="about"){
                document.getElementById('about').classList.add('active');
                document.getElementById('cancel-about-button').addEventListener('click', function() {
                    document.getElementById('about').classList.remove('active');
                });
            }else {
                document.querySelector(`.option-menu[data-option="${currentOption}"]`).classList.remove('active');
                document.getElementById(currentOption).classList.remove('active');
                    
                // Activar nueva pestaña
                currentOption = this.dataset.option;
                this.classList.add('active');
                document.getElementById(currentOption).classList.add('active');
                
                // Cargar usuarios inicialmente si es necesario
                if (currentOption === 'users') {
                    fillTableUsers();
                }
                if (currentOption === 'export') {
                    getForms();
                }
            }
        });
    });

    // Manejar formulario de inicio de sesión
    if(window.location.pathname.split("/").pop() === "login.html") {
        document.getElementById('login-form').addEventListener('submit', async function(e) {
            e.preventDefault();
            
            const cedula = document.getElementById('cedula').value;
            const password = document.getElementById('password').value;
            
            try {
                const response = await loginUser(cedula, password);
                if (response.failed === false) {
                    showStatus("Los datos que ingresaste son incorrectos. Por favor, inténtalo de nuevo", 'error');
                }else if (response.tipo === 'Investigador') {
                    sessionStorage.setItem('userAuth', JSON.stringify(response));
                    // Redirigir a la página principal
                    window.location.href = 'index.html';
                    currentOption = 'welcome';
                    document.querySelector(`.option-menu[data-option="${currentOption}"]`).classList.add('active');
                    document.getElementById(currentOption).classList.add('active');
                } else {
                    showStatus('El usuario no es Investigador', 'error');
                }
                // Limpiar formulario de inicio de sesión
                document.getElementById('login-form').reset();
                // Actualizar la pestaña actual
            } catch (error) {
            }
        });
    }
    
    // Manejar formulario de registro
    document.getElementById('register-form').addEventListener('submit', async function(e) {
        e.preventDefault();
        
        const nombre = document.getElementById('nombre').value;
        const cedula = document.getElementById('cedula').value;
        const password = document.getElementById('password').value;
        const passwordConfirm = document.getElementById('password-confirm').value;
        const tipo = document.getElementById('tipo').value;

        
        
        try {
            while (password !== passwordConfirm) {
                throw new Error('Las contraseñas no coinciden');
            }
            const response = await registerUserData(nombre, cedula, password, tipo);
            showStatus('Usuario registrado exitosamente.', 'success');
            if(response.failed === false) {
                showStatus('El usuario con la cedula ' + cedula + ' ya existe', 'error');
            }
            document.getElementById('register-form').reset();
            
            // Si estamos en la pestaña de usuarios, actualizar la lista
            if (currentOption === 'users') {
                fillTableUsers();
            }
        } catch (error) {
            showStatus('Error al registrar usuario: ' + error.message, 'error');
        }
    });
    
    // Manejar botón de actualizar usuarios
    document.getElementById('refresh-users').addEventListener('click', fillTableUsers);
    
    // Manejar botón de exportación
    document.getElementById('export-button').addEventListener('click', exportToCsv);
    
    document.getElementById('clean-button').addEventListener('click', deleteFormsDB);

    document.getElementById('actualizar-button').addEventListener('click', getForms);

    document.getElementById('cancel-change-password').addEventListener('click', function() {
                    document.getElementById('change-password').classList.remove('active');
                });
    
});