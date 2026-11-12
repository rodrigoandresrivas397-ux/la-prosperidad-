# Documentación de la API REST - La Prosperidad

## 1. Información general

Proyecto: La Prosperidad
Tecnología: Java, Jakarta Servlet, Gson y PostgreSQL.
Servidor: Apache Tomcat 10.1.
Formato de intercambio: JSON.

URL base:
http://localhost:8081/LaProsperidadWeb/api

## 2. Servicios de productos

### 2.1 Consultar todos los productos

- Método: GET
- Ruta: /productos
- Descripción: Consulta todos los productos registrados.

Ejemplo de respuesta:
[
  {
    "id": 5,
    "nombre": "Lapicero negro",
    "descripcion": "Lapicero de tinta negra",
    "precio": 8000.0,
    "cantidad": 50
  }
]

### 2.2 Consultar un producto por ID

- Método: GET
- Ruta: /productos/{id}
- Descripción: Busca un producto por su identificador.

Ejemplo:
GET /productos/5

### 2.3 Registrar un producto

- Método: POST
- Ruta: /productos
- Tipo de contenido: application/json
- Descripción: Registra un producto nuevo.

Ejemplo de solicitud:
{
  "nombre": "Cuaderno",
  "descripcion": "Cuaderno cuadriculado",
  "precio": 5000,
  "cantidad": 10
}

Respuesta esperada:
{
  "mensaje": "Producto registrado correctamente"
}

### 2.4 Actualizar un producto

- Método: PUT
- Ruta: /productos/{id}
- Tipo de contenido: application/json
- Descripción: Actualiza los datos del producto indicado.

Ejemplo:
PUT /productos/5

Cuerpo JSON:
{
  "nombre": "Cuaderno actualizado",
  "descripcion": "Cuaderno de 100 hojas",
  "precio": 6000,
  "cantidad": 15
}

### 2.5 Eliminar un producto

- Método: DELETE
- Ruta: /productos/{id}
- Descripción: Elimina el producto indicado por su identificador.

Ejemplo:
DELETE /productos/5

## 3. Estructura de los datos

| Campo | Tipo | Descripción |
|---|---|---|
| id | Integer | Identificador del producto |
| nombre | String | Nombre del producto |
| descripcion | String | Descripción del producto |
| precio | Decimal | Precio del producto |
| cantidad | Integer | Cantidad disponible |

## 4. Validaciones y respuestas

La API utiliza códigos HTTP para indicar el resultado de las operaciones:

- 200: operación realizada correctamente.
- 201: registro creado correctamente.
- 400: solicitud o datos incorrectos.
- 404: producto no encontrado.
- 500: error interno del servidor.

Los campos nombre, precio y cantidad se validan antes de guardar o actualizar un producto. El precio y la cantidad no deben ser negativos.

## 5. Pruebas realizadas

Se realizaron pruebas desde PowerShell contra el servidor Tomcat:

- GET: consulta de productos realizada correctamente.
- POST: registro de un producto de prueba realizado correctamente.
- PUT: actualización del producto de prueba realizada correctamente.
- DELETE: eliminación del producto de prueba realizada correctamente.

Las cuatro operaciones CRUD fueron comprobadas con respuestas satisfactorias.

## 6. Base de datos

Motor: PostgreSQL.
Base de datos: prosperidad_db.
Tabla utilizada: productos.

La conexión se realiza mediante JDBC y el controlador PostgreSQL.
La contraseña de conexión se obtiene de la variable de entorno DB_PASSWORD y no se incluye en esta documentación.

## 7. Servicios REST de autenticación

### 7.1 Registro de usuarios

- Método: POST
- Ruta: /auth/registro
- URL completa: /api/auth/registro
- Tipo de contenido: application/json
- Descripción: Registra un nuevo usuario en el sistema.

Ejemplo de solicitud:

{
  "usuario": "usuario1",
  "password": "ClaveSegura123"
}

Respuesta exitosa:
- Código HTTP: 201
- Mensaje: Usuario registrado correctamente.

### 7.2 Inicio de sesión

- Método: POST
- Ruta: /auth/login
- URL completa: /api/auth/login
- Tipo de contenido: application/json
- Descripción: Verifica las credenciales de un usuario registrado.

Ejemplo de solicitud:

{
  "usuario": "usuario1",
  "password": "ClaveSegura123"
}

Respuesta exitosa:
- Código HTTP: 200
- Mensaje: Autenticación satisfactoria.

### 7.3 Validaciones de autenticación

- El usuario debe tener entre 3 y 50 caracteres.
- La contraseña debe tener mínimo 8 caracteres.
- Los datos se reciben en formato JSON.
- Las contraseñas se gestionan mediante el módulo de seguridad del proyecto.

### 7.4 Códigos HTTP de autenticación

- 200: autenticación satisfactoria.
- 201: usuario registrado.
- 400: datos incompletos o formato JSON incorrecto.
- 401: autenticación fallida.
- 404: servicio no encontrado.
- 409: nombre de usuario duplicado.
- 500: error interno o de base de datos.

