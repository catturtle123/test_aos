FROM amazoncorretto:17

# 타임존 설정
RUN ln -snf /usr/share/zoneinfo/Asia/Seoul /etc/localtime

# 빌드 아티팩트 복사
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} umc_aos.jar

# 엔트리포인트 설정
ENTRYPOINT ["java","-jar","/umc_aos.jar"]