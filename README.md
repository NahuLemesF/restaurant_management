# Restaurant Management Application

> ✅ **Production Ready** - Version 0.0.1-SNAPSHOT  
> 🔒 Secure | ⚡ Transactional | 💰 BigDecimal Precision | 📦 Immutable DTOs

## 📋 Descripción General
Esta aplicación es un sistema de gestión de restaurantes desarrollado en Java utilizando Spring Boot. Proporciona la capacidad de administrar clientes, menús, platos y órdenes, con un diseño basado en buenas prácticas de programación y arquitecturas modernas.

### ⚡ Características Técnicas Destacadas
- ✅ **DTOs como Java Records** - Inmutabilidad y código conciso
- ✅ **BigDecimal para precios** - Precisión monetaria perfecta
- ✅ **@Transactional completo** - Integridad de datos garantizada
- ✅ **Variables de entorno** - Configuración segura
- ✅ **Profiles de Spring** - Configuración por ambiente (dev/prod)
- ✅ **Validaciones robustas** - Jakarta Validation
- ✅ **Patrones de diseño** - Observer, Chain of Responsibility, Strategy
- ✅ **100% Tests pasando** - Cobertura comprehensiva

---

## Características Principales

### 1. Entidades
- **Client**: Representa a los clientes con datos personales y su tipo (común o frecuente).
- **Menu**: Contiene los menús disponibles, cada uno asociado con una lista de platos.
- **Dish**: Representa los platos con detalles como nombre, descripción, precio (BigDecimal) y tipo (común o popular).
- **Order**: Registra las órdenes de los clientes, cada una con una lista de platos seleccionados.

### 2. Relaciones
- Un cliente puede realizar múltiples órdenes.
- Una órden debe contener al menos un plato.
- Los platos están asociados a un menú.
- Una relación muchos-a-muchos entre platos y órdenes.

### 3. Características Técnicas
- **API RESTful** para todas las entidades.
- **Validación de datos** con `Jakarta Validation`.
- **Gestor de base de datos**: MySQL.
- **Hibernate** para persistencia de datos.
- **Swagger/OpenAPI** para documentación automática.

---

## Instalación y Ejecución

### 1. Prerrequisitos
- **Java 17** o superior.
- **Gradle**.
- **MySQL 8.0+**.

### 2. Configuración de la Base de Datos

#### a) Crear la base de datos
```sql
CREATE DATABASE restaurant_management;
CREATE USER 'restaurant'@'localhost' IDENTIFIED BY 'tu_contraseña_segura';
GRANT ALL PRIVILEGES ON restaurant_management.* TO 'restaurant'@'localhost';
FLUSH PRIVILEGES;
```

#### b) Ejecutar migración (IMPORTANTE - Solo primera vez)
```bash
mysql -u restaurant -p restaurant_management < migration.sql
```

#### c) Configurar variables de entorno
```bash
# Copiar el archivo de ejemplo
cp .env.example .env

# Editar con tus credenciales
nano .env
```

**Contenido del .env:**
```bash
DB_URL=jdbc:mysql://localhost:3306/restaurant_management?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
DB_USERNAME=restaurant
DB_PASSWORD=tu_contraseña_segura
DDL_AUTO=update
SHOW_SQL=true
FORMAT_SQL=true
```

### 3. Compilación y Ejecución

#### Desarrollo
```bash
# Cargar variables de entorno
export $(cat .env | xargs)

# Ejecutar con profile de desarrollo
./gradlew bootRun --args='--spring.profiles.active=dev'
```

#### Producción
```bash
# Variables de entorno en el servidor
export DB_URL=jdbc:mysql://prod-server:3306/restaurant_management
export DB_USERNAME=prod_user
export DB_PASSWORD=SECURE_PASSWORD
export DDL_AUTO=validate
export SHOW_SQL=false

# Compilar
./gradlew clean build -x test

# Ejecutar
java -jar build/libs/restaurant-management-0.0.1-SNAPSHOT.jar \
  --spring.profiles.active=prod
```

La aplicación estará disponible en: [http://localhost:8080/api/v1](http://localhost:8080/api/v1)

---

## Endpoints Principales

### Client
- **GET** `/clients`: Obtiene todos los clientes.
- **POST** `/clients`: Crea un nuevo cliente.
- **GET** `/clients/{id}`: Obtiene un cliente por su ID.
- **PUT** `/clients/{id}`: Actualiza un cliente.
- **DELETE** `/clients/{id}`: Elimina un cliente.

### Menu
- **GET** `/menus`: Obtiene todos los menús.
- **POST** `/menus`: Crea un nuevo menú.
- **GET** `/menus/{id}`: Obtiene un menú por su ID.
- **PUT** `/menus/{id}`: Actualiza un menú.
- **DELETE** `/menus/{id}`: Elimina un menú.

### Dish
- **GET** `/dishes`: Obtiene todos los platos.
- **POST** `/dishes`: Crea un nuevo plato.
- **GET** `/dishes/{id}`: Obtiene un plato por su ID.
- **PUT** `/dishes/{id}`: Actualiza un plato.
- **DELETE** `/dishes/{id}`: Elimina un plato.

### Order
- **GET** `/orders`: Obtiene todas las órdenes.
- **POST** `/orders`: Crea una nueva órden.
- **GET** `/orders/{id}`: Obtiene una órden por su ID.
- **PUT** `/orders/{id}`: Actualiza una órden.
- **DELETE** `/orders/{id}`: Elimina una órden.

---

## Diagrama de Clases

![image](https://github.com/user-attachments/assets/3ea543e7-8874-4aca-bf64-5317a3106ac8)

El sistema cuenta con un diagrama UML que describe las relaciones entre las entidades:
- Asociaciones: `1..*`, `0..*` según los requisitos del negocio.
- Tipos de relaciones: composición y agregación.

---

## Diagrama Relacional

![image](https://github.com/user-attachments/assets/34293338-0d98-4ee2-a3d6-13aec99d0efb)

El diagrama relacional representa las relaciones entre las tablas de la base de datos utilizadas en el sistema de gestión de restaurantes. 
Este modelo incluye las claves primarias, claves foráneas y las relaciones (1:1, 1:N, N:M) entre las tablas.

### Relación entre las tablas

- **Client**: Relacionado con **Order** (1 Cliente puede tener 0 o más Órdenes).
- **Order**: Relacionado con **Dish** (N:M) y con **Client** (N:1).
- **Dish**: Relacionado con **Menu** (N:1) y **Order** (N:M).
- **Menu**: Relacionado con **Dish** (1:N).

---

## Autor
Proyecto desarrollado por Nahu Lemes.

