FROM maven

LABEL maintainer="StatysTechAQAImage"

RUN apt-get update && apt-get -y install groff
RUN curl --silent --show-error --fail "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
RUN unzip awscliv2.zip
RUN ./aws/install
RUN curl --silent --show-error --fail "https://s3.amazonaws.com/amazon-ecs-cli/ecs-cli-linux-amd64-latest" -o "/usr/local/bin/ecs-cli"
RUN chmod +x /usr/local/bin/ecs-cli

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