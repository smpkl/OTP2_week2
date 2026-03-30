# Use a known-good OpenJDK base image
FROM eclipse-temurin:17-jdk

# Optional: set up display (for GUI forwarding)
ENV DISPLAY=host.docker.internal:0.0

# Force container to use UTF-8
ENV LANG=C.UTF-8

ENV JAVA_TOOL_OPTIONS="-Dfile.encoding=UTF-8 -Dsun.jnu.encoding=UTF-8"

# Install dependencies for GUI + Maven build
RUN apt-get update && \
    apt-get install -y maven wget unzip libgtk-3-0 libgbm1 libx11-6 && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

# Install japanese font
RUN apt-get install -y \
    fonts-noto-cjk \
    fonts-dejavu-core \
    fontconfig && \
    fc-cache -fv

# Download JavaFX SDK 17
RUN wget https://download2.gluonhq.com/openjfx/17/openjfx-17_linux-x64_bin-sdk.zip -O /tmp/openjfx.zip && \
    unzip /tmp/openjfx.zip -d /opt && \
    rm /tmp/openjfx.zip

WORKDIR /app

# Copy project files
COPY pom.xml .
COPY src ./src

# Build the shaded JAR
RUN mvn clean package -DskipTests

# List target folder to check JAR
RUN ls -l target

# Copy fat jar
COPY target/test.jar app.jar

# Run the **shaded JAR** with JavaFX modules
CMD ["java", "--module-path", "/opt/javafx-sdk-17/lib", "--add-modules", "javafx.controls,javafx.fxml", "-jar", "target/test.jar"]