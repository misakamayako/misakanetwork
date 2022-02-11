FROM openjdk:8-jdk
EXPOSE 8080:8080
RUN mkdir /app
RUN ["./gradlew.bat","shadowJar"]
COPY ./build/libs/misakanetwork-backend-0.0.1-all.jar /app/
WORKDIR /app/
CMD ["java", "-server", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap", "-XX:InitialRAMFraction=2", "-XX:MinRAMFraction=2", "-XX:MaxRAMFraction=2", "-XX:+UseG1GC", "-XX:MaxGCPauseMillis=100", "-XX:+UseStringDeduplication", "-jar", "misakanetwork-backend-0.0.1-all.jar"]
