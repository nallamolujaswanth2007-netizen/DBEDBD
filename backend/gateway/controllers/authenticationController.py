import os

import httpx
from dotenv import load_dotenv
from fastapi import APIRouter, Header, HTTPException
from fastapi.responses import JSONResponse

from models.schemas import SigninSchema, SignupSchema

load_dotenv()

SPRING_URL = os.getenv("SPRING_URL", "http://localhost:8001")

router = APIRouter(prefix="/authservice", tags=["Authentication Gateway"])


async def forward_request(method: str, path: str, **kwargs):
    try:
        async with httpx.AsyncClient(timeout=20.0) as client:
            response = await client.request(method, f"{SPRING_URL}{path}", **kwargs)
    except httpx.RequestError as exc:
        raise HTTPException(
            status_code=503,
            detail=f"Spring Boot core service is not reachable at {SPRING_URL}: {exc}",
        ) from exc

    try:
        content = response.json()
    except ValueError:
        content = {"message": response.text}

    return JSONResponse(status_code=response.status_code, content=content)


@router.post("/signin")
async def signin(data: SigninSchema):
    return await forward_request("POST", "/users/signin", json=data.model_dump())


@router.post("/signup")
async def signup(data: SignupSchema):
    return await forward_request("POST", "/users/signup", json=data.model_dump())


@router.get("/profile")
async def profile(Token: str = Header(...)):
    return await forward_request("GET", "/users/profile", headers={"Token": Token})
