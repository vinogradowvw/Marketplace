from abc import ABC
from pymilvus import MilvusClient
from Domain.VectorObject import VectorObject
from typing import List, Generic, TypeVar, Type

Entity = TypeVar("Entity", bound=VectorObject)


class VecRepository(ABC, Generic[Entity]):

    def __init__(self, entity_class: Type[Entity]):
        self._milvus_client = MilvusClient("http://localhost:19530")
        self.entity_class = entity_class

    def upsert(self, vector_object: Entity) -> None:
        collection_name = self.entity_class.collection_name()
        self._milvus_client.upsert(collection_name=collection_name, data=vector_object.model_dump())

    def find_by_id(self, id: int) -> Entity:
        collection_name = self.entity_class.collection_name()
        item = self._milvus_client.get(collection_name=collection_name, ids=[id])
        return self.entity_class(id=item[0]["id"], vector=item[0]["vector"])

    def find_similar(self, vector: List[float], n: int) -> List[Entity]:
        collection_name = self.entity_class.collection_name()
        response = self._milvus_client.search(
            collection_name=collection_name,
            limit=n,
            data=vector,
            search_params={"metric_type": "COSINE", "params": {}}
        )
        similar_objects = []

        for item in response[0]:
            vector_object = self.entity_class(id=item["id"], vector=item["vector"])
            similar_objects.append(vector_object)

        return similar_objects
