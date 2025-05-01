#!/bin/sh

echo "🚀 Iniciando Ollama..."

# Levantar servidor en segundo plano
ollama serve &

# Guardamos el PID
OLLAMA_PID=$!

# Esperamos a que arranque
for i in $(seq 1 10); do
  if curl -s http://localhost:11434 > /dev/null; then
    echo "✅ Ollama ya está corriendo"
    break
  fi
  echo "⏳ Esperando a que Ollama arranque..."
  sleep 1
done

# Verificamos si el modelo 'mistral' ya está descargado
if ! ollama list | grep -q mistral; then
    echo "📥 Modelo 'mistral' no encontrado. Descargando..."
    ollama pull mistral
else
    echo "✅ Modelo 'mistral' ya está disponible."
fi

# Dejamos el servidor activo
wait $OLLAMA_PID

