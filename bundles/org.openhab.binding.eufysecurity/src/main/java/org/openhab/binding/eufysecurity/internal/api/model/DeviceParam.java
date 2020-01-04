/**
 * /**
 *  * Copyright (c) 2010-2019 Contributors to the openHAB project
 *  *
 *  * See the NOTICE file(s) distributed with this work for additional
 *  * information.
 *  *
 *  * This program and the accompanying materials are made available under the
 *  * terms of the Eclipse Public License 2.0 which is available at
 *  * http://www.eclipse.org/legal/epl-2.0
 *  *
 *  * SPDX-License-Identifier: EPL-2.0
 *  */
 */
package org.openhab.binding.eufysecurity.internal.api.model;

import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * Device Param Object. Field names obtained from REST Service JSON structure
 *
 * @author Sriram Balakrishnan - Initial contribution
 *
 */
public class DeviceParam {
    public static final int BIT_ALARM = 2;
    public static final int BIT_DEPLOY_AWAY = 0;
    public static final int BIT_DEPLOY_CUSTOME1 = 3;
    public static final int BIT_DEPLOY_CUSTOME2 = 4;
    public static final int BIT_DEPLOY_CUSTOME3 = 5;
    public static final int BIT_DEPLOY_HOME = 1;
    public static final int BIT_DEPLOY_SCHEDULE = 2;
    public static final int BIT_NOTIFICATION = 8;
    public static final int BIT_RECORD = 1;
    public static final int BIT_SENSOR_NOTIFICATION = 8;
    public static final int BIT_SENSOR_STATION_ALARM = 4;
    public static final int BIT_STATION_ALARM = 4;

    public static final int TYPE_INDOOR_OUTDOOR = 99901;
    public static final int TYPE_PRIVATE_MODE = 99904;
    public int create_time;
    public String device_sn;
    public int param_id;
    public int param_type;
    public String param_value = "";
    public int status;

    public static boolean isIndoor(List<DeviceParam> list) {
        if (!(list == null || list.isEmpty())) {
            for (DeviceParam deviceParam : list) {
                if (deviceParam != null) {
                    if (deviceParam.param_type == TYPE_INDOOR_OUTDOOR) {
                        return "1".equals(deviceParam.param_value);
                    }
                }
            }
        }
        return false;
    }

    public static boolean isPrivateMode(List<DeviceParam> list) {
        if (!(list == null || list.isEmpty())) {
            for (DeviceParam deviceParam : list) {
                if (deviceParam != null) {
                    if (deviceParam.param_type == TYPE_PRIVATE_MODE) {
                        return "1".equals(deviceParam.param_value);
                    }
                }
            }
        }
        return false;
    }

    public static boolean isPrivateMode(DeviceParam deviceParam) {
        if (deviceParam == null || deviceParam.param_type != TYPE_PRIVATE_MODE) {
            return false;
        }
        return "1".equals(deviceParam.param_value);
    }

    public static boolean isOpenPIR(List<DeviceParam> list) {
        if (!(list == null || list.isEmpty())) {
            for (DeviceParam deviceParam : list) {
                if (deviceParam != null) {
                    if (deviceParam.param_type == 1011) {
                        return "1".equals(deviceParam.param_value);
                    }
                }
            }
        }
        return false;
    }

    public static boolean isOpenPIR(DeviceParam deviceParam) {
        if (deviceParam != null && deviceParam.param_type == 1011) {
            if ("1".equals(deviceParam.param_value)) {
                return true;
            }
        }
        return false;
    }

    public static int getBatteryLev(DeviceParam deviceParam) {
        int i = 0;
        if (deviceParam == null) {
            return 0;
        }
        int i2 = deviceParam.param_type;
        if (i2 != 1101) {
            i = 0;
        } else if (!StringUtils.isEmpty(deviceParam.param_value)) {
            int intValue = Integer.valueOf(deviceParam.param_value).intValue();
            if (intValue >= 6) {
                i = intValue < 10 ? 1 : intValue < 25 ? 2 : intValue < 45 ? 3 : intValue < 70 ? 4 : 5;
            }
        }
        return i;
    }

    public static int getBatteryValue(DeviceParam deviceParam) {
        int i = 0;
        if (deviceParam == null) {
            return 0;
        }
        if (deviceParam != null) {
            try {
                if (!StringUtils.isEmpty(deviceParam.param_value)) {
                    int intValue = Integer.valueOf(deviceParam.param_value).intValue();
                    if (intValue < 0) {
                        intValue = 0;
                    }
                    i = intValue > 100 ? 100 : intValue;
                }
            } catch (Exception e) {
                
            }
        }
        return i;
    }

    public static int getMirrorMode(DeviceParam deviceParam) {
        int i = 0;
        if (deviceParam == null) {
            return 0;
        }
        if (deviceParam.param_type == CommandType.APP_CMD_SET_MIRRORMODE
                && !StringUtils.isEmpty(deviceParam.param_value)) {
            try {
                i = Integer.valueOf(deviceParam.param_value).intValue();
            } catch (Exception e) {
            }
        }
        return i;
    }

    public static boolean isOpenLED(DeviceParam deviceParam) {
        if (deviceParam != null && deviceParam.param_type == CommandType.APP_CMD_DEV_LED_SWITCH) {
            if ("1".equals(deviceParam.param_value)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isOpenIRCUT(DeviceParam deviceParam) {
        if (deviceParam != null && deviceParam.param_type == 1013) {
            if ("1".equals(deviceParam.param_value)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isOpenAUDDEC(DeviceParam deviceParam) {
        if (deviceParam != null && deviceParam.param_type == 1017) {
            if ("1".equals(deviceParam.param_value)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isOpenEAS(DeviceParam deviceParam) {
        if (deviceParam != null && deviceParam.param_type == 1015) {
            if ("1".equals(deviceParam.param_value)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isOpenRecord(String str) {
        boolean z = false;
        try {
            if ((Integer.valueOf(str).intValue() & 1) == 1) {
                z = true;
            }
            return z;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isOpenSensorNotification(String str) {
        boolean z = false;
        try {
            if ((Integer.valueOf(str).intValue() & 8) == 8) {
                z = true;
            }
            return z;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isOpenSensorHomebaseAlarm(String str) {
        boolean z = false;
        try {
            if ((Integer.valueOf(str).intValue() & 4) == 4) {
                z = true;
            }
            return z;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isOpenNotification(String str) {
        boolean z = false;
        try {
            if ((Integer.valueOf(str).intValue() & 8) == 8) {
                z = true;
            }
            return z;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isOpenDeviceAlarm(String str) {
        boolean z = false;
        try {
            if ((Integer.valueOf(str).intValue() & 2) == 2) {
                z = true;
            }
            return z;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isOpenHomebaseAlarm(String str) {
        boolean z = false;
        try {
            if ((Integer.valueOf(str).intValue() & 4) == 4) {
                z = true;
            }
            return z;
        } catch (Exception e) {
            return false;
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("param_id=");
        stringBuilder.append(this.param_id);
        stringBuilder.append("create_time=");
        stringBuilder.append(this.create_time);
        stringBuilder.append("param_type=");
        stringBuilder.append(this.param_type);
        stringBuilder.append("param_value=");
        stringBuilder.append(this.param_value);
        stringBuilder.append("device_sn=");
        stringBuilder.append(this.device_sn);
        stringBuilder.append("status=");
        stringBuilder.append(this.status);
        return stringBuilder.toString();
    }
}
