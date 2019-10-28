FROM matthewjt/openjdk:8u181-jre-slim

MAINTAINER Matthew Tomasov matthewjt@gmail.com

ADD AudiobookRSS-0.0.1.jar /jar/services_latest.jar
ADD AudiobookRSS-0.0.1-sources.jar /jar/AudiobookRSS-0.0.1-sources.jar

ADD jvm_args.properties /jvm_args.properties

VOLUME ["/books"]
VOLUME ["/config"]

EXPOSE 80/tcp

CMD ["/etc/my_init"]
