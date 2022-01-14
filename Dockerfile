FROM maven

LABEL maintainer="StatysTechAQAImage"

ENV aws_cli=1.11.136

RUN apt-get update && apt-get install -y jq python-pip python-dev
# Install AWS-CLI
RUN pip install awscli==${aws_cli}

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