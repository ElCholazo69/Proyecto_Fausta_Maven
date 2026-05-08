// Resumen de la compra
const carrito =  document.querySelector(".carrito");
const direccion = document.querySelector(".direccion");
const pago = document.querySelector(".pago");
const boleta = document.getElementById("boleta");
const compra = [carrito, direccion, pago, boleta];

// Paginacion
const anterior = document.getElementById("anterior");
const pag1 = document.getElementById("pag1");
const pag2 = document.getElementById("pag2");
const pag3 = document.getElementById("pag3");
const pag4 = document.getElementById("pag4");
const siguiente = document.getElementById("siguiente");
const paginas = [pag1, pag2, pag3, pag4];
const barra = document.querySelector(".barra");

// Capturación de radios
const envioCasa = document.getElementById("envioCasa");
const envioTienda = document.getElementById("envioTienda");

//Campos
const campoDireccion = document.getElementById("campoDireccion");
const campoUbicacionTienda = document.getElementById("campoUbicacionTienda");
const campoHorarioAtencion = document.getElementById("campoHorarioAtencion");
const campoCorreo = document.getElementById("campoCorreo");

//Referencias a los inputs de los formularios
const inputNombre = document.getElementById("nombre");
const inputTelefono = document.getElementById("telefono");

//Función de mostrar u ocultar campos segun metodo de envio
function actualizarCamposEnvio(){
    if(envioCasa.checked){
        campoDireccion.style.display="block";
        campoUbicacionTienda.style.display="none";
        campoHorarioAtencion.style.display="none";
        campoCorreo.style.display="none";
    }else{
        campoDireccion.style.display = "none";
        campoUbicacionTienda.style.display="block";
        campoHorarioAtencion.style.display="block";
        campoCorreo.style.display="block";

        //Rellenar los datos de la tienda directamente
        document.getElementById("ubicacionTienda").value="Jr. Pedro Conde 472 Lince";
        document.getElementById("horarioAtencion").value="Lunes - viernes: 10AM-6PM, Sabado: 10AM-7PM, Domingo: 11AM-6PM";
        document.getElementById("correo").value="pedidos@faustapasteleria.com";
    }
}

//Función cargar los datos de usuario
function cargarDatosUsuario(){
  const usuario = JSON.parse(localStorage.getItem("usuario"));


    if (usuario) {
        inputNombre.value = usuario.nombres +" "+ usuario.apellidos || "";
        inputTelefono.value = usuario.telefono || "";
    } else {
        inputNombre.value = "";
        inputTelefono.value = "";
    }  
}

envioCasa.addEventListener("change",actualizarCamposEnvio);
envioTienda.addEventListener("change",actualizarCamposEnvio);

document.addEventListener('DOMContentLoaded', () => {
    cargarDatosUsuario();
    actualizarCamposEnvio(); 
    mostrarCarrito();
    activarEventosInputsCantidad();
    calcularSubtotal();

    // Validaciones del formulario de pago
    const numeroTarjeta = document.getElementById("numeroTarjeta");
    numeroTarjeta.addEventListener("input", (e) => {
        let value = e.target.value.replace(/\D/g, "");
        value = value.replace(/(\d{4})(?=\d)/g, "$1 ");
        e.target.value = value.substring(0, 19);
    });

    const cvv = document.getElementById("cvv");
    cvv.addEventListener("input", (e) => {
        e.target.value = e.target.value.replace(/\D/g, "").substring(0, 3);
    });

    const fechaVencimiento = document.getElementById("fechaVencimiento");
    const hoy = new Date();
    const mesActual = hoy.getFullYear() + "-" + String(hoy.getMonth() + 1).padStart(2, "0");
    fechaVencimiento.min = mesActual;
});

