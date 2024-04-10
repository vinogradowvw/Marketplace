package com.marketplace.demo.service;

import com.marketplace.demo.domain.EntityWithId;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public abstract class CrudServiceImpl <T extends EntityWithId<ID>, ID> implements CrudService<T, ID>{

    @Override
    public T create(T e) throws IllegalArgumentException {
        Optional<ID> id = Optional.ofNullable(e.getID());
        if (id.isPresent()){
            if (getRepository().existsById(id.get())){
                throw new IllegalArgumentException("Entity with id " + id.get() + " already exists");
            }
        }

        return getRepository().save(e);
    }

    @Override
    public Optional<T> readById(ID id) {
        return getRepository().findById(id);
    }

    @Override
    public Iterable<T> readAll() {
        return getRepository().findAll();
    }

    @Override
    public void update(ID id, T e) throws IllegalArgumentException {
        if (!getRepository().existsById(id)){
            throw new IllegalArgumentException("Entity with id " + id + " does not exist");
        }

        getRepository().save(e);
    }

    @Override
    public void deleteById(ID id) throws IllegalArgumentException {
        if (!getRepository().existsById(id)){
            throw new IllegalArgumentException("Entity with id " + id + " does not exist");
        }

        getRepository().deleteById(id);
    }

    protected abstract CrudRepository<T, ID> getRepository();
}
