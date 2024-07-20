from pydantic import BaseModel
from typing import List, Optional
from abc import ABC, abstractmethod


class VectorObject(BaseModel, ABC):
    id: int
    vector: Optional[List[float]]

    def __init__(self, id, vector: Optional[List[float]] = None):
        super().__init__()
        self.id = id
        if not vector:
            self.vector = vector

    @staticmethod
    @abstractmethod
    def collection_name() -> str:
        """Property that must be implemented by subclasses to return the collection name."""
        return NotImplemented
