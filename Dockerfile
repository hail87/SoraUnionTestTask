FROM maven

LABEL maintainer="StatysTechAQAImage"

RUN aws codeartifact get-authorization-token --domain lwa --domain-owner 326726142239 --query authorizationToken --output text --region us-east-1 > token.xml
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