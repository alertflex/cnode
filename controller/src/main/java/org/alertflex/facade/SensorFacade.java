/*
 *   Copyright 2021 Oleg Zharkov
 *
 *   Licensed under the Apache License, Version 2.0 (the "License").
 *   You may not use this file except in compliance with the License.
 *   A copy of the License is located at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   or in the "license" file accompanying this file. This file is distributed
 *   on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 *   express or implied. See the License for the specific language governing
 *   permissions and limitations under the License.
 */
 
package org.alertflex.facade;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.alertflex.entity.Sensor;

@Stateless
public class SensorFacade extends AbstractFacade<Sensor> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SensorFacade() {
        super(Sensor.class);
    }

    public Sensor findSensorByName(String ref, String node, String name) {

        Sensor s = null;

        try {
            em.flush();

            Query listQry = em.createQuery(
                    "SELECT s FROM Sensor s WHERE s.sensorPK.node = :node AND s.sensorPK.refId = :ref AND s.sensorPK.name = :name")
                    .setParameter("ref", ref).setParameter("node", node).setParameter("name", name);

            // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            s = (Sensor) listQry.getSingleResult();

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Authenticated", ""));
        } catch (Exception e) {

        }

        return s;

    }
    
    public Sensor findSensorByProbe(String ref, String node, String probe) {

        Sensor s = null;

        try {
            em.flush();

            Query listQry = em.createQuery(
                    "SELECT s FROM Sensor s WHERE s.sensorPK.node = :node AND s.sensorPK.refId = :ref AND s.probe = :probe")
                    .setParameter("ref", ref).setParameter("node", node).setParameter("probe", probe);

            // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            s = (Sensor) listQry.getSingleResult();

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Authenticated", ""));
        } catch (Exception e) {

        }

        return s;

    }

    public List<Sensor> findSensorsByRef(String ref) {

        List<Sensor> l = null;

        try {
            em.flush();

            Query listQry = em.createQuery(
                    "SELECT s FROM Sensor s WHERE s.sensorPK.refId = :ref").setParameter("ref", ref);

            // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            l = (List<Sensor>) listQry.getResultList();

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Authenticated", ""));
        } catch (Exception e) {

        }

        return l;

    }

    public List<Sensor> findSensorsByType(String ref, String node, String type) {

        List<Sensor> l = null;

        try {
            em.flush();

            Query listQry = em.createQuery(
                    "SELECT s FROM Sensor s WHERE s.sensorPK.node = :node AND s.sensorPK.refId = :ref AND s.type = :type")
                    .setParameter("ref", ref).setParameter("node", node).setParameter("type", type);

            // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            l = (List<Sensor>) listQry.getResultList();

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Authenticated", ""));
        } catch (Exception e) {
            return null;
        }

        return l;

    }
    
    public List<String> findProbeNamesByNode(String ref, String node) {

        List<String> lp = null;

        try {
            em.flush();

            Query listQry = em.createQuery(
                "SELECT s.probe FROM Sensor s WHERE s.sensorPK.node = :node AND s.sensorPK.refId = :ref GROUP BY s.sensorPK.node")
                    .setParameter("ref", ref).setParameter("node", node);

            // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            lp = listQry.getResultList();

        } catch (Exception e) {

        }

        return lp;
    }

}
