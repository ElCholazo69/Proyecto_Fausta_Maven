# Fausta Pastelería - Sistema Web 🧁🍰

## 1. Descripción
El sistema web de Fausta Pastelería fue desarrollado utilizando Java Web bajo una arquitectura Modelo-Vista-Controlador (MVC), permitiendo una organización clara de la lógica de negocio, la presentación y el acceso a los datos.

La aplicación permite a los clientes consultar el catálogo de productos, registrarse, iniciar sesión, editar sus datos personales, realizar pedidos y generar comprobantes de compra. Asimismo, cuenta con un panel administrativo para la gestión de productos, consultas, pedidos y clientes.

El proyecto implementa una arquitectura por capas donde los Servlets actúan como controladores, las clases del modelo representan las entidades del sistema, las clases DAO gestionan el acceso a la base de datos y las vistas fueron desarrolladas principalmente con HTML, CSS y JavaScript, utilizando JSP únicamente como página inicial y para realizar las redirecciones necesarias.

## 2. Arquitectura del sistema
El sistema está organizado siguiendo el patrón de arquitectura MVC (Modelo-Vista-Controlador), permitiendo separar las responsabilidades de cada componente para facilitar el mantenimiento y la escalabilidad de la aplicación.

La estructura principal del proyecto se divide en:

- **Modelo (Model):** Contiene las clases que representan las entidades del sistema, como producto, consulta, descuento, detallepedido, clientes, pedido y empleado, además de las clases DAO encargadas de realizar las operaciones sobre la base de datos.
- **Vista (View):** Contiene las interfaces desarrolladas con HTML, CSS y JavaScript, responsables de la interacción con el usuario. Se utiliza JSP únicamente como página de inicio y para realizar las redirecciones correspondientes.
- **Controlador (Controller):** Está conformado por los Servlets, encargados de recibir las solicitudes del usuario, procesar la lógica correspondiente, comunicarse con el modelo y devolver la respuesta adecuada.

Esta organización permite mantener una separación clara entre la presentación, la lógica de negocio y el acceso a los datos.

## 3. Instalación y configuración

### Requisitos previos

- Java JDK 11.
- Apache Maven 3.8 o superior.
- Apache Tomcat 10 o superior.
- MySQL Server.

### Paso 1: Clonar el repositorio

```bash
git clone https://github.com/usuario/Proyecto_Fausta_Maven.git
cd Proyecto_Fausta_Maven
```

### Paso 2: Configurar la base de datos

1. Crear una base de datos en MySQL con el nombre:

```sql
CREATE DATABASE panaderiafausta;
```

2. Importar el archivo **`script_fausta.sql`**, ubicado en la raíz del proyecto, el cual contiene la estructura completa de la base de datos, incluyendo las tablas, relaciones, restricciones, datos iniciales y procedimientos almacenados necesarios para el funcionamiento de la aplicación.

### Paso 3: Configurar la conexión
Modificar las credenciales de conexión a la base de datos en la clase correspondiente, indicando el nombre de la base de datos, el usuario y la contraseña de MySQL según la configuración del entorno.

**Configuración manual de rutas JavaScript para desarrollo local:** 
   El código en el repositorio está configurado por defecto para producción en **Render** usando rutas absolutas desde la raíz (ej. `/registroCliente`). Para poder probar y ejecutar el proyecto en tu entorno local (`localhost`), debes modificar temporalmente las URLs de las peticiones `fetch`:
   
   * Abre los archivos JavaScript y pon el servidor local que es **Apache Tomcat** junto con el nombre del proyecto a cada ruta.
   
   **Ejemplo de cambio requerido para Localhost:**
   ```javascript
   // Código original en el repositorio (Modo Render):
   fetch("/registroCliente", { ... })

   // Cambio manual que debes aplicar (Modo Localhost):
   fetch("http://localhost:8080/Proyecto_Fausta_Maven/registroCliente", { ... })
   ```

### Paso 4: Ejecutar el proyecto en LocalHost

1. Abrir el proyecto en Apache NetBeans.
2. Configurar Apache Tomcat como servidor de aplicaciones.
3. Compilar y ejecutar el proyecto.
4. Acceder desde el navegador a:

```text
http://localhost:8080/Proyecto_Fausta_Maven
```

## 4. Tecnologías utilizadas

- **Java:** Desarrollo de la lógica de negocio de la aplicación.
- **Jakarta Servlet:** Implementación de los controladores siguiendo el patrón MVC.
- **JSP:** Página inicial y redirecciones dentro de la aplicación.
- **HTML5:** Estructuración de las interfaces de usuario.
- **CSS3:** Diseño y estilos visuales de la aplicación.
- **JavaScript:** Interactividad y validaciones del lado del cliente.
- **MySQL:** Sistema gestor de base de datos.
- **Apache Tomcat:** Servidor de aplicaciones para el despliegue del proyecto.
- **Apache Maven:** Gestión de dependencias y construcción del proyecto.

