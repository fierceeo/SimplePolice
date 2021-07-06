package com.voidcitymc.plugins.SimplePolice.api;

import com.voidcitymc.plugins.SimplePolice.api.events.GenericSimplePoliceEvent;


public interface SimplePoliceAPI {

    /**
     * Registers a simple police event
     *
     * @param event The event to register
     */

    void registerEvent(GenericSimplePoliceEvent event);

    /**
     * Returns utility class holding internal methods
     *
     * @return Utility the utility class
     */

    Utility getUtilityInstance();

}
