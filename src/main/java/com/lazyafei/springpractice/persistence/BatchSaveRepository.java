package com.lazyafei.springpractice.persistence;

import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface BatchSaveRepository<T> {
    <S extends T> List<S> batchSave(Iterable<S> entities,Boolean isSave);

}