function mostrarCarrito() {
    const contenedorCarrito = document.querySelector(".productosCarrito");
    contenedorCarrito.innerHTML = ""; // Limpiar contenido anterior

    let carrito = JSON.parse(localStorage.getItem("carrito")) || [];

    carrito.forEach(producto => {
        const div = document.createElement("div");
        div.classList.add("carrito-producto");

        div.innerHTML = `
            <img class="imagen-carrito" src="${producto.imagen}">
            <h5 class="nombre-carrito">${producto.nombre}</h5>
            <p class="tamano-carrito">Tamaño: ${producto.tamano}</p>

            <div class="input-group mb-3 d-flex align-items-center carritoCantidad">
                <button class="btn btn-outline-secondary restar">-</button>
                <input type="text" class="form-control text-center numeroCantidad" value="${producto.cantidad}">
                <button class="btn btn-outline-secondary sumar">+</button>
            </div>

            <p class="precio-carrito">S/${(producto.precio * producto.cantidad).toFixed(2)}</p>
            <button type="button" class="btn-close cerrar"></button>
        `;
        
        contenedorCarrito.appendChild(div);
    });

    // Agrega la funcion de eliminar en cada boton de un producto
    const eliminarCarrito = document.querySelectorAll(".cerrar");
    eliminarCarrito.forEach(boton => {
        boton.addEventListener("click", () => eliminarProducto(boton));
    });

    // Agrega las funciones para actualizar el precio
    const restar = document.querySelectorAll(".restar");
    const sumar = document.querySelectorAll(".sumar");

    restar.forEach(boton => {
        boton.addEventListener("click", () => restarCantidadProducto(boton));
    });

    sumar.forEach(boton => {
        boton.addEventListener("click", () => sumarCantidadProducto(boton));
    });
}

function eliminarProducto(boton) {
    const div = boton.closest(".carrito-producto");

    const nombre = div.querySelector(".nombre-carrito").textContent;
    const tamano = div.querySelector(".tamano-carrito")
                      .textContent.replace("Tamaño: ", "").trim();

    let carrito = JSON.parse(localStorage.getItem("carrito")) || [];

    carrito = carrito.filter(p => !(p.nombre === nombre && p.tamano === tamano));

    localStorage.setItem("carrito", JSON.stringify(carrito));

    mostrarCarrito();
    activarEventosInputsCantidad();
    calcularSubtotal();
}

function actualizarCantidadManual(input){
    const div = input.closest(".carrito-producto");
    const precio_carrito = div.querySelector(".precio-carrito");

    if (input.value.trim() === "") {
        return;
    }

    let cantidad = parseInt(input.value);

    if (isNaN(cantidad) || cantidad < 1) {
        cantidad = 1;
        input.value = 1;
    }
    actualizarProductoEnCarrito(div, cantidad, precio_carrito);
}

function activarEventosInputsCantidad() {
    const inputs = document.querySelectorAll(".numeroCantidad");

    inputs.forEach(input => {

        // Al escribir manualmente
        input.addEventListener("input", () => {
            actualizarCantidadManual(input);
        });

        // Restringir solo a números y teclas especiales permitidas
        input.addEventListener("keydown", (e) => {

            const teclasPermitidas = [
                "Backspace","Delete","ArrowLeft","ArrowRight","Tab"
            ];

            // Permitir teclas especiales siempre
            if (teclasPermitidas.includes(e.key)) return;

            // Permitir números
            if (/^[0-9]$/.test(e.key)) return;

            // Si no es número ni tecla permitida, bloquear
            e.preventDefault();
        });

        // Si presiona Enter
        input.addEventListener("keypress", (e) => {
            if (e.key === "Enter") {
                e.preventDefault();
                actualizarCantidadManual(input);
                input.blur();
            }
        });

    });
}

function actualizarProductoEnCarrito(div, cantidad, precio_carrito) {

    const nombre = div.querySelector(".nombre-carrito").textContent;
    const tamano = div.querySelector(".tamano-carrito")
                      .textContent.replace("Tamaño: ", "").trim();

    let carrito = JSON.parse(localStorage.getItem("carrito")) || [];

    const producto = carrito.find(p => p.nombre === nombre && p.tamano === tamano);
    if (!producto) return;

    producto.cantidad = cantidad;

    precio_carrito.textContent = `S/${(producto.precio * cantidad).toFixed(2)}`;

    localStorage.setItem("carrito", JSON.stringify(carrito));

    calcularSubtotal();
}

