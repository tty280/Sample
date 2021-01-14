# Pull the nginx 1.19.6 as base image
FROM nginx:1.19.6

MAINTAINER TTY280

# Install nano
RUN \
  apt-get update && \
  apt-get install -y nano

# Copy nginx configuration to working directory
COPY ./nginx.conf /etc/nginx/nginx.conf

# Expose ports
EXPOSE 80 
EXPOSE 443

# Define nginx command
CMD ["nginx", "-g", "daemon off;"]

# For debugging container
#ENTRYPOINT ["tail", "-f", "/dev/null"]