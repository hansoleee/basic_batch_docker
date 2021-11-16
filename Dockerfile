FROM openjdk:11-jdk

WORKDIR /usr/src/app
COPY ./build/libs/basic_batch_docker.jar /usr/src/app

RUN apt-get update && apt-get upgrade -y
RUN apt-get install -y openjdk-11-jdk

RUN java -jar basic_batch_docker.jar --JOB=sourceToTarget requestDate=2021-10-01

CMD ["/bin/bash", "java -jar basic_batch_docker.jar", "--JOB=sourceToTarget", "requestDate=2021-10-01"]