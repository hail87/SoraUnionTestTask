FROM maven

LABEL maintainer="StatysTechAQAImage"

COPY settings.xml /root/.m2/
COPY settings.xml /usr/share/maven/ref/

COPY src /aqa/src
COPY pom.xml /aqa/pom.xml
COPY .kube /aqa/.kube
COPY docker-startup.sh /aqa/docker-startup.sh

WORKDIR /aqa

RUN mvn -f /aqa/pom.xml clean install

EXPOSE 8080
CMD ["/bin/sh", "docker-startup.sh"]