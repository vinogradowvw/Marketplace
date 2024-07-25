from fastapi import APIRouter
from Domain.PostDTO import PostDTO
from Service.PostService import PostService
from injector import inject
router = APIRouter(prefix="/post")


@inject
@router.get("/reccomendations/post/{post_id}")
async def get_reccomendations_by_post(post_id: int, limit: int, post_service: PostService):
    post_ids = post_service.get_recommended_posts_by_post_id(post_id, limit)
    return {"post_ids": post_ids}


@inject
@router.get("/reccomendations/user/{user_id}")
async def get_reccomendations_by_user(user_id: int, limit: int, post_service: PostService):
    post_ids = post_service.get_recommended_posts_by_user_id(user_id, limit)
    return {"post_ids": post_ids}


@inject
@router.post("/save")
async def save_post(post: PostDTO, post_service: PostService):
    post_service.upsert(post)
