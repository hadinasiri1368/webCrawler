package org.webCrawler.repository;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import org.webCrawler.common.CommonUtils;

import java.util.List;
import java.util.Map;

@Repository
public class JPA<ENTITY, ID> {
    @PersistenceContext()
    private EntityManager entityManager;

    @Transactional
    public void save(ENTITY entity) throws Exception {
        CommonUtils.setNull(entity);
        entityManager.persist(entity);
    }

    @Transactional
    public void update(ENTITY entity) throws Exception {
        CommonUtils.setNull(entity);
        entityManager.merge(entity);
    }

    @Transactional
    public void remove(ENTITY entity) {
        entityManager.remove(entityManager.merge(entity));
    }

    public ENTITY findOne(Class<ENTITY> aClass, ID id) {
        return entityManager.find(aClass, id);
    }

    public List listByQuery(Query query, Map<String, Object> param) {
        if (!CommonUtils.isNull(param) && param.size() > 0)
            for (String key : param.keySet()) {
                query.setParameter(key, param.get(key));
            }
        return query.getResultList();
    }

    public int executeUpdate(Query query, Map<String, Object> param) {
        if (!CommonUtils.isNull(param) && param.size() > 0)
            for (String key : param.keySet()) {
                query.setParameter(key, param.get(key));
            }
        return query.executeUpdate();
    }

    public List listByQuery(Query query) {
        return listByQuery(query, null);
    }

    public List<ENTITY> findAll(Class<ENTITY> aClass) {
        Entity entity = aClass.getAnnotation(Entity.class);
        Query query = entityManager.createQuery("select entity from " + entity.name() + " entity");
        return query.getResultList();
    }

    public List<ENTITY> findAll(Class<ENTITY> aClass, Map<String, Object> param) {
        Entity entity = aClass.getAnnotation(Entity.class);
        String strQuery = CommonUtils.getStringQuery("select entity from " + entity.name() + " entity", param);
        Query query = entityManager.createQuery(strQuery);
        for (String key : param.keySet()) {
            query.setParameter(key, param.get(key));
        }
        return query.getResultList();
    }
}