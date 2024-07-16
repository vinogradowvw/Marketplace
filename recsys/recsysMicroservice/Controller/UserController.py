from fastapi import APIRouter
from Service.UserService import UserService

from injector import inject


router = APIRouter(prefix="/user")


@inject
@router.post("/like/user/{user_id}/post/{post_id}")
async def user_liked(user_id: int, post_id: int, user_service: UserService):
    user_service.update_users_vector(user_id=user_id, post_id=post_id, weight=30)


@inject
@router.post("/like/user/{user_id}/post/{post_id}")
async def user_purchased(user_id: int, post_id: int, user_service: UserService):
    user_service.update_users_vector(user_id=user_id, post_id=post_id, weight=70)
