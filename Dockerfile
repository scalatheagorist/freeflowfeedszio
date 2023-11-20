ARG BUILD_DIR=/root/app

FROM adoptopenjdk/openjdk11:jdk-11.0.19_7-slim as build

# Arbeitsverzeichnis für den Build
WORKDIR ${BUILD_DIR}/

RUN apt-get update && \
    apt-get install -y gnupg2 curl wget && \
    echo "deb https://repo.scala-sbt.org/scalasbt/debian all main" | tee /etc/apt/sources.list.d/sbt.list && \
    echo "deb https://repo.scala-sbt.org/scalasbt/debian /" | tee /etc/apt/sources.list.d/sbt_old.list && \
    curl -sL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x2EE0EA64E40A89B84B2DF73499E82A75642AC823" | apt-key add && \
    apt-get update && \
    apt-get install -y sbt && \
    wget https://downloads.lightbend.com/scala/2.13.10/scala-2.13.10.tgz && \
    tar -xzf scala-2.13.10.tgz && \
    mv scala-2.13.10 /usr/share/scala && \
    rm scala-2.13.10.tgz && \
    ln -s /usr/share/scala/bin/scala /usr/local/bin/scala && \
    ln -s /usr/share/scala/bin/scalac /usr/local/bin/scalac

ENV SBT_OPTS="-Dsbt.rootdir=true"

WORKDIR /app

COPY ./build.sbt .
COPY ./project ./project
COPY ./src ./src

RUN sbt clean && \
    sbt compile && \
    sbt assembly

FROM adoptopenjdk/openjdk11:jre-11.0.19_7 as stage

WORKDIR /app

COPY ./src ./src
COPY ./data ./data
COPY --from=build /app/target/scala-2.13/freeflowfeedszio-assembly-1.0-SNAPSHOT.jar ./freeflowfeedszio.jar

CMD ["java", "-Xmx8g", "-XX:+UseG1GC", "-jar", "freeflowfeedszio.jar"]
