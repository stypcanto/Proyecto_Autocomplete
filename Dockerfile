# Usa una imagen base oficial de Tomcat con JDK 17
FROM tomcat:10.1-jdk17-temurin

# Elimina las aplicaciones web por defecto (ej: docs, examples)
RUN rm -rf /usr/local/tomcat/webapps/*

# Copia el archivo WAR generado por Maven al directorio de despliegue de Tomcat
COPY target/pagina_autocomplete-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# Exponer el puerto por defecto de Tomcat (8080)
EXPOSE 8080
