FROM maven

COPY src /aqa/src
COPY pom.xml /aqa/pom.xml
COPY docker-startup.sh /aqa/docker-startup.sh
WORKDIR /aqa

RUN mvn -f /aqa/pom.xml clean install

EXPOSE 8080
CMD ["/bin/sh", "docker-startup.sh"]