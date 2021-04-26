FROM maven

COPY src /aqa/src
COPY pom.xml /aqa/pom.xml
WORKDIR /aqa

RUN mvn -f /aqa/pom.xml clean package

ENTRYPOINT ["mvn","test"]