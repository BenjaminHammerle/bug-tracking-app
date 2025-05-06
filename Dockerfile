# Stage that builds the application, a prerequisite for the running stage
FROM maven:3.8.1-openjdk-17-slim as build
RUN apt-get update && \
    apt-get install -y git openssh-client && \
    rm -rf /var/lib/apt/lists/*
RUN curl -sL https://deb.nodesource.com/setup_14.x | bash -
RUN apt-get update -qq && apt-get install -qq --no-install-recommends nodejs

# Stop running as root at this point
RUN useradd -m myuser
WORKDIR /usr/src/app/
RUN chown myuser:myuser /usr/src/app/
USER myuser

# Copy pom.xml and prefetch dependencies so a repeated build can continue from the next step with existing dependencies
COPY --chown=myuser pom.xml ./
RUN mvn dependency:go-offline -Pproduction

# Copy all needed project files to a folder
COPY --chown=myuser:myuser src src
COPY --chown=myuser:myuser src/main/frontend frontend

# Build the production package, assuming that we validated the version before so no need for running tests again.
RUN mvn clean package -DskipTests -Pproduction -Dvaadin.offlineKey=eyJraWQiOiI1NDI3NjRlNzAwMDkwOGU2NWRjM2ZjMWRhYmY0ZTJjZDI4OTY2NzU4IiwidHlwIjoiSldUIiwiYWxnIjoiRVM1MTIifQ.eyJhbGxvd2VkUHJvZHVjdHMiOlsidmFhZGluLWNoYXJ0cyIsInZhYWRpbi10ZXN0YmVuY2giLCJ2YWFkaW4tZGVzaWduZXIiLCJ2YWFkaW4tY2hhcnQiLCJ2YWFkaW4tYm9hcmQiLCJ2YWFkaW4tY29uZmlybS1kaWFsb2ciLCJ2YWFkaW4tY29va2llLWNvbnNlbnQiLCJ2YWFkaW4tcmljaC10ZXh0LWVkaXRvciIsInZhYWRpbi1ncmlkLXBybyIsInZhYWRpbi1tYXAiLCJ2YWFkaW4tc3ByZWFkc2hlZXQtZmxvdyIsInZhYWRpbi1jcnVkIiwidmFhZGluLWNvcGlsb3QiLCJ2YWFkaW4tZGFzaGJvYXJkIl0sInN1YiI6IjVmZjQ5YTBmLTIxZDAtNDkwYi05MDE4LTJjNGNmOTgxYzA0NCIsInZlciI6MSwiaXNzIjoiVmFhZGluIiwiYWxsb3dlZEZlYXR1cmVzIjpbImNlcnRpZmljYXRpb25zIiwic3ByZWFkc2hlZXQiLCJ0ZXN0YmVuY2giLCJkZXNpZ25lciIsImNoYXJ0cyIsImJvYXJkIiwiYXBwc3RhcnRlciIsInZpZGVvdHJhaW5pbmciLCJwcm8tcHJvZHVjdHMtMjAyMjEwIl0sIm1hY2hpbmVfaWQiOm51bGwsInN1YnNjcmlwdGlvbiI6IlZhYWRpbiBQcm8iLCJzdWJzY3JpcHRpb25LZXkiOm51bGwsIm5hbWUiOiJCZW5qYW1pbiBIYW1tZXJsZSIsImJ1aWxkX3R5cGVzIjpbInByb2R1Y3Rpb24iXSwiZXhwIjoxNzUwNDY0MDAwLCJpYXQiOjE3NDY1MzkyMDYsImFjY291bnQiOiJNYW5hZ2VtZW50IENlbnRlciBJbm5zYnJ1Y2sifQ.AYrVO65SBj2IcKOR28AlXps8-jtJV5PtLmAHubERvuYbJ57MFqHUnfBlXaX0JqcxI7UNmA1OnImy3UqGGPmGtRjFAayZ2PMDJq6lDr46mB7DAs-ao_62XUKmfgnD5la3C5FFzYy1hbPguHeHS_1wvvj_N-WFgapoXf8ikPAPFb673glh

# Running stage: the part that is used for running the application
FROM maven:3.8.1-openjdk-17-slim
RUN apt-get update && \
    apt-get install -y git openssh-client && \
    rm -rf /var/lib/apt/lists/*
RUN curl -sL https://deb.nodesource.com/setup_14.x | bash -
RUN apt-get update -qq && apt-get install -qq --no-install-recommends nodejs

COPY --from=build /usr/src/app/target/*.jar /usr/app/app.jar
RUN useradd -m myuser
USER myuser
EXPOSE 8080
CMD java -jar /usr/app/app.jar
