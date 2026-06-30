# Tahap 1: Build aplikasi menggunakan Maven dan JDK 25
FROM maven:3.8.5-openjdk-25 AS build
COPY . .
RUN mvn clean package -DskipTests

# Tahap 2: Jalankan aplikasi menggunakan JDK yang lebih ringan
FROM openjdk:17-jdk-slim
COPY --from=build /target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]