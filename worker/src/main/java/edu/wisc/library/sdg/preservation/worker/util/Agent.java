package edu.wisc.library.sdg.preservation.worker.util;

import org.apache.commons.lang3.StringUtils;

public final class Agent {

    public static final String PRESERVATION_SERVICE_VERSION = String.format("preservation-service-%s",
            StringUtils.defaultString(Agent.class.getPackage().getImplementationVersion(), "SNAPSHOT"));

    private Agent() {
    }

}
