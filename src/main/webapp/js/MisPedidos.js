let pedidosUsuario = []; // Array para almacenar los pedidos cargados

document.addEventListener("DOMContentLoaded", () => {
    cargarPedidos();
});

/*--CARGAR PEDIDOS DEL USUARIO--*/
function cargarPedidos() {
    const usuario = JSON.parse(localStorage.getItem("usuario"));
    const cont = document.getElementById("listaPedidos");
    //Validar si el usuario inicio sesión
    if (!usuario) {
        console.warn("No hay usuario logueado.");
        if (cont) cont.innerHTML = "<p>Debes iniciar sesión para ver tus pedidos.</p>";
        return;
    }
    //Detectar si admin accedio a MisPedidos.html
    if(usuario.tipo === 'empleado'){
        console.warn("Se detecto un admin acceder pedidos.");
        if (cont) cont.innerHTML = "<p>Solo los usuarios de tipo cliente pueden ver sus pedidos.</p>";
        return;
    }

    const idCliente = usuario.id;

    fetch(`/ListarPedidos?idCliente=${idCliente}`)
        .then(res => {
            if (!res.ok) throw new Error("Respuesta no OK: " + res.status);
            return res.json();
        })
        .then(data => {
            pedidosUsuario = data; // Guardamos los pedidos en memoria
            mostrarPedidos(data);
        })
        .catch(err => {
            console.error("Error cargando pedidos:", err);
            if (cont) cont.innerHTML = `<p class="text-danger">Error cargando pedidos: ${err.message}</p>`;
        });
}

/*--UTIL: obtener precio unitario desde el detalle (maneja 0/null en precios)--*/
function obtenerPrecioUnitarioDesdeDetalle(d) {
    const p1 = Number(d.precio1) || 0;
    const p2 = Number(d.precio2) || 0;
    const p3 = Number(d.precio3) || 0;

    const tam = (d.tamano || "").toString().trim().toLowerCase();

    let preferencia = null;
    if (tam.startsWith("s") || tam.includes("pequ")) preferencia = 0;
    else if (tam.startsWith("m") || tam.includes("med")) preferencia = 1;
    else if (tam.startsWith("l") || tam.includes("gr")) preferencia = 2;

    const precioPorIndice = (idx) => {
        if (idx === 0) return p1;
        if (idx === 1) return p2;
        if (idx === 2) return p3;
        return 0;
    };

    if (preferencia !== null) {
        const pref = precioPorIndice(preferencia);
        if (pref > 0) return pref;
    }

    if (p1 > 0) return p1;
    if (p2 > 0) return p2;
    if (p3 > 0) return p3;

    const cantidad = Number(d.cantidad) || 0;
    const subtotal = Number(d.subtotal) || 0;
    if (cantidad > 0) return subtotal / cantidad;

    return 0;
}

/*--MOSTRAR PEDIDOS--*/
function mostrarPedidos(lista) {
    const contenedor = document.getElementById("listaPedidos");
    contenedor.innerHTML = "";

    if (!Array.isArray(lista) || lista.length === 0) {
        contenedor.innerHTML = `<p>No tienes pedidos registrados.</p>`;
        return;
    }

    lista.forEach(p => {
        let color = "#999";
        if (p.estado?.toUpperCase() === "PENDIENTE") color = "#f1c40f";
        if (p.estado?.toUpperCase() === "CANCELADO") color = "#e74c3c";
        if (p.estado?.toUpperCase() === "FINALIZADO") color = "#27ae60";

        const botonPDF = (p.estado?.toUpperCase() === "FINALIZADO")
            ? `<button class="btn btn-primary btn-sm ms-2" onclick="descargarPDF(${p.id_pedido})">Descargar PDF</button>`
            : "";

        const btnDetalles = `<button class="btn btn-outline-secondary btn-sm" onclick="toggleDetalles(${p.id_pedido})">Ver detalles</button>`;

        let filas = "";
        let subtotalCalc = 0;

        if (Array.isArray(p.detalles)) {
            p.detalles.forEach(d => {
                const cantidad = Number(d.cantidad) || 0;
                const precioUnit = obtenerPrecioUnitarioDesdeDetalle(d);
                const importe = precioUnit * cantidad;

                subtotalCalc += importe;

                filas += `
                    <tr>
                        <td>${escapeHtml(d.producto)}</td>
                        <td class="text-center">${escapeHtml(d.tamano || "-")}</td>
                        <td class="text-center">${cantidad}</td>
                        <td class="text-end">S/ ${precioUnit.toFixed(2)}</td>
                        <td class="text-end">S/ ${importe.toFixed(2)}</td>
                    </tr>
                `;
            });
        }

        const descuento = Number(p.descuento) || 0;

        // Subtotal con descuento
        const subtotalConDescuento = Math.max(subtotalCalc - descuento, 0);

        // IGV
        const igv = subtotalConDescuento > 0 ? subtotalConDescuento * 0.18 : 0;

        // Total
        const total = subtotalConDescuento + igv;

        contenedor.innerHTML += `
            <div class="card mb-3 p-3">
                <div class="d-flex justify-content-between align-items-start">
                    <div>
                        <h5>Pedido #${p.id_pedido}</h5>
                        <div style="display:inline-block;background:${color};color:#fff;padding:6px 10px;border-radius:6px;">
                            ${escapeHtml(p.estado)}
                        </div>
                    </div>
                    <div class="text-end">
                        ${btnDetalles}
                        ${botonPDF}
                    </div>
                </div>

                <div class="mt-2">
                    <p class="mb-1"><strong>Fecha:</strong> ${escapeHtml(p.fecha)}</p>
                    <p class="mb-1"><strong>Método:</strong> ${escapeHtml(p.metodo_envio || "")}</p>
                    ${p.direccion ? `<p class="mb-1"><strong>Dirección:</strong> ${escapeHtml(p.direccion)}</p>` : ""}
                </div>

                <div id="detalles-${p.id_pedido}" style="display:none; margin-top:12px;">
                    <div class="table-responsive">
                        <table class="table table-bordered">
                            <thead class="table-light">
                                <tr>
                                    <th>Producto</th>
                                    <th class="text-center">Tamaño</th>
                                    <th class="text-center">Cantidad</th>
                                    <th class="text-end">Precio Unit.</th>
                                    <th class="text-end">Importe</th>
                                </tr>
                            </thead>
                            <tbody>
                                ${filas}
                            </tbody>
                        </table>
                    </div>

                    <div class="d-flex justify-content-end">
                        <div style="width: 260px;">
                            <p class="mb-1"><strong>Subtotal:</strong> S/ ${subtotalCalc.toFixed(2)}</p>
                            <p class="mb-1"><strong>IGV (18%):</strong> S/ ${igv.toFixed(2)}</p>
                            <p class="mb-1"><strong>Descuento del subtotal:</strong> S/ ${descuento.toFixed(2)}</p>
                            <hr>
                            <h5 class="text-end">TOTAL: S/ ${total.toFixed(2)}</h5>
                        </div>
                    </div>
                </div>
            </div>
        `;
    });
}

// Descargar PDF usando el id del pedido
function descargarPDF(idPedido) {
    window.open(`/GenerarBoleta?idPedido=${idPedido}`, '_blank');
}

// Mostrar/ocultar detalles
function toggleDetalles(id) {
    const el = document.getElementById("detalles-" + id);
    if (!el) return;
    el.style.display = (el.style.display === "none" || el.style.display === "") ? "block" : "none";
}

// Escapar HTML
function escapeHtml(text) {
    if (text == null) return "";
    return String(text)
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;");
}