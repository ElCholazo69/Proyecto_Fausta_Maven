// - Elementos -
const carritoBoton = document.querySelector(".carritoBoton");
const carrito = document.querySelector(".carrito");
const fondoCarrito = document.querySelector(".fondoCarrito");
const seleccionPrecio = document.getElementById("seleccionPrecio");
const agregarCarrito = document.querySelectorAll(".agregarCarrito");

let productoActual = null;
let cantidad_carrito;

// Abrir carrito
function abrirCarrito() {
    carrito.style.display = "flex";
    fondoCarrito.style.display = "flex";
    mostrarCarrito();
}
carritoBoton.addEventListener("click", abrirCarrito);

// Cerrar carrito
function cerrarCarrito() {
    carrito.style.display = "none";
    fondoCarrito.style.display = "none";
    if (seleccionPrecio) seleccionPrecio.style.display = "none";
}
fondoCarrito.addEventListener("click", cerrarCarrito);

// Seleccionar precio
function seleccionarPrecio(boton) {
    const productoDiv = boton.closest(".producto");
    const nombre = productoDiv.querySelector("h5").textContent;
    const imagen = productoDiv.querySelector("img").src;

    // Objeto temporal con el producto seleccionado
    productoActual = { nombre, imagen };

    // Mostrar selector de precios si existe
    if (seleccionPrecio) {
        seleccionPrecio.style.display = "flex";
        fondoCarrito.style.display = "flex";
    }
}

// Procesar selección de precio
function agregarAlCarrito(nombre, imagen, precio) {
    let carritoLS = JSON.parse(localStorage.getItem("carrito")) || [];

    // Convertimos precio a número
    const precioNum = parseFloat(precio);

    const producto = {
        nombre,
        imagen,
        precio: precioNum, // siempre número
        cantidad: 1
    };

    // Revisar si ya existe
    const existe = carritoLS.find(p => p.nombre === nombre);

    if (!existe) {
        carritoLS.push(producto);
        localStorage.setItem("carrito", JSON.stringify(carritoLS));
        alert(`Producto agregado: ${nombre} - S/${precioNum.toFixed(2)}`);
    } else {
        alert("Este producto ya está en el carrito.");
    }

    actualizarCantidadCarrito();
    mostrarCarrito();
}

// Mostrar carrito 
function mostrarCarrito() {
    const contenedorCarrito = document.querySelector(".productosCarrito");
    if (!contenedorCarrito) return;
    contenedorCarrito.innerHTML = "";

    let carritoLS = JSON.parse(localStorage.getItem("carrito")) || [];

    carritoLS.forEach(producto => {
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

    // Eventos de botones
    document.querySelectorAll(".numeroCantidad").forEach(input => {input.addEventListener("input", () => actualizarCantidadManual(input));});
    document.querySelectorAll(".cerrar").forEach(btn => btn.addEventListener("click", () => eliminarProducto(btn)));
    document.querySelectorAll(".restar").forEach(btn => btn.addEventListener("click", () => cambiarCantidad(btn, -1)));
    document.querySelectorAll(".sumar").forEach(btn => btn.addEventListener("click", () => cambiarCantidad(btn, 1)));

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
    const tamanoTexto = div.querySelector(".tamano-carrito").textContent;
    const tamano = tamanoTexto.replace("Tamaño: ", "").trim();

    let carritoLS = JSON.parse(localStorage.getItem("carrito")) || [];

    // Buscar el producto exacto (nombre + tamaño)
    const producto = carritoLS.find(p => p.nombre === nombre && p.tamano === tamano);
    if (!producto) return;

    // Actualizar cantidad
    producto.cantidad = cantidad;

    // Actualizar precio visible
    precio_carrito.textContent = `S/${(producto.precio * cantidad).toFixed(2)}`;

    // Guardar cambios en localStorage
    localStorage.setItem("carrito", JSON.stringify(carritoLS));

    // Recalcular subtotal
    calcularSubtotal();
    actualizarCantidadCarrito();
}

//Cambiar cantidad
function cambiarCantidad(boton, delta) {
    const div = boton.closest(".carrito-producto");
    const nombre = div.querySelector(".nombre-carrito").textContent;
    const input = div.querySelector(".numeroCantidad");
    const precioElem = div.querySelector(".precio-carrito");

    let carritoLS = JSON.parse(localStorage.getItem("carrito")) || [];
    const producto = carritoLS.find(p => p.nombre === nombre);
    if (!producto) return;

    producto.cantidad = Math.max(1, producto.cantidad + delta);
    input.value = producto.cantidad;
    precioElem.textContent = `S/${(producto.precio * producto.cantidad).toFixed(2)}`;

    localStorage.setItem("carrito", JSON.stringify(carritoLS));
    calcularSubtotal();
    actualizarCantidadCarrito();
}

// Eliminar producto
function eliminarProducto(boton) {
    const div = boton.closest(".carrito-producto");
    const nombre = div.querySelector(".nombre-carrito").textContent;

    //Obtener el tamaño del carrito
    const tamanoTexto = div.querySelector(".tamano-carrito").textContent;
    const tamano = tamanoTexto.replace("Tamaño: ", "").trim();

    let carritoLS = JSON.parse(localStorage.getItem("carrito")) || [];

    //Filtrar solo el producto exacto (nombre y tamaño)
    carritoLS = carritoLS.filter(p => !(p.nombre === nombre && p.tamano === tamano));

    localStorage.setItem("carrito", JSON.stringify(carritoLS));
    mostrarCarrito();
    actualizarCantidadCarrito();
    alert("Producto eliminado");
}

//Subtotal
function calcularSubtotal() {
    let carritoLS = JSON.parse(localStorage.getItem("carrito")) || [];
    const subtotal = carritoLS.reduce((sum, p) => sum + (p.precio * p.cantidad), 0);

    const subtotalLabel = document.querySelector(".subtotal-carrito");
    if (subtotalLabel) subtotalLabel.textContent = `S/${subtotal.toFixed(2)}`;
    localStorage.setItem("subtotal", subtotal.toFixed(2));
}

//Cantidad total de productos
function actualizarCantidadCarrito() {
    let carritoLS = JSON.parse(localStorage.getItem("carrito")) || [];
    let total = 0; 
    
    for (let i = 0; i < carritoLS.length; i++) {
        total += carritoLS[i].cantidad;
    }

    cantidad_carrito.textContent = total;
}

//Asociar botones de agregar al carrito
document.addEventListener("DOMContentLoaded", () => {
    cantidad_carrito = document.getElementById("cantidad_carrito");
    actualizarCantidadCarrito();
    document.querySelectorAll(".btn-precio").forEach(btn => {
        btn.addEventListener("click", () => {
            if (!productoActual) return;
            agregarAlCarrito(productoActual.nombre, productoActual.imagen, btn.textContent.replace("S/", ""));
        });
    });
});