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
    
    if (!response.ok || data.error) {
        throw new Error(data.error || 'Error en la solicitud');
    }
    
    return data;
}
// Funciones para interactuar con la API
async function registerUserData(nombre, cedula,password, tipo, investigador) {
    const response = await fetch(`${API_BASE_URL}/register`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            nombre,
            cedula,
            password,
            tipo,
            investigador
        })
    });
    
    const data = await response.json();
    
    
    if (!response.ok || data.error) {
        throw new Error(data.error || 'El usuario con la cedula ' + cedula + ' ya existe');
    }
    
    return data;
}

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

async function User(cedula) {
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
        return data;
    } catch (error) {
        showStatus('Error al cargar usuarios: ' + error.message, 'error');
    }
}

async function fillInvestigadores() {
    const data = await User();
    const datos = data.data;
    const select = document.getElementById('investigador');
    const optionsHTML = datos.filter(item => item.tipo === "Investigador").map(item => `<option value="${item.id}">${item.nombre}</option>`).join('');
    select.innerHTML = optionsHTML;
}
async function fillTableUsers() {
    let accionEstado='';
    const data = await User();
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
                <td><button class="table-button desactivate-button" data-cedula="${user.cedula}" onclick="deleteUser(${user.cedula}, '${accionEstado}')">${accionEstado}</button>
                <button class="table-button delete-button" data-cedula="${user.cedula}" onclick="deleteUser(${user.cedula}, 'Eliminar')">Borrar</button></td>
            `;
            tbody.appendChild(tr);
        });
    } else {
        const tr = document.createElement('tr');
        tr.innerHTML = '<td colspan="5" style="text-align: center;">No hay usuarios registrados</td>';
        tbody.appendChild(tr);
    }
    
    //showStatus('Usuarios cargados exitosamente', 'success');
    
}

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

//Enlace con el HTML

// Esperar a que el DOM esté cargado
document.addEventListener('DOMContentLoaded', function() {
    // Manejar cambios de pestaña
    document.querySelectorAll('.option-menu').forEach(optionMenu => {
        optionMenu.addEventListener('click', function() {
            // Mejorar la logica de cerrar sesión
            if (this.dataset.option ==="logout") {
                window.location.href = 'login.html';
                sessionStorage.removeItem('userAuth');
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
                if (currentOption === 'register') {
                    fillInvestigadores();
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
                console.log(response);
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
                showStatus('Error al iniciar sesión: ' + error.message, 'error');
            }
        });
    }
    document.getElementById('tipo').addEventListener('change', function() {
        if (this.value === "Investigador") {
            document.getElementById('investigador-label').style.display = 'none';
            document.getElementById('investigador').style.display = 'none';
            document.getElementById('investigador').value = null;
        } else {
            document.getElementById('investigador-label').style.display = 'block';
            document.getElementById('investigador').style.display = 'block';
        }
    });
    // Manejar formulario de registro
    document.getElementById('register-form').addEventListener('submit', async function(e) {
        e.preventDefault();
        
        const nombre = document.getElementById('nombre').value;
        const cedula = document.getElementById('cedula').value;
        const password = document.getElementById('password').value;
        const passwordConfirm = document.getElementById('password-confirm').value;
        const tipo = document.getElementById('tipo').value;
        const investigador = document.getElementById('investigador').value;

        
        
        try {
            while (password !== passwordConfirm) {
                throw new Error('Las contraseñas no coinciden');
            }
            const response = await registerUserData(nombre, cedula, password, tipo, investigador);
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
    
});