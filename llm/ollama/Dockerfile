FROM debian:bookworm-slim

RUN apt-get update && apt-get install -y curl ca-certificates gnupg unzip sudo && rm -rf /var/lib/apt/lists/*

RUN curl -fsSL https://ollama.com/install.sh | bash

COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

EXPOSE 11434
VOLUME /root/.ollama

ENTRYPOINT ["/entrypoint.sh"]

