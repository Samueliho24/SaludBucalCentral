Revision de La version 1.1 del sistema Evaluacion Salud Bucal
Fecha: 30/04/2026
Realizada por:
	Samuel Chourio
	chourio.samuel.24@gmail.com


Hallazgos:
	Aplicacion Movil:
		- Hay errores ortograficos en los siguiente nombres:
			- Mordida(morida profunda anterior).
			- Mordida(morida tope a tope)
		- Elementos no tiene tamanos uniformes.
		- Los campos de introduccion de datos son pequenos.

	Programa de Escritorio:
		- Tiene problemas con el ajuste de las imagenes.
		- Se puede volver a la pantalla principal si ya ha sido iniciada una vez solo volviendo a la pagina anterior.
		- El footer esta pegado a las secciones y no en la parte baja de la pagina.
		- Se muestran las direccion de los archivos en barra de URL.
		- La lista de usuarios registrados no tiene paginacion.


Recomendaciones para la version 1.2:
	Aplicacion Movil: 
		- Corregir nombres y opciones de los campos
		- Mejorar el estilo de los elementos.
		- Definir explicitamente todos los tamanos.
		- Integrar lectura de codigo QR para la sincronizacion de datos y para la exportacion de formularios.
		- Agregar una opcion de recuperacion de datos manual.
		- Bloquear la seleccion de las palabras.
		- Pedir una confirmacion antes de eliminarlos datos.

	Programa de Escritorio:
		- Mejorar el diseno de las paginas.
		- Utilizar Renderizado por Framework para no visualizar las url.
		- Cambiar el proceso de obtencion de la IP para que funciones tanto en Windows como en Linux.
		- Agregar paginacion a la lista de usuario.
		- Cambiar el proceso de transferencia de datos para recibir los datos y enviar una confirmacion de recibido al dispositivo, antes de ingresar los datos a la DB.
		- Cambiar los procesos de ingreso de datos a la DB a Transacciones.


