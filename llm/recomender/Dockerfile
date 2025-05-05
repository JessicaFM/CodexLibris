FROM python:3.10-slim

# Instalar curl y pip dependencies
RUN apt-get update && apt-get install -y curl

WORKDIR /app

# Instalar dependencias de Python
COPY app/requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# Copiar código y script de arranque
COPY app/ ./app/
COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

CMD ["/entrypoint.sh"]