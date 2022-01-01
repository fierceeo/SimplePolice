package com.voidcitymc.plugins.SimplePolice.api;


public interface SimplePoliceAPI {

    /**
     * Registers a simple police event
     *
     * @param event The event to register
     */

    void registerEvent(SimplePoliceEvent event);

    /**
     * Returns utility class holding internal methods
     *
     * @return Utility the utility class
     */

    Utility getUtilityInstance();

}
