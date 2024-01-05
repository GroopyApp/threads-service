docker build --tag threads-service:latest --platform=linux/amd64 .
docker tag threads-service:latest aledanna/threads-service
docker push aledanna/threads-service
