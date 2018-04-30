FROM jenkins

# Give Jenkins sudo
USER root
RUN apt-get update \
      && apt-get install -y sudo \
      && rm -rf /var/lib/apt/lists/*
RUN echo "jenkins ALL=NOPASSWD: ALL" >> /etc/sudoers

# Install Docker-in-Docker
# RUN apt-get install \
#     apt-transport-https \
#     ca-certificates \
#     curl \
#     software-properties-common
# RUN curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
# RUN add-apt-repository \
#    "deb [arch=amd64] https://download.docker.com/linux/ubuntu \
#    $(lsb_release -cs) \
#    stable"
# RUN apt-get update
# RUN apt-get install docker-ce

# missing from the jenkins image
RUN apt-get update && apt-get install -y libltdl7 && rm -rf /var/lib/apt/lists/*

ENTRYPOINT ["/bin/tini", "--", "/usr/local/bin/jenkins.sh"]
