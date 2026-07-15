# --- ETAPA 1: Builder (Construccion) ---
    FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
    WORKDIR /app
    COPY . .
    RUN mvn clean package -DskipTests
    
    # --- ETAPA 2: Runtime (Producto final) ---
    FROM eclipse-temurin:21-jre-alpine
    WORKDIR /app
    COPY --from=builder /app/target/*.jar orden_trabajo.jar
    ENTRYPOINT ["java", "-jar", "orden_trabajo.jar"]