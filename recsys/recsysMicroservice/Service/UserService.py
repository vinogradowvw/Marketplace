from typing import List

import numpy as np

from injector import inject

from Domain.UserVec import UserVec
from Repository.PostVecRepository import PostVecRepository
from Repository.UserVecRepository import UserVecRepository


class UserService:

    @inject
    def __init__(self, user_vec_repo: UserVecRepository, post_vec_repo: PostVecRepository):
        self.__user_vec_repo = user_vec_repo
        self.__post_vec_repo = post_vec_repo

    def get_recommended_users_by_user_id(self, user_id: int, n: int) -> List[int]:
        user = self.__user_vec_repo.find_by_id(user_id)
        users = self.__user_vec_repo.find_similar(user.vector, n)
        user_ids = [user.id for user in users]
        return user_ids

    def update_users_vector(self, user_id: int, post_id: int, weight: int):

        post_vector = np.array(self.__post_vec_repo.find_by_id(post_id).vector)

        old_user = self.__user_vec_repo.find_by_id(user_id)
        old_user_vector = np.array(old_user.vector)

        new_user_vector = (old_user_vector * old_user.weight_count + weight * post_vector) / \
                          (old_user.weight_count + weight)

        new_user = UserVec(id=user_id, vector=new_user_vector.tolist(), weight_count=old_user.weight_count + weight)

        self.__user_vec_repo.upsert(new_user)
