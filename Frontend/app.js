// Variables globales
const API_BASE_URL = 'http://localhost:8080';
let currentOption = 'welcome'; // Pestaña actual

// Función para mostrar mensajes de estado
function showStatus(message, type) {
    const statusElement = document.getElementById('status-message');
    statusElement.textContent = message;
    statusElement.className = 'status ' + type;
    
    // Ocultar el mensaje después de 1 segundos
    setTimeout(() => {
        statusElement.className = 'status';
        statusElement.textContent = '';
    }, 1000);
}

async function loginData(cedula, password) {
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
async function registerUserData(name, cedula,password, role) {
    const response = await fetch(`${API_BASE_URL}/register`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            name,
            cedula,
            password,
            role
        })
    });
    
    const data = await response.json();
    
    if (!response.ok || data.error) {
        throw new Error(data.error || 'Error en la solicitud');
    }
    
    return data;
}


async function loadUsers() {
    try {
        // En una implementación real, aquí llamarías a tu endpoint para obtener usuarios
        // Para este ejemplo, usaremos datos simulados
        const response = await fetch(`${API_BASE_URL}/use`);
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
                const tr = document.createElement('tr');
                tr.innerHTML = `
                    <td>${user.cedula}</td>
                    <td>${user.nombre || 'N/A'}</td>
                    <td>${user.estado || 'N/A'}</td>
                    <td>${user.tipo || 'N/A'}</td>
                `;
                tbody.appendChild(tr);
            });
        } else {
            const tr = document.createElement('tr');
            tr.innerHTML = '<td colspan="5" style="text-align: center;">No hay usuarios registrados</td>';
            tbody.appendChild(tr);
        }
        
        showStatus('Usuarios cargados exitosamente', 'success');
    } catch (error) {
        showStatus('Error al cargar usuarios: ' + error.message, 'error');
    }
}



async function exportToCsv() {
    try {
        const response = await fetch(`${API_BASE_URL}/exportCSV`);
        const data = await response.json();
        /*
        if (!response.ok || data.error) {
            throw new Error(data.error || 'Error al exportar datos');
        }
        
        // Crear y descargar el archivo CSV
        const blob = new Blob([data.csv], { type: 'text/csv' });
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = 'DatosRecolectados.csv';
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
        URL.revokeObjectURL(url);
        
        showStatus('Datos exportados exitosamente', 'success');*/
    } catch (error) {
        showStatus('Error al exportar datos: ' + error.message, 'error');
    }
}

// Esperar a que el DOM esté cargado
document.addEventListener('DOMContentLoaded', function() {
    // Manejar cambios de pestaña
    document.querySelectorAll('.option-menu').forEach(optionMenu => {
        optionMenu.addEventListener('click', function() {
            // Mejorar la logica de cerrar sesión
            if (this.dataset.option ==="logout") {
                window.location.href = 'login.html';
            }else {
                document.querySelector(`.option-menu[data-option="${currentOption}"]`).classList.remove('active');
                document.getElementById(currentOption).classList.remove('active');
                    
                // Activar nueva pestaña
                currentOption = this.dataset.option;
                this.classList.add('active');
                document.getElementById(currentOption).classList.add('active');
            }
        });
    });

    // Manejar formulario de inicio de sesión
    document.getElementById('login-form').addEventListener('submit', async function(e) {
        e.preventDefault();
        
        const cedula = document.getElementById('cedula').value;
        const password = document.getElementById('password').value;
        
        try {
            //const response = await loginUser(cedula, password);
            if (cedula === '1' && password === '1') {
                showStatus('Sesión iniciada exitosamente.', 'success');
                window.location.href = 'index.html';
                
            } else {
                showStatus('Datos incorrectos.', 'error');
            }
             // Redirigir a la página principal
            
            // Limpiar formulario de inicio de sesión
            document.getElementById('login-form').reset();
            // Actualizar la pestaña actual
            currentOption = 'welcome';
            document.querySelector(`.option-menu[data-option="${currentOption}"]`).classList.add('active');
            document.getElementById(currentOption).classList.add('active');
            
        } catch (error) {
            showStatus('Error al iniciar sesión: ' + error.message, 'error');
        }
    });
    
    // Manejar formulario de registro
    document.getElementById('register-form').addEventListener('submit', async function(e) {
        e.preventDefault();
        
        const name = document.getElementById('name').value;
        const cedula = document.getElementById('cedula').value;
        const password = document.getElementById('password').value;
        const passwordConfirm = document.getElementById('password-confirm').value;
        const role = document.getElementById('role').value;
        
        
        try {
            while (password !== passwordConfirm) {
                throw new Error('Las contraseñas no coinciden');
            }
            const response = await registerUser(name, cedula, password, passwordConfirm, role);
            showStatus('Usuario registrado exitosamente.', 'success');
            document.getElementById('register-form').reset();
            
            // Si estamos en la pestaña de usuarios, actualizar la lista
            if (currentOption === 'users') {
                loadUsers();
            }
        } catch (error) {
            showStatus('Error al registrar usuario: ' + error.message, 'error');
        }
    });
    
    // Manejar botón de actualizar usuarios
    document.getElementById('refresh-users').addEventListener('click', loadUsers);
    

    
    // Manejar botón de exportación
    document.getElementById('export-button').addEventListener('click', exportToCsv);
    
    // Cargar usuarios inicialmente si es necesario
    if (currentOption === 'users') {
        loadUsers();
    }
});