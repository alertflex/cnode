/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.common;

import com.opendxl.client.DxlClientConfig;
import com.opendxl.client.exception.DxlException;

/**
 *
 * @author root
 */
public class DxlCommon {

    private DxlCommon() {
        super();
    }

    public static DxlClientConfig getClientConfig(final String conf) throws DxlException {
        return DxlClientConfig.createDxlConfigFromFile(conf);
    }

}
