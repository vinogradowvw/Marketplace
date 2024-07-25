from abc import ABC
from pymilvus import MilvusClient
from Domain.VectorObject import VectorObject
from typing import List, Generic, TypeVar, Type

Entity = TypeVar("Entity", bound=VectorObject)


class VecRepository(ABC, Generic[Entity]):

    def __init__(self, entity_type: Type[Entity]):
        self.__entity_type = entity_type
        self._milvus_client = MilvusClient("http://localhost:19530")

    def upsert(self, vector_object: Entity) -> None:
        self._milvus_client.upsert(collection_name=vector_object.collection_name,
                                            data=vector_object.model_dump())

    def find_by_id(self, id: int) -> Entity:
        item = self._milvus_client.get(collection_name=self.__entity_type.collection_name, ids=id)

        return Type[Entity](id=item[0]["id"], vector=item[0]["vector"])

    def find_similar(self, vector: List[float], n: int) -> List[Entity]:
        response = self._milvus_client.search(collection_name=Entity.collection_name(),
                                              limit=n,
                                              data=vector,
                                              search_params={"metric_type": "COSINE", "params": {}})

        similar_objects = []

        for item in response[0]:
            vector_object = Type[Entity](id=item["id"], vector=item["vector"])
            similar_objects.append(vector_object)

        return similar_objects

