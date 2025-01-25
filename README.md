# Springboot demo

![Java](https://img.shields.io/badge/Java-17-orange.svg) ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.1-brightgreen.svg) ![Gradle](https://img.shields.io/badge/Gradle-8.11.1-blue.svg) ![Docker](https://img.shields.io/badge/Docker-Supported-blue.svg)
***
Este proyecto es una aplicación desarrollada con Spring Boot que permite gestionar tópicos y categorías, usando una arquitectura hexagonal.
## Diagrama de clases
![CD](diagramas/CD.png)
## Diagrama de ER
![ERD](diagramas/ERD.png)
## Arquitectura
![Hexagonal](https://kaizen-solutions.net/Medias/content-editor/ARTICLES/clean_architecture.png)
***
## Content

1. [🚀 Setup](#setup)
  - [👷 Requisitos](#requirements)
    - [🔧 Docker](#docker)
    - [🔧 Docker Compose](#docker-compose)
    - [📦️ Clonar el repositorio y ejecutar el contenedor](#clonar-el-repositorio-y-ejecutar-el-contenedor)
    - [Notas adicionales](#notas-adicionales)
2. [🧪 Prueba con Postman](#-prueba-con-postman)
3. [✅ Resultados de la prueba](#test-result)
4. [🌐 Endpoints](#-endpoints)
  - [Category](#category)
    - [1. Get all Categories](#1-get-all-categories)
    - [2. Get Category by ID](#2-get-category-by-id)
    - [3. Search Categories by Name](#3-search-categories-by-name)
    - [4. Create Category](#4-create-category)
    - [5. Update Category](#5-update-category)
    - [6. Delete Category](#6-delete-category)
  - [Topic](#topic)
    - [1. Get all Topics](#1-get-all-topics)
    - [2. Get Topic by ID](#2-get-topic-by-id)
    - [3. Get Topics by Category ID](#3-get-topics-by-category-id)
    - [4. Search Topics by Title](#4-search-topics-by-title)
    - [5. Create Topic](#5-create-topic)
    - [6. Update Topic](#6-update-topic)
    - [7. Delete Topic](#7-delete-topic)
***
## Setup
Este proyecto utiliza Docker para crear una aplicación Spring Boot en un container.
A continuación, se detallan los pasos para instalar Docker, clonar el repositorio y 
ejecutar la aplicación usando `docker-compose`.

## Requirements

### Docker
Asegúrate de tener Docker instalado en tu máquina. Si no lo tienes, sigue estos pasos:

1. Ve a [Docker Desktop](https://www.docker.com/products/docker-desktop/) y descarga la versión adecuada para tu sistema operativo.
2. Sigue el asistente de instalación.
3. Después de instalar Docker, abre la aplicación.

### Docker Compose
Para macOS [Docker usando brew](https://formulae.brew.sh/formula/docker-compose)
```bash
brew install docker-compose
```
Para linux puedes seguir el [Manual](https://docs.docker.com/compose/install/linux/).
Para Ubuntu y Debian 
```bash
sudo apt-get update
sudo apt-get install docker-compose-plugin
```

### Clonar el repositorio y ejecutar el contenedor
1. Clona el repositorio de Git:
```bash 
git clone https://github.com/AddsDev/springboot-demo-campusland.git
```
```bash 
cd springboot-demo-campusland
```

2. (Opcional) Puedes construir el archivo .jar compilando el proyecto:
 - Asegurate de tener Java instalado en tu maquina (JDK 21 recomendado)
```bash
 java --version
```
- Si no lo tienes, sigue estos pasos para [macOs](https://medium.com/@aniketvishal/how-to-install-java-and-setup-java-home-path-in-macos-apple-silicon-m1-m2-2edf185b992c) o [Linux](https://www.java.com/es/download/help/linux_x64_install.html#install).
- Configura las propiedades de conexión para Flyway y Spring en el archivo `src/main/resources/application.properties` usando el valor opcional `${DB_USER: TU_USUARIO_DE_MYSQL}`, ejemplo para el usuario `dev-user-mysql` > `${DB_USER: dev-user-mysql}`.
```properties
spring.datasource.url=jdbc:mysql://${DB_HOST: TU_HOST_DE_MYSQL}:${DB_PORT: TU_PUERTO_DE_MYSQL}/${DB_NAME: TU_BASE_DE_DATOS_DE_MYSQL}
spring.datasource.username=${DB_USER: TU_USUARIO_DE_MYSQL}
spring.datasource.password=${DB_PASSWORD: TU_PASSWORD_DE_MYSQL}

spring.flyway.url=jdbc:mysql://${DB_HOST: TU_HOST_DE_MYSQL}:${DB_PORT: TU_PUERTO_DE_MYSQL}/${DB_NAME: TU_BASE_DE_DATOS_DE_MYSQL}
spring.flyway.user=${DB_USER: TU_USUARIO_DE_MYSQL}
spring.flyway.password=${DB_PASSWORD: TU_PASSWORD_DE_MYSQL}
```
- Compila el proyecto
```bash
./gradlew build
```
- Puedes encontrar el archivo .jar en `/build/libs/springboot-demo-0.0.1-SNAPSHOT.jar`
```bash
cd /build/libs/
```
3. Si quieres crear el contenedor con el archivo .jar del [Releases](https://github.com/AddsDev/springboot-demo-campusland/releases):
- Configura la ruta o el path del archivo [Dockerfile](Dockerfile)
> ⚠ **No modifiques este archivo si realizaste el paso** ⚠
```Dockerfile
# Copiar el archivo JAR desde el directorio build/libs
# Asegúrate de que el nombre del archivo JAR sea correcto según tu proyecto
# Asegúrate de haber compilado ejecutando ./gradlew build o ./gradlew clean build
# ADD {RUTA_LOCAL_DEL_JAR} {RUTA_DEL_CONTENERDOR_DEL_JAR}
ADD TU_NUEVA_RUTA/springboot-demo-0.0.1-SNAPSHOT.jar /app/springboot-demo-0.0.1-SNAPSHOT.jar
```
4. Configura las propiedades de conexión del archivo [docker file](Dockerfile)
```Dockerfile
# Variables de entorno para configurar el perfil y la base de datos
#  ⚠ NO MODIFICAR DB_HOST ⚠
#  ⚠ NOMBRE DEL CONTENEDOR DE COMPOSE ⚠
ENV DB_HOST=mysql-spring-crud
ENV SPRING_PROFILES_ACTIVE=prod

ENV DB_PORT=TU_PUERTO_DE_MYSQL
ENV DB_NAME=TU_BASE_DE_DATOS_DE_MYSQL
ENV DB_USER=TU_USUARIO_DE_MYSQL
ENV DB_PASSWORD=TU_PASSWORD_DE_MYSQL
```
5. Configura las propiedades de conexión del archivo [docker compose](docker-compose.yml)
- Para el servicio de mysql:
```yml
mysqldb:
    ...
    environment:
      MYSQL_DATABASE: 'TU_BASE_DE_DATOS_DE_MYSQL'
      MYSQL_USER: 'TU_USUARIO_DE_MYSQL'
      MYSQL_PASSWORD: 'TU_PASSWORD_DE_MYSQL'
      MYSQL_ROOT_PASSWORD: 'TU_PASSWORD_DE_MYSQL'
    ..
```
> Puedes omitir este paso si ya configuro las propiedades de conexión del archivo [docker file](Dockerfile) del paso 4.
- Para el servicio de la API con Spring:
```yml
springboot:
  ...
  environment:
      SPRING_PROFILES_ACTIVE: prod
      DB_HOST: 'mysql-spring-crud' # ⚠ NO MODIFICAR ⚠
      DB_PORT: 'TU_PUERTO_DE_MYSQL'
      DB_NAME: 'TU_BASE_DE_DATOS_DE_MYSQL'
      DB_USER: 'TU_USUARIO_DE_MYSQL'
      DB_PASSWORD: 'TU_PASSWORD_DE_MYSQL' 
  ...
```
5. Construye y ejecuta los contenedores con Docker Compose:
```bash
docker-compose up --build
```
### Notas adicionales
Asegúrate de que el puerto expuesto en el archivo `docker-compose.yml` no esté en conflicto con otro servicio en tu máquina.

***

## 🧪 Prueba con Postman
Para probar los Endpoints puedes usar un Cliente REST como [Postman](https://www.postman.com) o [Insomnia](https://insomnia.rest)
Importa los archivos JSON de `Collection` y `Environment` a Postman.

![Steps](postman/Import_postman.gif)

> [Collections](postman/api_springboot_demo.postman_collection.json)

> [Environment](postman/api_springboot_demo.postman_environment.json)

## Test Result
![ImageTest](postman/api_springboot_demo.png)
> [Resultados - Run collection test](postman/api_pringboot_demo.postman_test_run.json)

***
## 🚀🚀 Endpoints
***
## Category

### 1\. Get all Categories

**Endpoint:** `/category`  
**Method:** `GET`  
**Description:** Retrieve a paginated list of all categories.

#### Request:

- **Parameters:**

    - `page` (optional, default 0) – Page number.

    - `size` (optional, default 20) – Number of items per page.

    - `sort` (optional) – Sort fields, e.g., `name,desc,id,asc`.

- **Response:**
```json
 {
  "content": [
    {
      "id": 16,
      "name": "3D Printing",
      "createdAt": "18.03.2024"
    },
    {
      "id": 42,
      "name": "Agriculture",
      "createdAt": "19.08.2023"
    },
    {
      "id": 57,
      "name": "AI Ethics",
      "createdAt": "02.12.2023"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 3,
    "sort": {
      "empty": false,
      "unsorted": false,
      "sorted": true
    },
    "offset": 0,
    "unpaged": false,
    "paged": true
  },
  "totalPages": 20,
  "totalElements": 58,
  "last": false,
  "size": 3,
  "number": 0,
  "sort": {
    "empty": false,
    "unsorted": false,
    "sorted": true
  },
  "numberOfElements": 3,
  "first": true,
  "empty": false
}
```
### 2\. Get Category by ID

**Endpoint:** `/category/{id}`  
**Method:** `GET`  
**Description:** Retrieve details of a specific category by its ID.

#### Request:

- **Parameters:**

    - `id` (Path variable) – The ID of the category.
- **Response:**
```json
 {
  "id": 59,
  "name": "Lorem HTTP",
  "createdAt": "24.01.2025"
}
```

### 3\. Search Categories by Name

**Endpoint:** `/category/search`  
**Method:** `GET`  
**Description:** Search categories by name.

#### Request:

- **Parameters:**

    - `name` (Query parameter) – The name of the category to search.

    - `page`, `size`, `sort` (optional, default as above).

- **Response:**
```json
 {
  "content": [
    {
      "id": 19,
      "name": "Biotechnology",
      "createdAt": "10.04.2024"
    },
    {
      "id": 14,
      "name": "Blockchain Technology",
      "createdAt": "05.01.2024"
    },
    {
      "id": 31,
      "name": "Green Technology",
      "createdAt": "10.03.2024"
    },
    {
      "id": 47,
      "name": "Nanotechnology",
      "createdAt": "15.04.2024"
    },
    {
      "id": 1,
      "name": "Technology",
      "createdAt": "24.01.2025"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 5,
    "sort": {
      "empty": false,
      "unsorted": false,
      "sorted": true
    },
    "offset": 0,
    "unpaged": false,
    "paged": true
  },
  "totalPages": 1,
  "totalElements": 5,
  "last": true,
  "size": 5,
  "number": 0,
  "sort": {
    "empty": false,
    "unsorted": false,
    "sorted": true
  },
  "numberOfElements": 5,
  "first": true,
  "empty": false
}
```

### 4\. Create Category

**Endpoint:** `/category`  
**Method:** `POST`  
**Description:** Create a new category.

#### Request:

- **Body:**


``` json
{
  "name": "Lorem HTTP"
}
 ```
- **Response:**
```json
 {
  "id": 59,
  "name": "Lorem HTTP",
  "createdAt": "24.01.2025"
}
```

### 5\. Update Category

**Endpoint:** `/category/{id}`  
**Method:** `PUT`  
**Description:** Update an existing category by ID.

#### Request:

- **Parameters:**

    - `id` (Path variable) – The ID of the category to update.

- **Body:**


``` json
{
  "name": "New categor Update SMS"
}
 ```

- **Response:**
```json
 {
  "id": 59,
  "name": "New categor Update SMS",
  "createdAt": "24.01.2025"
}
```

### 6\. Delete Category

**Endpoint:** `/category/{id}`  
**Method:** `DELETE`  
**Description:** Delete a category by ID.

#### Request:

- **Parameters:**

    - `id` (Path variable) – The ID of the category to delete.

- **Response:**
```json
 {
  "id": 59,
  "name": "New categor Update SMS",
  "createdAt": "24.01.2025"
}
```
***
## Topic

### 1\. Get all Topics

**Endpoint:** `/topic`  
**Method:** `GET`  
**Description:** Retrieve a paginated list of all topics.

#### Request:

- **Parameters:**

    - `page`, `size`, `sort` (optional, default as above).

- **Response:**
```json
 {
  "content": [
    {
      "id": 5,
      "title": "World Cup 2023 Highlights",
      "description": "Top moments from the 2023 World Cup.",
      "rating": 4.6,
      "views": 2000,
      "category": {
        "id": 5,
        "name": "Sports",
        "createdAt": "24.01.2025"
      },
      "createdAt": "24.01.2025"
    },
    {
      "id": 6,
      "title": "Top 10 Movies of 2024",
      "description": "A list of must-watch movies this year.",
      "rating": 4.2,
      "views": 1500,
      "category": {
        "id": 6,
        "name": "Entertainment",
        "createdAt": "24.01.2025"
      },
      "createdAt": "24.01.2025"
    },
    {
      "id": 1,
      "title": "AI Revolution",
      "description": "Discussion on how AI is transforming industries.",
      "rating": 4.8,
      "views": 1250,
      "category": {
        "id": 1,
        "name": "Technology",
        "createdAt": "24.01.2025"
      },
      "createdAt": "24.01.2025"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 3,
    "sort": {
      "empty": false,
      "unsorted": false,
      "sorted": true
    },
    "offset": 0,
    "unpaged": false,
    "paged": true
  },
  "totalPages": 4,
  "totalElements": 11,
  "last": false,
  "size": 3,
  "number": 0,
  "sort": {
    "empty": false,
    "unsorted": false,
    "sorted": true
  },
  "numberOfElements": 3,
  "first": true,
  "empty": false
}
```

### 2\. Get Topic by ID

**Endpoint:** `/topic/{id}`  
**Method:** `GET`  
**Description:** Retrieve details of a specific topic by its ID.

#### Request:

- **Parameters:**

    - `id` (Path variable) – The ID of the topic.

- **Response:**
```json
 {
  "id": 11,
  "title": "Lorem Ipsum",
  "description": "Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500",
  "rating": 2.3,
  "views": 671,
  "category": {
    "id": 2,
    "name": "Health",
    "createdAt": "24.01.2025"
  },
  "createdAt": "24.01.2025"
}
```

### 3\. Get Topics by Category ID

**Endpoint:** `/topic/category/{id}`  
**Method:** `GET`  
**Description:** Retrieve a list of topics belonging to a specific category.

#### Request:

- **Parameters:**

    - `id` (Path variable) – The ID of the category.

- **Response:**
```json
 {
  "content": [
    {
      "id": 2,
      "title": "Mental Health Awareness",
      "description": "Resources and tips to manage mental health.",
      "rating": 4.5,
      "views": 980,
      "category": {
        "id": 2,
        "name": "Health",
        "createdAt": "24.01.2025"
      },
      "createdAt": "24.01.2025"
    },
    {
      "id": 11,
      "title": "Lorem Ipsum",
      "description": "Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500",
      "rating": 2.3,
      "views": 671,
      "category": {
        "id": 2,
        "name": "Health",
        "createdAt": "24.01.2025"
      },
      "createdAt": "24.01.2025"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 5,
    "sort": {
      "empty": false,
      "unsorted": false,
      "sorted": true
    },
    "offset": 0,
    "unpaged": false,
    "paged": true
  },
  "totalPages": 1,
  "totalElements": 2,
  "last": true,
  "size": 5,
  "number": 0,
  "sort": {
    "empty": false,
    "unsorted": false,
    "sorted": true
  },
  "numberOfElements": 2,
  "first": true,
  "empty": false
}
```

### 4\. Search Topics by Title

**Endpoint:** `/topic/search`  
**Method:** `GET`  
**Description:** Search topics by title.

#### Request:

- **Parameters:**

    - `title` (Query parameter) – The title to search for.

    - `page`, `size`, `sort` (optional, default as above).
- **Response:**
```json
 {
  "content": [
    {
      "id": 1,
      "title": "AI Revolution",
      "description": "Discussion on how AI is transforming industries.",
      "rating": 4.8,
      "views": 1250,
      "category": {
        "id": 1,
        "name": "Technology",
        "createdAt": "24.01.2025"
      },
      "createdAt": "24.01.2025"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 50,
    "sort": {
      "empty": false,
      "unsorted": false,
      "sorted": true
    },
    "offset": 0,
    "unpaged": false,
    "paged": true
  },
  "totalPages": 1,
  "totalElements": 1,
  "last": true,
  "size": 50,
  "number": 0,
  "sort": {
    "empty": false,
    "unsorted": false,
    "sorted": true
  },
  "numberOfElements": 1,
  "first": true,
  "empty": false
}
```

### 5\. Create Topic

**Endpoint:** `/topic`  
**Method:** `POST`  
**Description:** Create a new topic.

#### Request:

- **Body:**


``` json
{
  "title": "Lorem Ipsum",
  "categoryId": 2
}
 ```

- **Response:**
```json
 {
  "id": 11,
  "title": "Lorem Ipsum",
  "description": "Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500",
  "rating": 2.3,
  "views": 671,
  "category": {
    "id": 2,
    "name": "Health",
    "createdAt": "24.01.2025"
  },
  "createdAt": "24.01.2025"
}
```

### 6\. Update Topic

**Endpoint:** `/topic/{id}`  
**Method:** `PUT`  
**Description:** Update an existing topic by ID.

#### Request:

- **Parameters:**

    - `id` (Path variable) – The ID of the topic to update.

- **Body:**


``` json
{
  "title": "Lorem Ipsum update"
}

 ```

- **Response:**
```json
 {
  "id": 11,
  "title": "Lorem Ipsum update",
  "description": "Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500.",
  "rating": 4.3,
  "views": 699,
  "category": {
    "id": 1,
    "name": "Technology",
    "createdAt": "24.01.2025"
  },
  "createdAt": "24.01.2025"
}
```

### 7\. Delete Topic

**Endpoint:** `/topic/{id}`  
**Method:** `DELETE`  
**Description:** Delete a topic by ID.

#### Request:

- **Parameters:**

    - `id` (Path variable) – The ID of the topic to delete.

- **Response:**
```json
 {
  "id": 11,
  "title": "Lorem Ipsum update",
  "description": "Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500.",
  "rating": 4.3,
  "views": 699,
  "category": {
    "id": 1,
    "name": "Technology",
    "createdAt": "24.01.2025"
  },
  "createdAt": "24.01.2025"
}
```

***
### 📝 Autor

Este proyecto fue creado [AddsDev](https://github.com/AddsDev).  
Distribuido bajo la licencia Apache. © 2025 [AddsDev](https://github.com/AddsDev).