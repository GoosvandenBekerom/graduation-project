//*************************************************//
//          INTHER LOGISTICS ENGINEERING           //
//*************************************************//

package com.inthergroup.webpoc.framework.services;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * @author gvandenbekerom
 * @since 18-Sep-18
 */
public class EntityService<T, TID, TREPO extends CrudRepository<T, TID>> {
    protected final TREPO repo;

    EntityService(TREPO repo) {
        this.repo = repo;
    }

    public <S extends T> S save(S instance) {
        return repo.save(instance);
    }

    public <S extends T> Iterable<S> saveAll(Iterable<S> collection) {
        return repo.saveAll(collection);
    }

    public Optional<T> findById(TID id) {
        return repo.findById(id);
    }

    public boolean existsById(TID id) {
        return repo.existsById(id);
    }

    public Iterable<T> findAll() {
        return repo.findAll();
    }

    public long count() {
        return repo.count();
    }

    public void deleteById(TID id) {
        repo.deleteById(id);
    }

    public void delete(T instance) {
        repo.delete(instance);
    }

    public void deleteAll(Iterable<? extends T> collection) {
        repo.deleteAll(collection);
    }

    public void deleteAll() {
        repo.deleteAll();
    }
}