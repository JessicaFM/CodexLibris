from fastapi import FastAPI, Query
import requests
import json
import os

OLLAMA_HOST = os.getenv("OLLAMA_HOST", "http://ollama:11434")

app = FastAPI(title="Books recomender")

@app.get("/recomender")
def recomend(text: str = Query(..., description="Text to recomend book")):
  prompt = f"Recomiendame libros parecidos a: {text}"

  response = requests.post(
    f"{OLLAMA_HOST}/api/generate",
    json={"model": "mistral", "prompt": prompt},
    stream=True
  )

  result = ""
  for line in response.iter_lines():
    if line:
      data = json.loads(line.decode("utf-8"))
      if "response" in data:
        result += data["response"]

  return { "response": result.strip()}