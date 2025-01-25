# Usar una imagen base ligera para Java 17
FROM amazoncorretto:17-alpine-jdk
# Autor del contenedor
LABEL authors="adrianruiz"

# Variables de entorno para configurar el perfil y la base de datos
ENV SPRING_PROFILES_ACTIVE=prod
ENV DB_HOST=mysql-spring-crud
ENV DB_PORT=3306
ENV DB_NAME=springboot_demo_db
ENV DB_USER=db_user
ENV DB_PASSWORD=db_password

# Crear un directorio para la aplicación
RUN mkdir -p /app/

# Copiar el archivo JAR desde el directorio build/libs
# Asegúrate de que el nombre del archivo JAR sea correcto según tu proyecto
# Asegúrate de haber compilado ejecutando ./gradlew build o ./gradlew clean build
ADD build/libs/springboot-demo-0.0.1-SNAPSHOT.jar /app/springboot-demo-0.0.1-SNAPSHOT.jar
# Comando para ejecutar la aplicación Spring Boot
ENTRYPOINT ["java", "-jar", "/app/springboot-demo-0.0.1-SNAPSHOT.jar"]