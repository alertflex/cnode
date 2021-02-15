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

package org.alertflex.controller;

import org.alertflex.entity.Agent;
import org.json.JSONException;
import org.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AgentsManagement {

    private static final Logger logger = LoggerFactory.getLogger(AgentsManagement.class);

    private InfoMessageBean eventBean;
    String ref;
    String node;

    public AgentsManagement(InfoMessageBean eb) {
        this.eventBean = eb;
        this.ref = eb.getRefId();
        this.node = eb.getNode();
    }

    public void saveAgentKey(String agent, String json) {

        try {

            JSONObject obj = new JSONObject(json);

            int error = obj.getInt("error");

            if (error != 0) {
                return;
            }

            JSONObject data = obj.getJSONObject("data");

            String id = data.getString("id");
            String key = data.getString("key");

            Agent a = eventBean.getAgentFacade().findAgentByName(ref, node, agent);

            if (a != null) {
                a.setAgentId(id);
                a.setAgentKey(key);
                eventBean.getAgentFacade().edit(a);
            }

        } catch (JSONException e) {
            logger.error(json);
            logger.error("alertflex_ctrl_exception", e);
        }

    }

}
