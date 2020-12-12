/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.facade;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.alertflex.entity.Attributes;
import org.eclipse.persistence.queries.ScrollableCursor;

/**
 *
 * @author root
 */
@Stateless
public class AttributesFacade extends AbstractFacade<Attributes> {

    @PersistenceContext(unitName = "misp_PU")
    private EntityManager em;

    Query attributesListQry;
    ScrollableCursor scrollableCursor;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AttributesFacade() {
        super(Attributes.class);
    }

    public List<Attributes> findAttributesByType(String t) {

        List<Attributes> a = null;

        try {
            em.flush();

            Query attributesListQry = em.createQuery(
                    "SELECT a FROM Attributes a WHERE a.type = :type").setParameter("type", t);

            // Enable forced database query
            attributesListQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            a = attributesListQry.getResultList();

        } catch (Exception e) {
            a = null;
        }

        return a;

    }

    public List<String> findCategories() {

        List<String> a = null;

        try {
            em.flush();

            Query categoriesListQry = em.createQuery(
                    "SELECT a.category FROM Attributes a WHERE a.type = :type GROUP BY a.category").setParameter("type", "ip-src");

            // Enable forced database query
            categoriesListQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            a = (List<String>) categoriesListQry.getResultList();

        } catch (Exception e) {
            a = null;
        }

        return a;

    }

    public void initAttributesByType(String t) {

        try {
            em.flush();

            attributesListQry = em.createQuery(
                    "SELECT a FROM Attributes a WHERE a.type = :type").setParameter("type", t);

            // Enable forced database query
            attributesListQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            attributesListQry.setHint("eclipselink.cursor.scrollable", true);
            scrollableCursor = (ScrollableCursor) attributesListQry.getSingleResult();

        } catch (Exception e) {

        }

    }

    public List<Object> scrollAttributesByType(int interval) {

        List<Object> a = null;

        try {
            a = scrollableCursor.next(interval);

        } catch (Exception e) {
            a = null;
        }

        return a;

    }

    public Attributes findByValueAndType(String value, String type) {

        Attributes attr = null;

        try {
            em.flush();

            Query attrQry = em.createQuery("SELECT a FROM Attributes a WHERE a.value1= :value AND a.type = :type").setParameter("value", value).setParameter("type", type);

            // Enable forced database query
            attrQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            attr = (Attributes) attrQry.getSingleResult();

        } catch (Exception e) {

        }

        return attr;
    }

    public Long countIpSrc() {

        Long result = 0L;

        try {
            em.flush();

            Query attributesQry = em.createQuery(
                    "SELECT count(a.value1) FROM Attributes a WHERE a.type = :type").setParameter("type", "ip-src");

            // Enable forced database query
            attributesQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            result = (Long) attributesQry.getSingleResult();

        } catch (Exception e) {
            return result;
        }

        return result;
    }
}
