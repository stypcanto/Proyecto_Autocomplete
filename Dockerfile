# Etapa 1: Construcción del WAR
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2: Despliegue en Tomcat
FROM tomcat:10.1-jdk17-temurin
COPY --from=build /app/target/pagina_autocomplete-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
