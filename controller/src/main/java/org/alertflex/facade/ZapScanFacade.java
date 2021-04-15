/*
 * Alertflex Management Console
 *
 * Copyright (C) 2021 Oleg Zharkov
 *
 */

package org.alertflex.facade;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.SqlResultSetMapping;
import org.alertflex.entity.ZapScan;
import org.alertflex.reports.Finding;

@Stateless
public class ZapScanFacade extends AbstractFacade<ZapScan> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ZapScanFacade() {
        super(ZapScan.class);
    }

    public ZapScan findRecord(String ref, String url, int plugin, int cwe, int source, int wasc) {

        ZapScan zs;

        try {
            em.flush();

            Query vQry = em.createQuery(
                    "SELECT z FROM ZapScan z WHERE z.refId = :ref AND z.url = :url AND z.pluginid = :p AND z.cweid = :c AND z.sourceid = :s AND z.wascid = :w")
                    .setParameter("ref", ref)
                    .setParameter("url", url)
                    .setParameter("p", plugin)
                    .setParameter("c", cwe)
                    .setParameter("s", source)
                    .setParameter("w", wasc);
            vQry.setMaxResults(1);
            // Enable forced database query
            vQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            zs = (ZapScan) vQry.getSingleResult();

        } catch (Exception e) {
            zs = null;
        }

        return zs;

    }
    
    public ZapScan findScan(String ref, String node, String probe, String target, String alert) {

        ZapScan zs = null;

        try {
            em.flush();

            Query qry = em.createQuery(
                    "SELECT z FROM ZapScan z WHERE z.refId = :ref AND z.nodeId = :node AND z.probe = :probe AND z.target = :target AND z.alertName = :alert")
                    .setParameter("ref", ref)
                    .setParameter("node", node)
                    .setParameter("probe", probe)
                    .setParameter("target", target)
                    .setParameter("alert", alert);
            // Enable forced database query
            qry.setMaxResults(1);
            
            qry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            zs = (ZapScan) qry.getSingleResult();

        } catch (Exception e) {
            zs = null;
        }

        return zs;
    }
    
    public List<Object[]> getFindings(String ref) {

        List<Object[]> f = null;

        try {
            em.flush();
            
            Query qry = em.createQuery(
                    "SELECT z.riskdesc, COUNT(z) FROM ZapScan z WHERE z.refId = :ref GROUP BY z.riskdesc")
                    .setParameter("ref", ref);
            // Enable forced database query
            qry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            
            f =  (List<Object[]>) qry.getResultList();

        } catch (Exception e) {
            f = null;
        }

        return f;
    }

}
