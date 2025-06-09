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
    prompt = (
        f'Dado: "{data.text}".\n'
        "Recomienda 3 libros similares.\n"
        "Devuelve solo un array JSON con objetos tipo: "
        '[{"title": "string", "author": "string", "year": 2000}].'
    )


    try:
        response = requests.post(
            f"{OLLAMA_HOST}/api/chat",
            json={
                "model": "gemma:2b",
                "messages": [
                    {"role": "user", "content": prompt}
                ],
                "stream": False,
                "temperature": 0.3,
                "top_p": 0.8
            },
            timeout=180
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
