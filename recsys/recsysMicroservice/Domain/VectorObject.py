from pydantic import BaseModel
from typing import List
from abc import ABC, abstractmethod


class VectorObject(BaseModel, ABC):
    id: int
    vector: List[float]

    def __init__(self, id, vector: List[float] = None):
        super().__init__()
        self.id = id
        if not vector:
            self.vector = vector

    @property
    @abstractmethod
    def collection_name(self) -> str:
        """Property that must be implemented by subclasses to return the collection name."""
        return NotImplemented
