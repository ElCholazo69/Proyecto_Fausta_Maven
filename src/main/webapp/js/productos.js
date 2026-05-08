// Array global para productos mostrados (escalabilidad ps mascota)
let productosMostrados = [];

document.addEventListener("DOMContentLoaded", function() {
    const contenedorProductos = document.querySelector("#contenedor-productos");

    // Limpiar array global al cargar
    productosMostrados = [];

    // Detectar la categoría según el HTML actual
    let categoriaActual = "";
    const url = window.location.pathname.toLowerCase();

    if (url.includes("tienda_tortas.html")) {
        categoriaActual = "tortas";
    } else if (url.includes("tienda_galletones.html")) {
        categoriaActual = "galletones";
    } else if (url.includes("tienda_bocaditos.html")) {
        categoriaActual = "bocaditos";
    } else if (url.includes("tienda_dulces.html")) {
        categoriaActual = "dulces";
    }

    // Llamar al servlet para obtener los productos
    fetch("/Proyecto_Fausta_Maven/productoCRUD", {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: "accion=listarJson"
    })
    .then(res => res.json())
    .then(productos => {
        console.log("Productos desde el servidor:", productos);
        contenedorProductos.innerHTML = "";

        // Filtrar por categoría
        const filtrados = productos.filter(
            prod => prod.categoria.toLowerCase() === categoriaActual
        );

        if (filtrados.length === 0) {
            contenedorProductos.innerHTML = `<p class="text-center mt-4">No hay productos en esta categoría.</p>`;
            return;
        }

        // Mostrar productos
        filtrados.forEach(prod => {
            // Verificación de id_producto (escalable: salta si no válido)
            if (!('id_producto' in prod) || prod.id_producto === undefined || prod.id_producto === null) {
                console.warn(`Producto ${prod.nombre} no tiene id_producto válido. Saltando.`);
                return;  // Salta este producto
            }

            // Agregar al array global (solo productos válidos)
            productosMostrados.push(prod);

            const div = document.createElement("div");
            div.classList.add("producto");

            // Determinar etiquetas según la categoría
            let etiquetas = [];

            if (categoriaActual === "tortas" || categoriaActual === "galletones") {
                etiquetas = ["Pequeño", "Mediano", "Grande"];
            } else if (categoriaActual === "bocaditos") {
                const cantidadPrecios = [prod.precio1, prod.precio2, prod.precio3].filter(p => p).length;
                if (cantidadPrecios === 1) {
                    etiquetas = ["x100unid"];
                } else if (cantidadPrecios > 1) {
                    etiquetas = ["Pequeño", "Grande"];
                }
            } else if (categoriaActual === "dulces") {
                if (prod.precio3) etiquetas = ["6 uni", "12 uni", "24 uni"];
                else if (prod.precio2) etiquetas = ["Pequeño", "Grande"];
                else etiquetas = ["Individual"];
            }

            // Construcción HTML de precios dinámicamente
            let preciosHTML = "";

            if (prod.precio1 > 0 || prod.precio2 > 0 || prod.precio3 > 0) {
                preciosHTML = `
                    <div class="precios-texto">
                        <div class="fila-titulos">
                            ${etiquetas[0] && prod.precio1 > 0 ? `<span>${etiquetas[0]}</span>` : ""}
                            ${etiquetas[1] && prod.precio2 > 0 ? `<span>${etiquetas[1]}</span>` : ""}
                            ${etiquetas[2] && prod.precio3 > 0 ? `<span>${etiquetas[2]}</span>` : ""}
                        </div>
                        <div class="fila-precios">
                            ${prod.precio1 > 0 ? `<span class="precio">S/${prod.precio1}</span>` : ""}
                            ${prod.precio2 > 0 ? `<span class="precio">S/${prod.precio2}</span>` : ""}
                            ${prod.precio3 > 0 ? `<span class="precio">S/${prod.precio3}</span>` : ""}
                        </div>
                    </div>
                `;
            } else {
                preciosHTML = `<div class="precios-texto"><span>Precio no disponible</span></div>`;
            }

            //Si stock es 0, se pone el boton deshabilitado
            const botonHTML = prod.cantidad > 0 
                ? `<button class="btn-agregar" data-index="${productosMostrados.length - 1}">Agregar al carrito</button>`
                : `<button class="btn btn-secondary" disabled>Sin stock</button>`;
            // Estructura del producto (data-index para escalabilidad)
            div.innerHTML = `
                <img src="../img/${prod.imagen}" alt="${prod.nombre}">
                <p>Stock: <strong>${prod.cantidad}</strong> unidades</p>
                <h5>${prod.nombre}</h5>
                ${preciosHTML}
                ${botonHTML}
                <div class="opciones-precio" style="display:none"></div>
            `;

            contenedorProductos.appendChild(div);
        });

        inicializarBotonesCarrito();
    })
    .catch(err => {
        console.error("Error cargando productos:", err);
        contenedorProductos.innerHTML = `<p class="text-danger text-center mt-4">Error al cargar los productos.</p>`;
    });
});

