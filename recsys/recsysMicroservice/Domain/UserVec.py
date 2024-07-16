from VectorObject import VectorObject


class UserVec(VectorObject):

    weight_count: int

    @property
    def collection_name(self) -> str:
        return 'user'
