FROM openjdk:17-alpine
COPY api/build/install/api/bin /deployments/bin
COPY api/build/install/api/lib /deployments/lib
WORKDIR /deployments/bin/
ENTRYPOINT [ "/deployments/bin/api" ]