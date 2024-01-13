package org.webCrawler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webCrawler.repository.MongoRepository;

@Service
public class GenericService<T> {
    @Autowired
    MongoRepository<T> repository;

    public T add(T user) {
        return repository.save(user);
    }

    public T getById(String id, Class<T> aClass) {
        return repository.get(id, aClass);
    }

    public T delete(String id, Class<T> aClass) {
        return repository.remove(id, aClass);
    }
}
