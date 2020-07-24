/*
 * EnrollmentStation.java
 * Copyright (c) 2008, Source Trace Systems
 * ALL RIGHTS RESERVED
 */
package com.sourcetrace.esesw.entity.profile;

import com.sourcetrace.eses.entity.Device;

/**
 * The Class EnrollmentStation.
 * @author $Author: aravind $
 * @version $Rev: 59 $, $Date: 2009-08-04 17:10:51 +0530 (Tue, 04 Aug 2009) $
 */
public class EnrollmentStation extends Device {


	/* (non-Javadoc)
     * @see com.ese.entity.profile.Device#toString()
     */
    public String toString() {
        return getName()+"-"+getDeviceId();
    }

}