function restarCantidadProducto(boton) {
    const div = boton.closest(".carrito-producto");
    const input = div.querySelector(".numeroCantidad");
    let cantidad = parseInt(input.value);

    if (cantidad > 1) {
        cantidad--;
        input.value = cantidad;
        const precio_carrito = div.querySelector(".precio-carrito");
        actualizarProductoEnCarrito(div, cantidad, precio_carrito);
    }
}

function sumarCantidadProducto(boton) {
    const div = boton.closest(".carrito-producto");
    const input = div.querySelector(".numeroCantidad");

    let cantidad = parseInt(input.value);
    cantidad++;
    input.value = cantidad;

    const precio_carrito = div.querySelector(".precio-carrito");
    actualizarProductoEnCarrito(div, cantidad, precio_carrito);
}

function calcularSubtotal() {
    let carrito = JSON.parse(localStorage.getItem("carrito")) || [];
    let subtotal = 0;

    carrito.forEach(p => {
        subtotal += p.precio * p.cantidad;
    });

    localStorage.setItem("subtotal", subtotal);

    document.querySelector(".subtotal-carrito").textContent =
        "S/" + subtotal.toFixed(2);
}

function pagina1() {
    carrito.style.display = "flex";
    direccion.style.display = "none";
    pago.style.display = "none";
    boleta.style.display = "none";

    anterior.classList.add("disabled");
    pag1.classList.add("active");
    pag2.classList.remove("active");
    pag3.classList.remove("active");
    pag4.classList.remove("active");
    siguiente.classList.remove("disabled");

    barra.style.width = "0%";
    barra.textContent = "0%";

}
pag1.addEventListener("click", pagina1);

function pagina2() {
    carrito.style.display = "none";
    direccion.style.display = "flex";
    pago.style.display = "none";
    boleta.style.display = "none";

    anterior.classList.remove("disabled");
    pag1.classList.remove("active");
    pag2.classList.add("active");
    pag3.classList.remove("active");
    pag4.classList.remove("active");
    siguiente.classList.remove("disabled");

    barra.style.width = "25%";
    barra.textContent = "25%";

}
pag2.addEventListener("click", pagina2);

function pagina3() {
    carrito.style.display = "none";
    direccion.style.display = "none";
    pago.style.display = "flex";
    boleta.style.display = "none";

    anterior.classList.remove("disabled");
    pag1.classList.remove("active");
    pag2.classList.remove("active");
    pag3.classList.add("active");
    pag4.classList.remove("active");
    siguiente.classList.remove("disabled");

    barra.style.width = "50%";
    barra.textContent = "50%";

}
pag3.addEventListener("click", pagina3);

function pagina4() {
    carrito.style.display = "none";
    direccion.style.display = "none";
    pago.style.display = "none";
    boleta.style.display = "flex";

    anterior.classList.remove("disabled");
    pag1.classList.remove("active");
    pag2.classList.remove("active");
    pag3.classList.remove("active");
    pag4.classList.add("active");
    siguiente.classList.add("disabled");

    barra.style.width = "100%";
    barra.textContent = "100%";

    //Se elimina el carritoCopia cuando se recargue la página, navega hacia adelante o atras
    window.addEventListener("beforeunload", function () {
    localStorage.removeItem("carritocopia");
    localStorage.removeItem("idPedido");
});
}
pag4.addEventListener("click", () => {
    pagina4();
    actualizarDatos();
});

