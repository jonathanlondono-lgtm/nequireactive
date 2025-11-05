FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp

# Crear usuario no-root para seguridad
RUN addgroup -g 1001 appgroup && \
    adduser -D -u 1001 -G appgroup appuser

COPY *.jar retoreactivo.jar

# Dar permisos al usuario appuser
RUN chown appuser:appgroup retoreactivo.jar

ENV JAVA_OPTS=" -Xshareclasses:name=cacheapp,cacheDir=/cache,nonfatal -XX:+UseContainerSupport -XX:MaxRAMPercentage=70 -Djava.security.egd=file:/dev/./urandom"

# Cambiar a usuario no-root
USER appuser

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS  -jar retoreactivo.jar" ]
