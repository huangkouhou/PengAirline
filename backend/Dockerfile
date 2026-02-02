# ---------- build ----------
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# 1. 先只复制 pom.xml
COPY pom.xml .

# 2. [关键优化] 预先下载依赖 (这一步会被缓存，除非你改了 pom.xml)
# 这一步能节省大量的构建时间
RUN mvn dependency:go-offline -B

# 3. 再复制源代码
COPY src ./src


# 4. 开始打包 (因为依赖已经有了，这里只编译代码，飞快)
RUN mvn clean package -DskipTests

# ---------- runtime ----------
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]