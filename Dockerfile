# --- Etapa 1: Construção (Build) ---
# Usamos a imagem oficial do Maven com Java 11
FROM maven:3.8.6-openjdk-11 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# --- Etapa 2: Execução (Run) ---
# MUDANÇA AQUI: Usamos 'eclipse-temurin' que é a imagem atual e estável do Java 11
FROM eclipse-temurin:11-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]