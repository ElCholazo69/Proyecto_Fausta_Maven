document.addEventListener("DOMContentLoaded", () => {
    const usuario = JSON.parse(localStorage.getItem("usuario"));
    const form = document.getElementById("form-contacto");

    // Si no hay usuario, pedir login
    if (!usuario) {
        alert("Debes iniciar sesión para enviar una consulta");
        mostrarLogin();
        return;
    }

    // Auto-completar campos con datos del usuario
    document.getElementById("nombres").value = usuario.nombres || "";
    document.getElementById("correo").value = usuario.email || "";

    // Evento submit
    form.addEventListener("submit", async (e) => {
        e.preventDefault();

        const asunto = document.getElementById("asunto").value.trim();
        const motivo = document.getElementById("motivo").value.trim();
        const id_cliente = usuario.id;

        // Validación
        if (!asunto || !motivo) {
            alert("Por favor, rellenar todos los campos antes de enviar.");
            return;
        }

        const formData = new URLSearchParams();
        formData.append("id_cliente", id_cliente);
        formData.append("asunto", asunto);
        formData.append("motivo", motivo);

        try {
            const response = await fetch("/consulta", {
                method: "POST",
                headers: { "Content-Type": "application/x-www-form-urlencoded" },
                body: formData.toString()
            });

            if (response.ok) {
                alert("Tu consulta fue enviada correctamente. ¡Gracias!");
                form.reset();
            } else {
                alert("Hubo un problema al enviar tu consulta.");
            }
        } catch (error) {
            console.error("ERROR AL ENVIAR UNA CONSULTA: ", error);
            alert("Error de conexión con el servidor.");
        }
    });
});