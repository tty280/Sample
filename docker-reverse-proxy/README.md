# Nginx Reverse Proxy using Docker

@Author TTY280

### Description

These are used to build docker images of nginx and a simple flask application. Nginx is configured to be used as a reverse proxy to the flask application. Both docker images are deployed using docker compose.

### Prerequisite(s)

- Docker version 19.03.8+


### Running the Docker Containers

1. Copy your certificate(s) and private key(s) to the ssl folder. If you're not planning on using SSL, make sure to comment the SSL section in the nginx.conf, starting from 'server { listen 443 ssl;'

2. Run docker build in the nginx and app directory to build the images: 'sudo docker build -t <image name> .'

3. Run docker compose in parent directory where the docker-compose.yml is stored. Example of command to use: 'sudo docker-compose up' or 'sudo docker-compose up --detach', to run in the background

4. Run the curl command to verify nginx and the flask application is working: 'curl https://localhost' or 'curl -k https://localhost', if you're running insecure certificates for testing purposes.

5. The curl results should output the following:


### Troubleshooting

1. If the containers are not running, verify the the docker images are built properly by inspecting the build logs. This includes verifying the dependencies are installed, and or not missing.
s
2. If the docker images are built properly, review the logs after running docker-compose. There should be a hint of what service failed to start. For example, a misconfiguration in the nginx.conf may prevent nginx from starting, or a code syntax error in the flask application.

3. If the logs are not showing anything unsual, go to the Dockerfiles, uncomment '#ENTRYPOINT ["tail", "-f", "/dev/null"]' as this will allow the container to run without the service running in the background. Build the docker image: 'docker build -t <image name> .', and run the image manually: 'docker run -d -p <port> <image name>'. Login to the docker container: 'docker exec -it <container name> /bin/bash'. From here, try running the nginx service or flask application manually, and investigate what could be causing the container to quit unexpectedly.