function inicializarBotonesCarrito() {
    const botonesAgregar = document.querySelectorAll(".btn-agregar");

    botonesAgregar.forEach(boton => {
        boton.addEventListener("click", () => {
            const usuario = JSON.parse(localStorage.getItem("usuario"));

            if (!usuario) {
            alert("Debes iniciar sesión para agregar productos al carrito.");
            
            // Mostrar login
            mostrarLogin();

            return;
            }
            const productoDiv = boton.closest(".producto");
            const nombre = productoDiv.querySelector("h5").textContent;
            const imagen = productoDiv.querySelector("img").src;
            const preciosTexto = productoDiv.querySelectorAll(".fila-precios span");
            const contenedorOpciones = productoDiv.querySelector(".opciones-precio");

            // Si ya está visible, lo ocultamos (toggle)
            if (contenedorOpciones.style.display === "flex") {
                contenedorOpciones.style.display = "none";
                return;
            }

            // Limpiar y generar los botones según los precios
            contenedorOpciones.innerHTML = "";
            preciosTexto.forEach((p, index) => {
                const btn = document.createElement("button");
                btn.textContent = p.textContent;
                btn.classList.add("btn-precio");

                btn.addEventListener("click", () => {
                    const precio = p.textContent.replace("S/", "");

                    // Obtener tamaño asociado
                    const tamanos = productoDiv.querySelectorAll(".fila-titulos span");
                    const tamano = tamanos[index]?.textContent || "Sin tamaño";

                    // Recuperar prod desde el array global usando data-index (escalable)
                    const prodIndex = parseInt(boton.getAttribute("data-index"));
                    const prod = productosMostrados[prodIndex];
                    //Impedir agregar si stock = 0
                    if (prod.cantidad <= 0) {
                        alert("Este producto está agotado.");
                        return;
                    }

                    if (!prod || !prod.id_producto) {
                        alert("Error: Producto no encontrado o sin ID. No se puede agregar.");
                        return;
                    }

                    let carrito = JSON.parse(localStorage.getItem("carrito")) || [];

                    // Verificación si existe el producto o no
                    const existe = carrito.find(item => item.nombre === nombre && item.tamano === tamano);

                    if (existe) {
                        existe.cantidad++;
                    } else {
                        carrito.push({
                            id: prod.id_producto,  // Acceso seguro al ID
                            nombre,
                            precio,  // Mantén precio para mostrar
                            imagen,
                            cantidad: 1,
                            tamano: tamano,
                            precioIndex: index
                        });
                    }

                    localStorage.setItem("carrito", JSON.stringify(carrito));

                    // Verificación de que si es una función
                    if (typeof actualizarCantidadCarrito === 'function') {
                        actualizarCantidadCarrito();
                    }

                    alert(`Producto agregado: ${nombre} - ${tamano} - S/${precio}`);
                    contenedorOpciones.style.display = "none";
                });

                contenedorOpciones.appendChild(btn);
            });

            contenedorOpciones.style.display = "flex"; // mostrar los botones
        });
    });
}