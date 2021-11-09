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

import java.sql.Timestamp;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.alertflex.entity.ScanreportTask;

@Stateless
public class ScanreportTaskFacade extends AbstractFacade<ScanreportTask> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ScanreportTaskFacade() {
        super(ScanreportTask.class);
    }

    public List<ScanreportTask> getOldReports(String ref, Timestamp time) {

        List<ScanreportTask> listReports = null;

        try {
            em.flush();

            Query listQry = em.createQuery("SELECT r FROM ScanreportTask r WHERE r.refId = :ref AND r.reportAdded < :time")
                .setParameter("ref", ref)
                .setParameter("time", time);

            // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            listReports = listQry.getResultList();

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Authenticated", ""));
        } catch (Exception e) {

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid query to DB", ""));
        }

        return listReports;
    }

    public List<ScanreportTask> findReportsByUuid(String uuid) {

        List<ScanreportTask> listReports = null;

        try {
            em.flush();

            Query listQry = em.createQuery("SELECT r FROM ScanreportTask r WHERE r.reportUuid = :uuid").setParameter("uuid", uuid);

            // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            listReports = listQry.getResultList();

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Authenticated", ""));
        } catch (Exception e) {
            return null;
        }

        return listReports;
    }
}
