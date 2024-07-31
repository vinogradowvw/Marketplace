from fastapi import FastAPI
from .Controller import PostController, UserController
from injector import Injector

injector = Injector()

app = FastAPI()
app.include_router(PostController.router)
app.include_router(UserController.router)
