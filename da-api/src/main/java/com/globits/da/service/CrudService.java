package com.globits.da.service;

import java.util.List;

public interface CrudService<T> {


    T create(T entity);
    T update(Long id, T entity);
    String delete(Long id);
    List<T> getAll();
    T getById(Integer id);

    List<T> getByName(String name);




}