## 5. Funcionalidades principales

El sistema web de **Fausta Pastelería** implementa diversas funcionalidades que permiten a los clientes realizar pedidos de manera sencilla y a los administradores gestionar la información del negocio.

| Funcionalidad | Descripción |
|---|---|
| Registro de clientes | Permite crear una cuenta para acceder a las funcionalidades del sistema. |
| Inicio de sesión | Permite autenticar a clientes y administradores mediante correo electrónico o DNI y contraseña. |
| Catálogo de productos | Permite visualizar los productos disponibles organizados por categorías. |
| Carrito de compras | Permite agregar, actualizar y eliminar productos antes de confirmar el pedido. |
| Registro de pedidos | Permite realizar pedidos seleccionando el método de entrega y aplicando códigos de descuento cuando corresponda. |
| Generación de comprobantes | Permite generar y descargar el comprobante de compra en formato PDF. |
| Gestión del perfil | Permite al cliente actualizar su información personal. |
| Gestión de consultas | Permite registrar consultas o sugerencias dirigidas a la pastelería. |
| Panel administrativo | Permite al administrador gestionar productos, clientes, pedidos y consultas del sistema. |

## 6. Páginas implementadas

El sistema cuenta con diferentes páginas organizadas según el tipo de acceso y el rol del usuario.

### Páginas públicas

| Página | Descripción |
|---|---|
| `PaginaPrincipal.html` | Página principal donde se presenta la información general de Fausta Pastelería. |
| `Nosotros.html` | Presenta información sobre la historia, misión y valores de la pastelería. |
| `Categoria.html` | Presenta información sobre las categorías disponibles de los productos. |
| `Tienda_Tortas.html` | Permite visualizar el catálogo de tortas disponibles. |
| `Tienda_Galletones.html`  | Permite visualizar el catálogo de galletones disponibles. |
| `Tienda_Bocaditos.html` | Permite visualizar el catálogo de bocaditos disponibles. |
| `Tienda_Dulcess.html`| Permite visualizar el catálogo de dulces disponibles. |

### Páginas para clientes autenticados

| Página | Descripción |
|---|---|
| `Carrito.html`| Permite administrar los productos seleccionados antes de confirmar el pedido. |
| `PerfilUsuario.html` | Permite visualizar y actualizar la información personal del cliente. |
| `MisPedidos.html` | Permite consultar el historial de pedidos realizados. |
| `Contactanos.html` | Permite registrar consultas o sugerencias dirigidas a la pastelería. |

### Páginas administrativas

| Página | Descripción |
|---|---|
|`PanelAdministrador.html`| Página principal para la gestión del sistema. |
| `GestionProducto.html` | Permite registrar, modificar, eliminar y consultar productos. |
| `GestionPedido.html`  | Permite visualizar los pedidos realizados y actualizar su estado. |
| `VerUsuarios.html`| Permite consultar la información de los clientes registrados. |
| `Consultas.html` | Permite visualizar las consultas enviadas por los clientes. |

## 7. Acceso al sistema

El sistema implementa un mecanismo de autenticación mediante credenciales para controlar el acceso a las diferentes funcionalidades disponibles para clientes y administradores.

Las principales características implementadas son:

| Elemento                   | Descripción                                                                                                                                                               |
| -------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Inicio de sesión           | Permite autenticar a clientes y administradores mediante correo electrónico o DNI y contraseña.                                                                           |
| Sesiones HTTP              | Se utilizan sesiones para mantener la autenticación del usuario durante su navegación en la aplicación.                                                                   |
| localStorage               | Se emplea para almacenar información que debe mantenerse entre sesiones del navegador, como los productos del carrito de compras.                                         |
| sessionStorage             | Se utiliza para almacenar información temporal necesaria durante la navegación y el intercambio de datos entre páginas mientras la sesión del navegador permanece activa. |
| Acceso por tipo de usuario | El sistema diferencia las funcionalidades disponibles para clientes y administradores según el tipo de usuario autenticado.                                               |
| Validaciones               | Se realizan validaciones tanto en el cliente mediante JavaScript como en el servidor mediante Java y Servlets.                                                            |

## 8. Licencia

Este proyecto tiene carácter académico y fue desarrollado con fines demostrativos, de aprendizaje y evaluación.

Los derechos de desarrollo y diseño pertenecen a sus creadores y a **Fausta Pastelería**, siendo su uso destinado exclusivamente para fines educativos y como parte del portafolio profesional.

No se permite la distribución, modificación o uso comercial del proyecto sin la autorización correspondiente de sus propietarios.
