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
import org.alertflex.entity.InspectorScan;
import org.alertflex.reports.Finding;


@Stateless
public class InspectorScanFacade extends AbstractFacade<InspectorScan> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public InspectorScanFacade() {
        super(InspectorScan.class);
    }

    public List<InspectorScan> findRecords(String ref, String ec2) {

        List<InspectorScan> lis = null;

        try {
            em.flush();

            Query qry = em.createQuery(
                    "SELECT i FROM InspectorScan i WHERE i.refId = :ref AND i.ec2Name = :ec2")
                    .setParameter("ref", ref)
                    .setParameter("ec2", ec2);
            // Enable forced database query
            qry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            lis = (List<InspectorScan>) qry.getResultList();

        } catch (Exception e) {
            lis = null;
        }

        return lis;
    }
    
    public InspectorScan findScan(String ref, String template, String title, String ec2) {

        InspectorScan is = null;

        try {
            em.flush();

            Query qry = em.createQuery(
                    "SELECT i FROM InspectorScan i WHERE i.refId = :ref AND i.arn = :template AND i.title = :title AND i.ec2Name = :ec2")
                    .setParameter("ref", ref)
                    .setParameter("template", template)
                    .setParameter("title", title)
                    .setParameter("ec2", ec2);
            
            // Enable forced database query
            qry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            is = (InspectorScan) qry.getSingleResult();

        } catch (Exception e) {
            is = null;
        }

        return is;
    }

    
    public List<String> findEc2(String ref) {

        List<String> lec2;

        try {
            em.flush();

            Query qry = em.createQuery(
                    "SELECT i.ec2Name FROM InspectorScan i WHERE i.refId = :ref GROUP BY i.ec2Name")
                    .setParameter("ref", ref);

            // Enable forced database query
            qry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            lec2 = (List<String>) qry.getResultList();

        } catch (Exception e) {
            lec2 = null;
        }

        return lec2;
    }
    
    public List<Object[]> getFindings(String ref) {

        List<Object[]> f = null;

        try {
            em.flush();

            Query qry = em.createQuery(
                    "SELECT i.severity, COUNT(i) FROM InspectorScan i WHERE i.refId = :ref GROUP BY i.severity", Finding.class)
                    .setParameter("ref", ref);
            // Enable forced database query
            qry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            
            f = (List<Object[]>) qry.getResultList();

        } catch (Exception e) {
            f = null;
        }

        return f;
    }
}
