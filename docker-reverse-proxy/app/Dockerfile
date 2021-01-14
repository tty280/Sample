# Pull the python 3.9.1 as base image
FROM python:3.9.1

MAINTAINER TTY280

# Install nano
RUN \
  apt-get update && \
  apt-get install -y nano

# Copy requirements
COPY ./src/requirements.txt /app/requirements.txt

#Define working directory for app
WORKDIR /app

# Install dependencies from requirements
RUN pip3 install -r /app/requirements.txt

# Copy to app folder
COPY ./src /app

# Expose port
EXPOSE 5000

# Set environment variable for flask
ENV FLASK_APP "/app/hello.py"
ENV FLASK_ENV development
ENV FLASK_DEBUG False

# Define command to run flask
CMD flask run --host=0.0.0.0

# For debugging container
#ENTRYPOINT ["tail", "-f", "/dev/null"]