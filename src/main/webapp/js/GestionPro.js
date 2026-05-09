const URL = "/productoCRUD";

    function mostrarFormulario(tipo) {
      document.querySelectorAll(".form-container").forEach(f => f.style.display = "none");
      document.getElementById("form" + tipo.charAt(0).toUpperCase() + tipo.slice(1)).style.display = "block";
    }

    function listarProductos() {
      const formData = new URLSearchParams();
      formData.append("accion", "listar");
      fetch(URL, {
        method: "POST",
        body: formData
    })
    .then(res => res.text())
    .then(html => document.getElementById("tablaProductos").innerHTML = html);
  }

    function enviarFormulario(formId) {
      const form = document.getElementById(formId);
      const formData = new FormData(form);
      fetch(URL, {
        method: "POST",
        body: formData
      })
      .then(res => res.text())
      .then(msg => {
        alert(msg);
        listarProductos();
        form.reset();
      });
    }

    function actualizarCamposPrecio(selectId, contenedorPreciosId) {
      const categoria = document.getElementById(selectId).value;
      
      //Determinar contenedor total de campos
      const contenedorCampos = (selectId === "categoriaAdd")
        ? document.getElementById("camposAdd")
        : document.getElementById("camposUpdate");

      const contenedorPrecios = document.getElementById(contenedorPreciosId);
      const precios = contenedorPrecios.querySelectorAll(".mb-3");

      //Si no hay categorias se oculta todo
      if(categoria === ""){
        contenedorCampos.style.display = "none";
        return;
      }
      // Mostrar todos los campos excepto categoría
      contenedorCampos.style.display = "block";

      // Ocultar todos los precios
      precios.forEach(i => i.style.display = "none");

      //Mostrar precios segun categoria
      if (categoria === "tortas") {
          // pequeño, mediano, grande
          precios[0].style.display = "block";
          precios[1].style.display = "block";
          precios[2].style.display = "block";
      }
      else if (categoria === "galletones") {
          precios[0].style.display = "block"; // pequeño
      }
      else if (categoria === "bocaditos") {
          precios[0].style.display = "block"; // x100unid
          precios[1].style.display = "block"; // pequeño y grande
      }
      else if (categoria === "dulces") {
          precios[0].style.display = "block"; //individual
          precios[1].style.display = "block"; //Pequeño y Grande
          precios[2].style.display = "block"; //6 unid - 12 uni - 24 uni
      }
    }


    document.addEventListener("DOMContentLoaded", () => {
      listarProductos();

      actualizarCamposPrecio("categoriaAdd", "preciosAdd");
      actualizarCamposPrecio("categoriaUpdate", "preciosUpdate");

      document.getElementById("formAdd").addEventListener("submit", e => {
            e.preventDefault(); enviarFormulario("formAdd");
        });
        document.getElementById("formDelete").addEventListener("submit", e => {
            e.preventDefault(); enviarFormulario("formDelete");
        });
        document.getElementById("formUpdate").addEventListener("submit", e => {
            e.preventDefault(); enviarFormulario("formUpdate");
        });
        document.getElementById("categoriaAdd")
        .addEventListener("change", () => actualizarCamposPrecio("categoriaAdd", "preciosAdd"));
        document.getElementById("categoriaUpdate")
        .addEventListener("change", () => actualizarCamposPrecio("categoriaUpdate", "preciosUpdate"));
    });
    listarProductos();