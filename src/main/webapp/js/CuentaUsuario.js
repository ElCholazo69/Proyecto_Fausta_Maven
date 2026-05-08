    const formulario_login = document.querySelector(".login");
    const fondoOscuro = document.querySelector(".fondoOscuro");
    const registroFormulario = document.querySelector(".registroFormulario");

    // Inputs del login
    const loginForm = document.querySelector(".login form");
    const emailLogin = document.getElementById("correoInicioSesion");
    const passwordLogin = document.getElementById("contraseñaInicioSesion");

    // Header
    const navDerecho = document.querySelector(".encabezado-derecho");

    //Funcion para hacer aparecer el login
    function mostrarLogin(){
        formulario_login.style.display = "flex";
        fondoOscuro.style.display = "flex";
    }

    //Funcion para desaparecer el login
    function cerrarLoginRegistro(){
        formulario_login.style.display = "none";
        fondoOscuro.style.display = "none";
        registroFormulario.style.display ="none";
    }
    fondoOscuro.addEventListener("click", cerrarLoginRegistro);

    //Funcion para hacer aparecer el formulario de registro
    const botonRegistrarse = document.querySelector(".botonRegistrarse");

    function mostrarRegistro(){
        formulario_login.style.display = "none";
        registroFormulario.style.display = "block";
    }
    botonRegistrarse.addEventListener("click", mostrarRegistro);

    //Funcion para registrarse en la BASE DE DATOS
    const registro = document.querySelector(".registro");
    function registrarse(e){
        e.preventDefault(); // Se mantiene en la misma página despues de registrarse

        const nombres = document.getElementById("nombre-CrearCuenta").value;
        const apellidos = document.getElementById("apellidos-CrearCuenta").value;
        const email = document.getElementById("correo-CrearCuenta").value;
        const contrasena = document.getElementById("contraseña-CrearCuenta").value;
        const telefono = document.getElementById("telefono-CrearCuenta").value;
        const dni = document.getElementById("dni-CrearCuenta").value;
        const ciudad = document.getElementById("ciudad_CrearCuenta").value;

        if (ciudad === "") {
            alert("Debe seleccionar una ciudad.");
            return;
        }
        const formData = new URLSearchParams();
        formData.append("nombres", nombres);
        formData.append("apellidos", apellidos);
        formData.append("email", email);
        formData.append("contrasena", contrasena);
        formData.append("telefono", telefono);
        formData.append("dni", dni);
        formData.append("id_ciudad",ciudad);
        
        fetch("/Proyecto_Fausta_Maven/registroCliente", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: formData.toString()
        })
        .then(res => res.json())
        .then(data => {
            alert(data.mensaje);
        })
        .catch(err => {
            console.error("Error: "+err);
        });
    }
    registro.addEventListener("submit", registrarse);

    //-----------------------------------//// 
    //=== LOGIN ===
    function iniciarSesion(e) {
        e.preventDefault();

        const identificador = emailLogin.value.trim();
        const contrasena = passwordLogin.value.trim();

        const formData = new URLSearchParams();
        formData.append("email", identificador);
        formData.append("contrasena", contrasena);

        fetch("/Proyecto_Fausta_Maven/login", {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: formData.toString()
        })
        .then(res => res.json())
        .then(data => {
            if (data.error) {
                alert(data.error);
                return;
            }
            if(data.estado ==="OK"){
                // Guardar en localStorage
                localStorage.setItem("usuario", JSON.stringify(data));

                // Actualizar header
                actualizarHeader(data);

                // Cerrar login
                cerrarLoginRegistro();
        } else{
            alert("No se pudo iniciar sesión");
        }
        })
        .catch(err => {
            console.error("Error en login:", err);
        });
    }
    loginForm.addEventListener("submit", iniciarSesion);

    // === ACTUALIZAR HEADER SEGÚN USUARIO ===
    function actualizarHeader(usuario) {
      // eliminar dropdown / menu anterior si existe (evita duplicados)
      const existingMenu = navDerecho.querySelector('.user-dropdown-wrapper');
      if (existingMenu) existingMenu.remove();

      // Localizar Contactanos (inserción después)
      const contactLink = navDerecho.querySelector('a[href*="Contactanos"]') || navDerecho.querySelector('a');

      // Ocultar el anchor de "Iniciar Sesión" si existe
      const loginAnchor = navDerecho.querySelector('a[onclick="mostrarLogin()"]') || navDerecho.querySelector('.boton-sesion')?.closest('a');
      if (loginAnchor) loginAnchor.style.display = 'none';

      // Contenedor wrapper (para poder eliminar con facilidad)
      const wrapper = document.createElement('div');
      wrapper.className = 'user-dropdown-wrapper'; // identificador para futuros removals
      wrapper.style.display = 'inline-block';
      wrapper.style.marginLeft = '10px';

      // Botón dropdown (Bootstrap)
      const btn = document.createElement('button');
      btn.className = 'btn btn-outline-light dropdown-toggle'; // ajustar estilo según header
      btn.type = 'button';
      const uid = 'dropdownUser_' + Date.now();
      btn.id = uid;
      btn.setAttribute('data-bs-toggle', 'dropdown');
      btn.setAttribute('aria-expanded', 'false');
      const nombreCompleto = ((usuario.nombres || "") + " " + (usuario.apellidos || "")).trim();
      const nombreSolo = nombreCompleto ? nombreCompleto.split(/\s+/)[0] : "";
      btn.textContent = `Hola, ${nombreSolo}`;

      // Lista del dropdown
      const ul = document.createElement('ul');
      ul.className = 'dropdown-menu dropdown-menu-end';
      ul.setAttribute('aria-labelledby', uid);
      
      

      // Si es empleado: Muestra panel administrador como opción disponible
      if (usuario.tipo === 'empleado') {
        //Obtener el div de tienda  
        const tiendaBoton = document.querySelector('a[href="Categoria.html"]');
        if (tiendaBoton) {
        tiendaBoton.style.display = "none"; // Oculta el botón
        }   
        const liAdmin = document.createElement('li');
        const aAdmin = document.createElement('a');
        aAdmin.className = 'dropdown-item';
        aAdmin.href = 'PanelAdministrador.html';
        aAdmin.textContent = 'Panel de administración';
        liAdmin.appendChild(aAdmin);
        ul.appendChild(liAdmin);

        const divider = document.createElement('li');
        divider.innerHTML = '<hr class="dropdown-divider">';
        ul.appendChild(divider);
      }
      
      //Si es usuario: Muestra mis pedidos como opción disponible
      if(usuario.tipo === 'cliente'){
          const liUsuario1 = document.createElement('li');
          const aUsuario1 = document.createElement('a');
          aUsuario1.className = 'dropdown-item';
          aUsuario1.href = 'PerfilUsuario.html';
          aUsuario1.textContent = 'Editar perfil';
          liUsuario1.appendChild(aUsuario1);
          ul.appendChild(liUsuario1);
          
          const divider1 = document.createElement('li');
          divider1.innerHTML = '<hr class = "dropdown-divider">';
          ul.appendChild(divider1);
          
          const liUsuario2 = document.createElement('li');
          const aUsuario2 = document.createElement('a');
          aUsuario2.className = 'dropdown-item';
          aUsuario2.href = 'MisPedidos.html';
          aUsuario2.textContent = 'Mis pedidos';
          liUsuario2.appendChild(aUsuario2);
          ul.appendChild(liUsuario2);
          
          const divider2 = document.createElement('li');
          divider2.innerHTML = '<hr class="dropdown-divider">';
          ul.appendChild(divider2);
      }

      // Agrega opción de cerrar sesión
      const liLogout = document.createElement('li');
      const btnLogout = document.createElement('button');
      btnLogout.type = 'button';
      btnLogout.className = 'dropdown-item';
      btnLogout.textContent = 'Cerrar sesión';
      btnLogout.addEventListener('click', cerrarSesion);
      liLogout.appendChild(btnLogout);
      ul.appendChild(liLogout);

      // Montar todo
      wrapper.appendChild(btn);
      wrapper.appendChild(ul);

      // Insertar en el DOM (después de Contactanos si se encuentra)
      if (contactLink && contactLink.parentElement === navDerecho) {
        contactLink.insertAdjacentElement('afterend', wrapper);
      } else {
        navDerecho.appendChild(wrapper);
      }

      // Inicializar Bootstrap Dropdown
      if (window.bootstrap && bootstrap.Dropdown) {
        try {
          new bootstrap.Dropdown(btn);
        } catch (err) {
          console.warn('No se pudo inicializar bootstrap.Dropdown:', err);
        }
      }
    }

    // === CERRAR SESIÓN ===//
    function cerrarSesion() {
        localStorage.removeItem("usuario");
        location.reload(); // Refrescar página para volver al estado inicial
    }
    
    // === Cargar lista de ciudades para el registro ===/
    function cargarCiudades(){
        fetch("/Proyecto_Fausta_Maven/ListarCiudades")
                .then(response =>response.json())
                .then(data =>{
                    
                    const selectCiudad = document.getElementById("ciudad_CrearCuenta");
            
            // Solo continúa si existe el select en esta página
            if (!selectCiudad) return;
            
            selectCiudad.innerHTML = "<option value=''>Seleccione...</option>";
            
            data.forEach(c =>{
                let option =document.createElement("option");
                option.value = c.id_ciudad;
                option.textContent = c.nombre;
                selectCiudad.appendChild(option);
            });
        })
        .catch(error => console.error("Error cargando ciudades:",error));
        
    }
    
    // Ejecutar automáticamente si la página tiene el select
    document.addEventListener("DOMContentLoaded", function () {
        const selectCiudad = document.getElementById("ciudad_CrearCuenta");
        if (selectCiudad) {
            cargarCiudades();
        }
    });

// === AL CARGAR LA PÁGINA: verificar si ya hay sesión ===
document.addEventListener("DOMContentLoaded", () => {
    const usuario = JSON.parse(localStorage.getItem("usuario"));
    if (usuario) {
        actualizarHeader(usuario);
    }
});