function retroceder(paginas, indice) {
    if (indice > 0) {
        paginas.forEach(pagina => pagina.classList.remove("active"));
        paginas[indice-1].classList.add("active");
    }

    if (indice-1 == 0) {
        anterior.classList.add("disabled");
    }

    siguiente.classList.remove("disabled");

    if (indice-1 >= 0 && indice-1 <= 3) {
        compra.forEach(compra => compra.style.display = "none");
        if(indice-1 == 0) {compra[0].style.display = "flex"; barra.style.width = "0%"; barra.textContent = "0%";}
        if(indice-1 == 1) {compra[1].style.display = "flex"; barra.style.width = "25%"; barra.textContent = "25%";}
        if(indice-1 == 2) {compra[2].style.display = "flex"; barra.style.width = "50%"; barra.textContent = "50%";}
        if(indice-1 == 3) {
            compra[3].style.display = "flex"; barra.style.width = "100%"; barra.textContent = "100%";
            actualizarDatos();
        }
    }
}
anterior.addEventListener("click", () =>{
    let paginaActiva = document.querySelector(".active");
    let indice = paginas.indexOf(paginaActiva);
    retroceder(paginas, indice);
});

function avanzar(paginas, indice) {
    if (indice < paginas.length-1) {
        paginas.forEach(pagina => pagina.classList.remove("active"));
        paginas[indice+1].classList.add("active");
    }

    if (indice+1 == paginas.length-1) {
        siguiente.classList.add("disabled");
    }
    
    anterior.classList.remove("disabled");

    if (indice+1 >=0 && indice+1 <=3) {
        compra.forEach(compra => compra.style.display = "none");
        if(indice+1 == 0) {compra[0].style.display = "flex"; barra.style.width = "0%"; barra.textContent = "0%";}
        if(indice+1 == 1) {compra[1].style.display = "flex"; barra.style.width = "25%"; barra.textContent = "25%";}
        if(indice+1 == 2) {compra[2].style.display = "flex"; barra.style.width = "50%"; barra.textContent = "50%";}
        if(indice+1 == 3) {
            compra[3].style.display = "flex"; barra.style.width = "100%"; barra.textContent = "100%";
            actualizarDatos();
        }
    }
}
siguiente.addEventListener("click", () =>{
    let paginaActiva = document.querySelector(".active");
    let indice = paginas.indexOf(paginaActiva);
    avanzar(paginas, indice);
});

function actualizarDatos() {
    // Cliente
    const cliente = document.querySelector(".cliente");
    cliente.innerHTML = "";
    let usuario = JSON.parse(localStorage.getItem("usuario")) || "Juan Pérez";
    cliente.innerHTML = "<strong>Cliente:</strong> "+ usuario.nombres+ " "+ usuario.apellidos;
    
    //Mostrar ID del pedido en la boleta
    const idBoleta = document.getElementById("idBoleta");
    const idPedido = localStorage.getItem("idPedido") || "---";
    idBoleta.textContent = "N° " + idPedido;

    // Fecha
    const fecha_actual = document.getElementById("fecha-actual");
    const fechaLocal = new Date().toLocaleDateString();
    fecha_actual.innerHTML = "<strong>Fecha de emisión:</strong> " + fechaLocal;

    // Productos
    const ProductosBoleta = document.getElementById("ProductosBoleta");
    ProductosBoleta.innerHTML = "";
    let carrito = JSON.parse(localStorage.getItem("carritocopia")) || JSON.parse(localStorage.getItem("carrito")) || [];
    carrito.forEach(producto => {
        const tr = document.createElement("tr");

        tr.innerHTML = `
            <td>${producto.nombre}</td>
            <td class="text-center">${producto.tamano}</td>
            <td class="text-center">${producto.cantidad}</td>
            <td class="text-end">S/${producto.precio}</td>
            <td class="text-end">S/${(producto.precio*producto.cantidad)}</td>
        `;
        
        ProductosBoleta.appendChild(tr);
    });

    // Subtotal
    let subtotalCalc = carrito.reduce((acc,p)=> acc + p.precio * p.cantidad, 0);
    document.querySelector(".subtotal3").innerHTML ="<strong>Subtotal:</strong> S/" + subtotalCalc.toFixed(2);

    // Descuento
    let descuentoAplicado = parseFloat(localStorage.getItem("descuento")) || 0;
    if(subtotalCalc === 0){
        descuentoAplicado = 0;
    }
    document.querySelector(".descuento").innerHTML = "<strong>Descuento del subtotal:</strong> S/" + descuentoAplicado.toFixed(2);

    // Subtotal con descuento
    let subtotalConDescuento = Math.max(subtotalCalc - descuentoAplicado, 0);

    // IGV
    let igvCalc = subtotalConDescuento > 0 ? subtotalConDescuento * 0.18 : 0;
    document.querySelector(".igv").innerHTML ="<strong>IGV (18%):</strong> S/" + igvCalc.toFixed(2);

    // Total
    let totalCalc = subtotalConDescuento + igvCalc;
    document.querySelector(".total").textContent ="TOTAL: S/" + totalCalc.toFixed(2);
    }

