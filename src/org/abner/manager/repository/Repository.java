package org.abner.manager.repository;

import java.util.List;

import org.abner.manager.model.Model;

public interface Repository<M extends Model> {

    List<M> find();

    Iterable<M> iterate();

    M find(final Long id);

    void remove(final M entity);

    long insert(final M entity);

    long update(final M entity);

}
