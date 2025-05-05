from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
import requests
import json
import os

app = FastAPI(title="Books Recommender")

OLLAMA_HOST = os.getenv("OLLAMA_HOST", "http://ollama:11434")


class RecomendRequest(BaseModel):
    text: str


@app.post("/recomender")
def recomend(data: RecomendRequest):
    #prompt = f"Recomiéndame libros parecidos a: {data.text}" "Devuelve el resultado como un array JSON de objetos con las claves: titulo, autor y anio."

    prompt = (
        "Actúa como un experto en literatura. "
        f"A partir de esta información: {data.text}, "
        "recomienda al menos 5 libros relacionados. "
        "Devuelve el resultado en formato JSON como un array de objetos, "
        "cada uno con: titulo, autor y anio. "
        "Si no puedes devolver JSON, responde en lista clara tipo markdown."
    )

    try:
        response = requests.post(
            f"{OLLAMA_HOST}/api/chat",
            json={
                "model": "gemma:2b",
                "messages": [
                    {"role": "user", "content": prompt}
                ],
                "stream": False
            },
            timeout=60
        )

        if response.status_code != 200:
            raise HTTPException(status_code=500, detail="Error al generar la recomendación")

        data = response.json()
        raw_response = data.get("message", {}).get("content", "")

        try:
            libros = json.loads(raw_response)
            if isinstance(libros, list):
                return {"response": libros}
            else:
                return {"response": raw_response}
        except json.JSONDecodeError:
            return {"response": raw_response}
        

    except Exception as e:
        print("💥 ERROR:", e)
        raise HTTPException(status_code=500, detail=f"Error al conectar con Ollama: {str(e)}")
