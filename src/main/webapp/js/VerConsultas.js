document.addEventListener("DOMContentLoaded", async () => {

    const contenedor = document.getElementById("contenedor-consultas");

    try {
        const response = await fetch("/Proyecto_Fausta_Maven/ListarConsultas");
        if (!response.ok) throw new Error("No se pudo obtener las consultas");

        const consultas = await response.json();

        if (consultas.length === 0) {
            contenedor.innerHTML = "<p>No hay consultas registradas.</p>";
            return;
        }

        consultas.forEach(c => {
            const div = document.createElement("div");
            div.className = "card p-3 mb-3 shadow-sm";

            div.innerHTML = `
                <p><strong>ID Cliente:</strong> ${c.id_cliente}</p>
                <p><strong>Asunto:</strong> ${c.asunto}</p>
                <p><strong>Motivo:</strong> ${c.motivo}</p>
            `;

            contenedor.appendChild(div);
        });

    } catch (error) {
        console.error("Error al cargar consultas:", error);
        contenedor.innerHTML = "<p>Error al cargar las consultas.</p>";
    }

});