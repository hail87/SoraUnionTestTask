#FROM webratio/java:8
#
#RUN apt-get update -y && \
#    apt-get install -y python python-pip python-virtualenv dos2unix && \
#    mkdir aws && \
#    virtualenv aws/env && \
#    ./aws/env/bin/pip install awscli && \
#    apt-get autoremove --purge -y && \
#    apt-get clean && \
#    rm -rf /var/lib/apt/lists/*

FROM maven

RUN mkdir -p /root/.m2 && mkdir /root/.m2/repository
COPY settings.xml /root/.m2

COPY src /aqa/src
COPY pom.xml /aqa/pom.xml
COPY .kube /aqa/.kube
COPY docker-startup.sh /aqa/docker-startup.sh

WORKDIR /aqa

RUN mvn -f /aqa/pom.xml clean install

EXPOSE 8080
CMD ["/bin/sh", "docker-startup.sh"]