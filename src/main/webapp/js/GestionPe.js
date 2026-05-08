document.addEventListener("DOMContentLoaded", () => {
    cargarPedidos();
});

function cargarPedidos() {
    fetch("/Proyecto_Fausta_Maven/GestionPedido")
        .then(response => response.json())
        .then(data => {
            const tbody = document.getElementById("listaPedidos");
            tbody.innerHTML = "";

            data.forEach(pedido => {
                const tr = document.createElement("tr");

                tr.innerHTML = `
                    <td>${pedido.id_pedido}</td>
                    <td>${pedido.nombreCliente}</td>
                    <td>${pedido.fecha}</td>
                    <td>
                        <select onchange="cambiarEstado(${pedido.id_pedido}, this.value)">
                            <option value="Pendiente" ${pedido.estado === "Pendiente" ? "selected" : ""}>Pendiente</option>
                            <option value="Finalizado" ${pedido.estado === "Finalizado" ? "selected" : ""}>Finalizado</option>
                            <option value="Cancelado" ${pedido.estado === "Cancelado" ? "selected" : ""}>Cancelado</option>
                        </select>
                    </td>
                    <td>${pedido.metodoEnvio}</td>
                    <td>
                        <button class="btn btn-sm btn-info" onclick='verDetalle(${JSON.stringify(pedido.detalles)})'>Detalle</button>
                        <button class="btn btn-sm btn-danger" onclick="eliminarPedido(${pedido.id_pedido})">Eliminar</button>
                    </td>
                `;

                tbody.appendChild(tr);
            });
        })
        .catch(error => console.error("Error cargando pedidos:", error));
}

function cambiarEstado(idPedido, nuevoEstado) {
    fetch("/Proyecto_Fausta_Maven/GestionPedido", {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: `accion=actualizarEstado&id_pedido=${idPedido}&estado=${nuevoEstado}`
    })
    .then(response => response.json())
    .then(data => {
        if (data.exito) {
            alert("Estado actualizado correctamente");
        } else {
            alert("Error al actualizar estado");
        }
    })
    .catch(error => console.error("Error actualizando estado:", error));
}

function eliminarPedido(idPedido) {
    if (!confirm("¿Deseas eliminar este pedido?")) return;

    fetch("/Proyecto_Fausta_Maven/GestionPedido", {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: `accion=eliminar&id_pedido=${idPedido}`
    })
    .then(response => response.json())
    .then(data => {
        if (data.exito) {
            alert("Pedido eliminado correctamente");
            cargarPedidos();
        } else {
            alert("Error al eliminar pedido");
        }
    })
    .catch(error => console.error("Error eliminando pedido:", error));
}

function verDetalle(detalles) {
    const tbody = document.getElementById("detallePedido");
    tbody.innerHTML = "";

    detalles.forEach(det => {
        const tr = document.createElement("tr");

        tr.innerHTML = `
            <td>${det.nombre}</td>
            <td>${det.tamano ? det.tamano : ""}</td>
            <td>${det.cantidad}</td>
            <td>${det.subtotal}</td>
        `;

        tbody.appendChild(tr);
    });

    // Mostrar modal usando Bootstrap 5
    const modal = new bootstrap.Modal(document.getElementById("modalDetalle"));
    modal.show();
}