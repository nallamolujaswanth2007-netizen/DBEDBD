import os

import httpx
from dotenv import load_dotenv
from fastapi import APIRouter, HTTPException, Query
from fastapi.responses import JSONResponse, Response

from models.schemas import (
    ActivityLogOut,
    InventoryAdjust,
    InventoryItemSchema,
    InventoryItemOut,
    InventoryItemUpdate,
    SummaryOut,
)

load_dotenv()

SPRING_URL = os.getenv("SPRING_URL", "http://localhost:8001")

router = APIRouter(prefix="/inventoryservice", tags=["Inventory Gateway"])


async def forward_request(method: str, path: str, **kwargs):
    try:
        async with httpx.AsyncClient(timeout=20.0) as client:
            response = await client.request(method, f"{SPRING_URL}{path}", **kwargs)
    except httpx.RequestError as exc:
        raise HTTPException(
            status_code=503,
            detail=f"Spring Boot core service is not reachable at {SPRING_URL}: {exc}",
        ) from exc

    if response.status_code == 204:
        return Response(status_code=204)

    try:
        content = response.json()
    except ValueError:
        content = {"message": response.text}

    return JSONResponse(status_code=response.status_code, content=content)


@router.get("/items", response_model=list[InventoryItemOut])
async def get_items(
    search: str | None = Query(default=None),
    location: str | None = Query(default=None),
    semantic_query: str | None = Query(default=None),
):
    params = {
        key: value
        for key, value in {
            "search": search,
            "location": location,
            "semantic_query": semantic_query,
        }.items()
        if value is not None
    }
    return await forward_request("GET", "/inventory/items", params=params)


@router.get("/items/{item_id}", response_model=InventoryItemOut)
async def get_item(item_id: int):
    return await forward_request("GET", f"/inventory/items/{item_id}")


@router.post("/items", response_model=InventoryItemOut)
async def create_item(data: InventoryItemSchema):
    return await forward_request("POST", "/inventory/items", json=data.model_dump())


@router.put("/items/{item_id}", response_model=InventoryItemOut)
async def update_item(item_id: int, data: InventoryItemUpdate):
    return await forward_request(
        "PUT",
        f"/inventory/items/{item_id}",
        json=data.model_dump(exclude_unset=True),
    )


@router.patch("/items/{item_id}/adjust", response_model=InventoryItemOut)
async def adjust_item(item_id: int, data: InventoryAdjust):
    return await forward_request(
        "PATCH",
        f"/inventory/items/{item_id}/adjust",
        json=data.model_dump(),
    )


@router.delete("/items/{item_id}", status_code=204)
async def delete_item(item_id: int):
    return await forward_request("DELETE", f"/inventory/items/{item_id}")


@router.get("/locations", response_model=list[str])
async def get_locations():
    return await forward_request("GET", "/inventory/locations")


@router.get("/activity", response_model=list[ActivityLogOut])
async def get_activity():
    return await forward_request("GET", "/inventory/activity")


@router.get("/queue", response_model=list[InventoryItemOut])
async def get_replenishment_queue():
    return await forward_request("GET", "/inventory/queue")


@router.get("/summary", response_model=SummaryOut)
async def get_summary():
    return await forward_request("GET", "/inventory/summary")


@router.get("/semantic-queries", response_model=list[str])
async def get_semantic_queries():
    return await forward_request("GET", "/inventory/semantic-queries")
