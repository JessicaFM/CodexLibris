#!/bin/sh
ollama serve &

OLLAMA_PID=$!

for i in $(seq 1 20); do
  if curl -s http://localhost:11434/api/tags > /dev/null; then
    echo "✅ Ollama listo"
    break
  fi
  echo "⏳ Esperando a Ollama..."
  sleep 1
done

if ! ollama list | grep -q mistral; then
  echo "📥 Descargando modelo mistral..."
  ollama pull mistral
else
  echo "✅ Modelo mistral ya está disponible."
fi

wait $OLLAMA_PID