// Envio de la compra a la bd
const formPago = document.getElementById("form-pago");
formPago.addEventListener("submit", (e) => {
    e.preventDefault();
    
    // Verificar si el usuario inició sesión
    const usuario = JSON.parse(localStorage.getItem("usuario"));

    if (!usuario || !usuario.id) {
        alert("Debes iniciar sesión para confirmar tu compra.");

    // Redirigir a la página principal
    window.location.href = "PaginaPrincipal.html";

    return;
    }
    // Validaciones
    const numeroTarjeta = document.getElementById("numeroTarjeta").value.trim();
    if (!numeroTarjeta) {
        alert("Por favor, completa todos los campos de pago.");
        return;
    }

    const carrito = JSON.parse(localStorage.getItem("carrito")) || [];
    if (carrito.length === 0) {
        alert("El carrito está vacío. Agrega productos antes de confirmar.");
        return;
    }

    // Verificar que los productos tengan id y precioIndex
    for (let prod of carrito) {
        if (!prod.id || prod.precioIndex === undefined) {
            alert("Error: Algunos productos no tienen datos completos. Vuelve a agregarlos.");
            return;
        }
    }

    const metodoEnvio = envioCasa.checked ? "casa" : "tienda";
    const direccion = metodoEnvio === "casa" ? document.getElementById("direccion").value.trim() : "";

    // Datos que se enviaran a la BD
    const idEmpleado = 1;
    const estado = "Pendiente";

    const formData = new URLSearchParams();
    formData.append("idcliente", usuario.id);
    formData.append("idEmpleado", idEmpleado);
    formData.append("estado", estado);
    formData.append("metodoEnvio", metodoEnvio);
    formData.append("direccion", direccion);
    const codigoDescuento = document.getElementById("codigoDescuento").value.trim();
    //Validación si tiene descuento o no
    if(codigoDescuento) {
    formData.append("codigoDescuento", codigoDescuento);
}

    // Agregar productos
    carrito.forEach((prod, index) => {
        formData.append(`producto${index}Id`, prod.id);  // ID del producto (de BD)
        formData.append(`producto${index}PrecioIndex`, prod.precioIndex);  // 0,1,2 para precio1, precio2, precio3
        formData.append(`producto${index}Cantidad`, prod.cantidad);
        formData.append(`producto${index}Tamano`, prod.tamano);
    });
    formData.append("totalProductos", carrito.length);

    //Enviar
    fetch("/Proyecto_Fausta_Maven/hacerPedido", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: formData.toString()
        })
        .then(res => res.json())
        .then(data => {
            if (data.mensaje && data.mensaje.includes("éxito")) {
                const carritoActual = JSON.parse(localStorage.getItem("carrito")|| []);
                localStorage.setItem("carritocopia",JSON.stringify(carritoActual));

                if(data.idPedido){
                    localStorage.setItem("idPedido",data.idPedido);
                }

                //Guardar descuento recibido desde el servlet
                localStorage.setItem("descuento", data.descuento || 0);

                alert("¡Pedido realizado exitosamente!");

                localStorage.removeItem("carrito");
                localStorage.removeItem("subtotal");      

                let paginaActiva = document.querySelector(".active");
                let indice = paginas.indexOf(paginaActiva);
                avanzar(paginas, indice);
            } else {
                alert("Error al procesar el pedido: " + (data.mensaje || "Inténtalo de nuevo."));
            }
        })
        .catch(err => {
            console.error("Error: "+err);
    });
});
