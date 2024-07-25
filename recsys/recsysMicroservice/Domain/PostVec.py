from VectorObject import VectorObject


class PostVec(VectorObject):
    
    @property
    def collection_name(self) -> str:
        return 'post'
