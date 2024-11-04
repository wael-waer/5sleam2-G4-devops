# Utiliser une image de base avec Java
FROM openjdk:11-jre-slim

# Définir le répertoire de travail dans le conteneur
WORKDIR /app

# Copier le fichier JAR dans le conteneur
COPY target/Foyer-0.0.1.jar app.jar

# Exposer le port sur lequel l'application écoutera
EXPOSE 8080

# Commande pour exécuter l'application
ENTRYPOINT ["java", "-jar", "app.jar"]
