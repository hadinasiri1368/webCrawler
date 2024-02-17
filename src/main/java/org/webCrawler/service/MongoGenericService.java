package org.webCrawler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.webCrawler.repository.MongoRepository;

import java.util.List;

@Service
public class MongoGenericService<T> {
    @Autowired
    MongoRepository<T> repository;

    public T add(T user) {
        return repository.save(user);
    }

    public T getById(String id, Class<T> aClass) {
        return repository.get(id, aClass);
    }

    public List<T> findAll(Class<T> aClass) {
        return repository.list(aClass);
    }

    public List<T> list(Class<T> clazz, Query query) {
        return repository.list(clazz, query);
    }

    public List<T> list(Class<T> clazz, String key, String value) {
        Query query = new Query(new Criteria(key).is(value));
        return repository.list(clazz, key, value);
    }

    public T delete(String id, Class<T> aClass) {
        return repository.remove(id, aClass);
    }
}
