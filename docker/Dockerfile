# 기본 베이스 이미지: Alpine Linux
FROM alpine:3.18

# Temurin(Adoptium) OpenJDK 17 JRE 설치
RUN apk add --no-cache openjdk17-jre

# JAVA_HOME과 PATH 설정
ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk
ENV PATH="$JAVA_HOME/bin:${PATH}"

# 홈 디렉터리 및 서비스/배너 파일 생성
RUN mkdir -p /root/app/log/backup \
    && touch /root/.bashrc \
    && mkdir -p /etc/systemd/system \
    && touch /etc/systemd/system/app.service \
    && touch /etc/motd \
    && touch /root/app/app.jar \
    && touch /root/app/log/app.log \
    && touch /root/app/log/truncate_log.sh

# entrypoint(테스트용, 필요시 실제 app 실행으로 변경)
CMD [ "sh" ]

# 기본 동작: 버전 확인
CMD ["java", "-version"]