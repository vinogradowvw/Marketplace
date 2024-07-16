from io import BytesIO
from typing import List
import requests
from PIL import Image

from injector import inject
from Domain.PostDTO import PostDTO
from Domain.PostVec import PostVec
from Repository.PostVecRepository import PostVecRepository
from Repository.UserVecRepository import UserVecRepository
import numpy as np

from Service.Vectorizers.ImageVectorizer import ImageVectorizer
from Service.Vectorizers.TextVectorizer import TextVectorizer


class PostService:

    @inject
    def __init__(self, post_vec_repo: PostVecRepository, user_vec_repo: UserVecRepository,
                 text_vectorizer: TextVectorizer, image_vectorizer: ImageVectorizer):
        self.__post_vec_repo = post_vec_repo
        self.__user_vec_repo = user_vec_repo
        self.__text_vectorizer = text_vectorizer
        self.__image_vectorizer = image_vectorizer

    def get_recommended_posts_by_user_id(self, user_id: int, n: int) -> List[int]:
        user = self.__user_vec_repo.find_by_id(user_id)
        posts = self.__post_vec_repo.find_similar(user.vector, n)
        post_ids = [post.id for post in posts]
        return post_ids

    def get_recommended_posts_by_post_id(self, post_id: int, n: int) -> List[int]:
        post = self.__user_vec_repo.find_by_id(post_id)
        posts = self.__post_vec_repo.find_similar(post.vector, n)
        post_ids = [post.id for post in posts]
        return post_ids

    def upsert(self, post: PostDTO) -> None:

        tag_vector = self.__text_vectorizer.bow_vectorize(post.tags)

        description_vector_tfidf = self.__text_vectorizer.tfidf_vectorize(post.description)
        description_vector_bert = self.__text_vectorizer.bert_vectorize(post.description)

        image_vectors = []
        for image in post.images:
            response = requests.get(str(image))

            with Image.open(BytesIO(response.content)) as im:
                image_vectors.append(self.__image_vectorizer.resnet_vectorize(im))

        image_vector = np.mean(image_vectors, axis=0)

        concatenated = np.concatenate((description_vector_tfidf, description_vector_bert, tag_vector, image_vector))

        post_vec = PostVec(id=post.id,
                           vector=concatenated.tolist())

        self.__post_vec_repo.upsert(post_vec)
