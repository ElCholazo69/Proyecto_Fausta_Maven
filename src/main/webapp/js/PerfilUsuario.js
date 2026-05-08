document.addEventListener("DOMContentLoaded", () => {

    const usuario = JSON.parse(localStorage.getItem("usuario"));

    if (!usuario) {
        alert("Debe iniciar sesión.");
        window.location.href = "PaginaPrincipal.html";
        return;
    }

    // Llenar los campos
    document.getElementById("id_cliente").value = usuario.id;
    document.getElementById("nombres").value = usuario.nombres || "";
    document.getElementById("apellidos").value = usuario.apellidos || "";
    document.getElementById("email").value = usuario.email || "";
    document.getElementById("telefono").value = usuario.telefono || "";
    document.getElementById("dni").value = usuario.dni || "";

    cargarCiudades(usuario.id_ciudad);

    bloquearCampos(true);

    //VALIDACIONES
    function mostrarError(input, mensaje) {
        let errorElem = input.nextElementSibling;
        if (!errorElem || !errorElem.classList.contains('error-msg')) {
            errorElem = document.createElement('small');
            errorElem.className = 'error-msg text-danger';
            input.insertAdjacentElement('afterend', errorElem);
        }
        errorElem.textContent = mensaje;
    }

    function limpiarError(input) {
        let errorElem = input.nextElementSibling;
        if (errorElem && errorElem.classList.contains('error-msg')) {
            errorElem.textContent = '';
        }
    }

    // Solo letras para nombres y apellidos
    const soloLetras = document.querySelectorAll("#nombres, #apellidos");
    soloLetras.forEach(input => {
        input.addEventListener("input", () => {
            if (/[^A-Za-zÁÉÍÓÚáéíóúÑñ\s]/.test(input.value)) {
                mostrarError(input, "Solo se permiten letras y espacios.");
                input.value = input.value.replace(/[^A-Za-zÁÉÍÓÚáéíóúÑñ\s]/g, '');
            } else {
                limpiarError(input);
            }
        });
    });

    // Solo números para teléfono y DNI
    const soloNumeros = document.querySelectorAll("#telefono, #dni");
    soloNumeros.forEach(input => {
        input.addEventListener("input", () => {
            if (/\D/.test(input.value)) {
                mostrarError(input, "Solo se permiten números.");
                input.value = input.value.replace(/\D/g, '');
            } else {
                limpiarError(input);
            }
        });
    });

    // Validación básica de email
    const emailInput = document.getElementById("email");
    emailInput.addEventListener("input", () => {
        const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!re.test(emailInput.value)) {
            mostrarError(emailInput, "Email no válido.");
        } else {
            limpiarError(emailInput);
        }
    });

});

//BLOQUEO DE CAMPOS
function bloquearCampos(bloquear) {
    const inputs = document.querySelectorAll("#formPerfil input");
    inputs.forEach(inp => inp.disabled = bloquear);

    // Dropdown ciudad
    document.getElementById("dropdownCiudad").disabled = bloquear;

    document.getElementById("btnEditar").style.display = bloquear ? "inline-block" : "none";
    document.getElementById("btnGuardar").style.display = bloquear ? "none" : "inline-block";
}

//CARGAR CIUDADES DINÁMICAS EN DROPDOWN
function cargarCiudades(idSeleccionada) {
    fetch("/Proyecto_Fausta_Maven/ListarCiudades")
        .then(res => res.json())
        .then(data => {
            const menu = document.getElementById("menuCiudades");
            const btn = document.getElementById("dropdownCiudad");

            menu.innerHTML = ""; // Limpiar items
            btn.textContent = "Seleccione..."; // Valor por defecto
            btn.dataset.value = ""; 

            data.forEach(c => {
                const li = document.createElement("li");
                const a = document.createElement("a");
                a.classList.add("dropdown-item");
                a.href = "#";
                a.textContent = c.nombre;
                a.dataset.value = c.id_ciudad;

                // Si es la ciudad seleccionada, mostrar en el botón
                if (c.id_ciudad == idSeleccionada) {
                    btn.textContent = c.nombre;
                    btn.dataset.value = c.id_ciudad;
                }

                // Click en ciudad
                a.addEventListener("click", function(e) {
                    e.preventDefault();
                    btn.textContent = this.textContent;
                    btn.dataset.value = this.dataset.value;
                });

                li.appendChild(a);
                menu.appendChild(li);
            });
        })
        .catch(err => console.error("Error cargando ciudades:", err));
}

// BOTÓN EDITAR 
document.getElementById("btnEditar").addEventListener("click", () => {
    bloquearCampos(false);
});

// GUARDAR CAMBIOS
document.getElementById("btnGuardar").addEventListener("click", () => {

    const id_cliente = document.getElementById("id_cliente").value;
    const nombres = document.getElementById("nombres").value.trim();
    const apellidos = document.getElementById("apellidos").value.trim();
    const email = document.getElementById("email").value.trim();
    const telefono = document.getElementById("telefono").value.trim();
    const dni = document.getElementById("dni").value.trim();
    const id_ciudad = document.getElementById("dropdownCiudad").dataset.value;

    if (!nombres || !apellidos || !email || !telefono || !dni || !id_ciudad) {
        alert("Por favor, complete todos los campos antes de guardar.");
        return;
    }

    const formData = new URLSearchParams();
    formData.append("id_cliente", id_cliente);
    formData.append("nombres", nombres);
    formData.append("apellidos", apellidos);
    formData.append("email", email);
    formData.append("telefono", telefono);
    formData.append("dni", dni);
    formData.append("id_ciudad", id_ciudad);

    fetch("/Proyecto_Fausta_Maven/PerfilUsuario", {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: formData.toString()
    })
    .then(res => res.json())
    .then(data => {
        alert(data.mensaje);

        if (data.estado === "OK") {
            // Actualizar localStorage
            const usuario = JSON.parse(localStorage.getItem("usuario"));

            usuario.nombres = nombres;
            usuario.apellidos = apellidos;
            usuario.email = email;
            usuario.telefono = telefono;
            usuario.dni = dni;
            usuario.id_ciudad = id_ciudad;

            localStorage.setItem("usuario", JSON.stringify(usuario));

            bloquearCampos(true);
        }
    })
    .catch(err => console.error("Error guardando perfil:", err));
});