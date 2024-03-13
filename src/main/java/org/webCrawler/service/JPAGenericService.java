package org.webCrawler.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webCrawler.common.CommonUtils;
import org.webCrawler.repository.JPA;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class JPAGenericService<Entity> {
    @Autowired
    private JPA<Entity, Long> genericJPA;

    @Autowired
    private EntityManager entityManager;

    @Transactional
    public void insert(Entity entity) throws Exception {
        Method m = entity.getClass().getMethod("setId", Long.class);
        m.invoke(entity, (Long) null);
        CommonUtils.setNull(entity);
        genericJPA.save(entity);
    }

    @Transactional
    public void update(Entity entity, Class<Entity> aClass) throws Exception {
        Method m = entity.getClass().getMethod("getId");
        Long id = (Long) m.invoke(entity);
        if (CommonUtils.isNull(id))
            throw new RuntimeException("id.not.found");
        if (CommonUtils.isNull(findOne(aClass, id)))
            throw new RuntimeException("id.not.found");
        CommonUtils.setNull(entity);
        genericJPA.update(entity);
    }

    @Transactional
    public void delete(Entity entity) {
        genericJPA.remove(entity);
    }

    @Transactional
    public int delete(Long id, Class<Entity> aClass) {
        jakarta.persistence.Entity entity = aClass.getAnnotation(jakarta.persistence.Entity.class);
        int returnValue = entityManager.createQuery("delete  " + entity.name() + " o where o.id=:id").setParameter("id", id).executeUpdate();
        if (returnValue == 0) {
            throw new RuntimeException("id.not.found");
        }
        return returnValue;
    }

    public Entity findOne(Class<Entity> aClass, Long id) {
        return genericJPA.findOne(aClass, id);
    }

    public List<Entity> findAll(Class<Entity> aClass) {
        return genericJPA.findAll(aClass);
    }
    public List listByQuery(Query query, Map<String, Object> param) {
        if (!CommonUtils.isNull(param) && param.size() > 0)
            for (String key : param.keySet()) {
                query.setParameter(key, param.get(key));
            }
        return query.getResultList();
    }
    public void persist(Object o) {
        entityManager.persist(o);
    }

}

