document.addEventListener("DOMContentLoaded", function () {
    cargarUsuarios();
});

function cargarUsuarios() {
    fetch("/listarClientes")
        .then(response => response.json())
        .then(data => {
            const tabla = document.getElementById("Usuarios");
            tabla.innerHTML = "";

            data.forEach(cliente => {
                const fila = document.createElement("tr");

                fila.innerHTML = `
                    <td>${cliente.id_cliente}</td>
                    <td>${cliente.nombres} ${cliente.apellidos}</td>
                    <td>${cliente.telefono}</td>
                    <td>${cliente.email}</td>
                `;

                tabla.appendChild(fila);
            });

        })
        .catch(error => {
            console.error("Error al cargar usuarios:", error);
        });
}