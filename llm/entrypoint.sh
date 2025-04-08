#!/bin/sh

echo "🚀 Iniciando Ollama..."
ollama serve &

# Esperamos a que el servidor arranque
sleep 5

# Verificamos si el modelo 'mistral' ya está descargado
if ! ollama list | grep -q mistral; then
    echo "📥 Modelo 'mistral' no encontrado. Descargando..."
    ollama pull mistral
else
    echo "✅ Modelo 'mistral' ya está disponible."
fi

# Dejamos el servidor activo en primer plano
wait $OLLAMA_PI
