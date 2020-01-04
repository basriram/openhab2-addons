/**
 * Copyright (c) 2010-2019 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.eufysecurity.internal.api.model;

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * {@code CommandType} enumeration of all valid commands that can be sent over P2P connection
 *
 * @author Sriram Balakrishnan - Initial contribution
 *
 */
@NonNullByDefault
public class CommandType {
    public static final int APP_CMD_AUDDEC_SWITCH = 1017;
    public static final int APP_CMD_AUDIO_FRAME = 1301;
    public static final int APP_CMD_BATCH_RECORD = 1049;
    public static final int APP_CMD_BIND_BROADCAST = 1000;
    public static final int APP_CMD_BIND_SYNC_ACCOUNT_INFO = 1001;
    public static final int APP_CMD_BIND_SYNC_ACCOUNT_INFO_EX = 1054;
    public static final int APP_CMD_CAMERA_INFO = 1103;
    public static final int APP_CMD_CHANGE_PWD = 1030;
    public static final int APP_CMD_CHANGE_WIFI_PWD = 1031;
    public static final int APP_CMD_CLOSE_AUDDEC = 1018;
    public static final int APP_CMD_CLOSE_DEV_LED = 1046;
    public static final int APP_CMD_CLOSE_EAS = 1016;
    public static final int APP_CMD_CLOSE_IRCUT = 1014;
    public static final int APP_CMD_CLOSE_PIR = 1012;
    public static final int APP_CMD_COLLECT_RECORD = 1047;
    public static final int APP_CMD_CONVERT_MP4_OK = 1303;
    public static final int APP_CMD_DECOLLECT_RECORD = 1048;
    public static final int APP_CMD_DELLETE_RECORD = 1027;
    public static final int APP_CMD_DEL_FACE_PHOTO = 1234;
    public static final int APP_CMD_DEL_USER_PHOTO = 1232;
    public static final int APP_CMD_DEVS_BIND_BROADCASE = 1038;
    public static final int APP_CMD_DEVS_BIND_NOTIFY = 1039;
    public static final int APP_CMD_DEVS_LOCK = 1019;
    public static final int APP_CMD_DEVS_SWITCH = 1035;
    public static final int APP_CMD_DEVS_TO_FACTORY = 1037;
    public static final int APP_CMD_DEVS_UNBIND = 1040;
    public static final int APP_CMD_DEVS_UNLOCK = 1020;
    public static final int APP_CMD_DEV_LED_SWITCH = 1045;
    public static final int APP_CMD_DEV_PUSHMSG_MODE = 1252;
    public static final int APP_CMD_DEV_RECORD_AUTOSTOP = 1251;
    public static final int APP_CMD_DEV_RECORD_INTERVAL = 1250;
    public static final int APP_CMD_DEV_RECORD_TIMEOUT = 1249;
    public static final int APP_CMD_DOENLOAD_FINISH = 1304;
    public static final int APP_CMD_DOORBELL_NOTIFY_PAYLOAD = 1701;
    public static final int APP_CMD_DOORBELL_SET_PAYLOAD = 1700;
    public static final int APP_CMD_DOOR_SENSOR_ALARM_ENABLE = 1506;
    public static final int APP_CMD_DOOR_SENSOR_DOOR_EVT = 1503;
    public static final int APP_CMD_DOOR_SENSOR_ENABLE_LED = 1505;
    public static final int APP_CMD_DOOR_SENSOR_GET_DOOR_STATE = 1502;
    public static final int APP_CMD_DOOR_SENSOR_GET_INFO = 1501;
    public static final int APP_CMD_DOOR_SENSOR_INFO_REPORT = 1500;
    public static final int APP_CMD_DOOR_SENSOR_LOW_POWER_REPORT = 1504;
    public static final int APP_CMD_DOWNLOAD_CANCEL = 1051;
    public static final int APP_CMD_DOWNLOAD_VIDEO = 1024;
    public static final int APP_CMD_EAS_SWITCH = 1015;
    public static final int APP_CMD_ENTRY_SENSOR_BAT_STATE = 1552;
    public static final int APP_CMD_ENTRY_SENSOR_CHANGE_TIME = 1551;
    public static final int APP_CMD_ENTRY_SENSOR_STATUS = 1550;
    public static final int APP_CMD_FLOODLIGHT_BROADCAST = 902;
    public static final int APP_CMD_FORMAT_SD = 1029;
    public static final int APP_CMD_FORMAT_SD_PROGRESS = 1053;
    public static final int APP_CMD_GATEWAYINFO = 1100;
    public static final int APP_CMD_GEO_ADD_USER_INFO = 1259;
    public static final int APP_CMD_GEO_DEL_USER_INFO = 1261;
    public static final int APP_CMD_GEO_SET_USER_STATUS = 1258;
    public static final int APP_CMD_GEO_UPDATE_LOC_SETTING = 1262;
    public static final int APP_CMD_GEO_UPDATE_USER_INFO = 1260;
    public static final int APP_CMD_GET_ADMIN_PWD = 1122;
    public static final int APP_CMD_GET_ALARM_MODE = 1151;
    public static final int APP_CMD_GET_ARMING_INFO = 1107;
    public static final int APP_CMD_GET_ARMING_STATUS = 1108;
    public static final int APP_CMD_GET_AUDDEC_INFO = 1109;
    public static final int APP_CMD_GET_AUDDEC_SENSITIVITY = 1110;
    public static final int APP_CMD_GET_AUDDE_CSTATUS = 1111;
    public static final int APP_CMD_GET_AWAY_ACTION = 1239;
    public static final int APP_CMD_GET_BATTERY = 1101;
    public static final int APP_CMD_GET_BATTERY_TEMP = 1138;
    public static final int APP_CMD_GET_CAMERA_LOCK = 1119;
    public static final int APP_CMD_GET_CHARGE_STATUS = 1136;
    public static final int APP_CMD_GET_CUSTOM1_ACTION = 1148;
    public static final int APP_CMD_GET_CUSTOM2_ACTION = 1149;
    public static final int APP_CMD_GET_CUSTOM3_ACTION = 1150;
    public static final int APP_CMD_GET_DEVICE_PING = 1152;
    public static final int APP_CMD_GET_DEVS_NAME = 1129;
    public static final int APP_CMD_GET_DEVS_RSSI_LIST = 1274;
    public static final int APP_CMD_GET_DEV_STATUS = 1131;
    public static final int APP_CMD_GET_DEV_TONE_INFO = 1127;
    public static final int APP_CMD_GET_DEV_UPGRADE = 1134;
    public static final int APP_CMD_GET_EAS_STATUS = 1118;
    public static final int APP_CMD_GET_EXCEPTION_LOG = 1124;
    public static final int APP_CMD_GET_FLOODLIGHT_WIFI_LIST = 1405;
    public static final int APP_CMD_GET_GATEWAY_LOCK = 1120;
    public static final int APP_CMD_GET_HOME_ACTION = 1225;
    public static final int APP_CMD_GET_HUB_LOG = 1132;
    public static final int APP_CMD_GET_HUB_LOGIG = 1140;
    public static final int APP_CMD_GET_HUB_NAME = 1128;
    public static final int APP_CMD_GET_HUB_POWWER_SUPPLY = 1137;
    public static final int APP_CMD_GET_HUB_TONE_INFO = 1126;
    public static final int APP_CMD_GET_HUB_UPGRADE = 1133;
    public static final int APP_CMD_GET_IRCUTSENSITIVITY = 1114;
    public static final int APP_CMD_GET_IRMODE = 1113;
    public static final int APP_CMD_GET_MDETECT_PARAM = 1105;
    public static final int APP_CMD_GET_MIRRORMODE = 1112;
    public static final int APP_CMD_GET_NEWVESION = 1125;
    public static final int APP_CMD_GET_P2P_CONN_STATUS = 1130;
    public static final int APP_CMD_GET_PIRCTRL = 1116;
    public static final int APP_CMD_GET_PIRINFO = 1115;
    public static final int APP_CMD_GET_PIRSENSITIVITY = 1117;
    public static final int APP_CMD_GET_RECORD_TIME = 1104;
    public static final int APP_CMD_GET_REPEATER_CONN_TEST_RESULT = 1270;
    public static final int APP_CMD_GET_REPEATER_RSSI = 1266;
    public static final int APP_CMD_GET_REPEATER_SITE_LIST = 1263;
    public static final int APP_CMD_GET_SUB1G_RSSI = 1141;
    public static final int APP_CMD_GET_TFCARD_FORMAT_STATUS = 1143;
    public static final int APP_CMD_GET_TFCARD_REPAIR_STATUS = 1153;
    public static final int APP_CMD_GET_TFCARD_STATUS = 1135;
    public static final int APP_CMD_GET_UPDATE_STATUS = 1121;
    public static final int APP_CMD_GET_UPGRADE_RESULT = 1043;
    public static final int APP_CMD_GET_WAN_LINK_STATUS = 1268;
    public static final int APP_CMD_GET_WAN_MODE = 1265;
    public static final int APP_CMD_GET_WIFI_PWD = 1123;
    public static final int APP_CMD_GET_WIFI_RSSI = 1142;
    public static final int APP_CMD_HUB_CLEAR_EMMC_VOLUME = 1800;
    public static final int APP_CMD_HUB_REBOOT = 1034;
    public static final int APP_CMD_HUB_TO_FACTORY = 1036;
    public static final int APP_CMD_IRCUT_SWITCH = 1013;
    public static final int APP_CMD_LIVEVIEW_LED_SWITCH = 1056;
    public static final int APP_CMD_MDETECTINFO = 1106;
    public static final int APP_CMD_NAS_SWITCH = 1145;
    public static final int APP_CMD_NAS_TEST = 1146;
    public static final int APP_CMD_NOTIFY_PAYLOAD = 1351;
    public static final int APP_CMD_P2P_DISCONNECT = 1044;
    public static final int APP_CMD_PING = 1139;
    public static final int APP_CMD_PIR_SWITCH = 1011;
    public static final int APP_CMD_RECORDDATE_SEARCH = 1041;
    public static final int APP_CMD_RECORDLIST_SEARCH = 1042;
    public static final int APP_CMD_RECORD_IMG = 1021;
    public static final int APP_CMD_RECORD_IMG_STOP = 1022;
    public static final int APP_CMD_RECORD_PLAY_CTRL = 1026;
    public static final int APP_CMD_RECORD_VIEW = 1025;
    public static final int APP_CMD_REPAIR_PROGRESS = 1058;
    public static final int APP_CMD_REPAIR_SD = 1057;
    public static final int APP_CMD_REPEATER_RSSI_TEST = 1269;
    public static final int APP_CMD_SDINFO = 1102;
    public static final int APP_CMD_SDINFO_EX = 1144;
    public static final int APP_CMD_SET_AI_NICKNAME = 1242;
    public static final int APP_CMD_SET_AI_PHOTO = 1231;
    public static final int APP_CMD_SET_AI_SWITCH = 1236;
    public static final int APP_CMD_SET_ALL_ACTION = 1255;
    public static final int APP_CMD_SET_ARMING = 1224;
    public static final int APP_CMD_SET_ARMING_SCHEDULE = 1211;
    public static final int APP_CMD_SET_AS_SERVER = 1237;
    public static final int APP_CMD_SET_AUDDEC_INFO = 1212;
    public static final int APP_CMD_SET_AUDDEC_SENSITIVITY = 1213;
    public static final int APP_CMD_SET_AUDIOSENSITIVITY = 1227;
    public static final int APP_CMD_SET_BITRATE = 1206;
    public static final int APP_CMD_SET_CUSTOM_MODE = 1256;
    public static final int APP_CMD_SET_DEVS_NAME = 1217;
    public static final int APP_CMD_SET_DEVS_OSD = 1214;
    public static final int APP_CMD_SET_DEVS_TONE_FILE = 1202;
    public static final int APP_CMD_SET_DEV_MD_RECORD = 1273;
    public static final int APP_CMD_SET_DEV_MIC_MUTE = 1240;
    public static final int APP_CMD_SET_DEV_MIC_VOLUME = 1229;
    public static final int APP_CMD_SET_DEV_SPEAKER_MUTE = 1241;
    public static final int APP_CMD_SET_DEV_SPEAKER_VOLUME = 1230;
    public static final int APP_CMD_SET_DEV_STORAGE_TYPE = 1228;
    public static final int APP_CMD_SET_FLOODLIGHT_BRIGHT_VALUE = 1401;
    public static final int APP_CMD_SET_FLOODLIGHT_DETECTION_AREA = 1407;
    public static final int APP_CMD_SET_FLOODLIGHT_LIGHT_SCHEDULE = 1404;
    public static final int APP_CMD_SET_FLOODLIGHT_MANUAL_SWITCH = 1400;
    public static final int APP_CMD_SET_FLOODLIGHT_STREET_LAMP = 1402;
    public static final int APP_CMD_SET_FLOODLIGHT_TOTAL_SWITCH = 1403;
    public static final int APP_CMD_SET_FLOODLIGHT_WIFI_CONNECT = 1406;
    public static final int APP_CMD_SET_GSSENSITIVITY = 1226;
    public static final int APP_CMD_SET_HUB_AUDEC_STATUS = 1222;
    public static final int APP_CMD_SET_HUB_GS_STATUS = 1220;
    public static final int APP_CMD_SET_HUB_IRCUT_STATUS = 1219;
    public static final int APP_CMD_SET_HUB_MVDEC_STATUS = 1221;
    public static final int APP_CMD_SET_HUB_NAME = 1216;
    public static final int APP_CMD_SET_HUB_OSD = 1253;
    public static final int APP_CMD_SET_HUB_PIR_STATUS = 1218;
    public static final int APP_CMD_SET_HUB_SPK_VOLUME = 1235;
    public static final int APP_CMD_SET_IRMODE = 1208;
    public static final int APP_CMD_SET_JSON_SCHEDULE = 1254;
    public static final int APP_CMD_SET_LANGUAGE = 1200;
    public static final int APP_CMD_SET_LIGHT_CTRL_BRIGHT_PIR = 1412;
    public static final int APP_CMD_SET_LIGHT_CTRL_BRIGHT_SCH = 1413;
    public static final int APP_CMD_SET_LIGHT_CTRL_LAMP_VALUE = 1410;
    public static final int APP_CMD_SET_LIGHT_CTRL_PIR_SWITCH = 1408;
    public static final int APP_CMD_SET_LIGHT_CTRL_PIR_TIME = 1409;
    public static final int APP_CMD_SET_LIGHT_CTRL_SUNRISE_INFO = 1415;
    public static final int APP_CMD_SET_LIGHT_CTRL_SUNRISE_SWITCH = 1414;
    public static final int APP_CMD_SET_LIGHT_CTRL_TRIGGER = 1411;
    public static final int APP_CMD_SET_MDETECTPARAM = 1204;
    public static final int APP_CMD_SET_MDSENSITIVITY = 1272;
    public static final int APP_CMD_SET_MIRRORMODE = 1207;
    public static final int APP_CMD_SET_MOTION_SENSITIVITY = 1276;
    public static final int APP_CMD_SET_NIGHT_VISION_TYPE = 1277;
    public static final int APP_CMD_SET_NOTFACE_PUSHMSG = 1248;
    public static final int APP_CMD_SET_PAYLOAD = 1350;
    public static final int APP_CMD_SET_PIRSENSITIVITY = 1210;
    public static final int APP_CMD_SET_PIR_INFO = 1209;
    public static final int APP_CMD_SET_PIR_POWERMODE = 1246;
    public static final int APP_CMD_SET_PIR_TEST_MODE = 1243;
    public static final int APP_CMD_SET_PRI_ACTION = 1233;
    public static final int APP_CMD_SET_RECORDTIME = 1203;
    public static final int APP_CMD_SET_REPEATER_PARAMS = 1264;
    public static final int APP_CMD_SET_RESOLUTION = 1205;
    public static final int APP_CMD_SET_SCHEDULE_DEFAULT = 1257;
    public static final int APP_CMD_SET_SNOOZE_MODE = 1271;
    public static final int APP_CMD_SET_STORGE_TYPE = 1223;
    public static final int APP_CMD_SET_TELNET = 1247;
    public static final int APP_CMD_SET_TIMEZONE = 1215;
    public static final int APP_CMD_SET_TONE_FILE = 1201;
    public static final int APP_CMD_SET_UPGRADE = 1238;
    public static final int APP_CMD_SNAPSHOT = 1028;
    public static final int APP_CMD_START_REALTIME_MEDIA = 1003;
    public static final int APP_CMD_START_RECORD = 1009;
    public static final int APP_CMD_START_REC_BROADCASE = 900;
    public static final int APP_CMD_START_TALKBACK = 1005;
    public static final int APP_CMD_START_VOICECALL = 1007;
    public static final int APP_CMD_STOP_REALTIME_MEDIA = 1004;
    public static final int APP_CMD_STOP_RECORD = 1010;
    public static final int APP_CMD_STOP_REC_BROADCASE = 901;
    public static final int APP_CMD_STOP_SHARE = 1023;
    public static final int APP_CMD_STOP_TALKBACK = 1006;
    public static final int APP_CMD_STOP_VOICECALL = 1008;
    public static final int APP_CMD_STREAM_MSG = 1302;
    public static final int APP_CMD_STRESS_TEST_OPER = 1050;
    public static final int APP_CMD_TIME_SYCN = 1033;
    public static final int APP_CMD_UNBIND_ACCOUNT = 1002;
    public static final int APP_CMD_VIDEO_FRAME = 1300;
    public static final int APP_CMD_WIFI_CONFIG = 1032;
    public static final int BASE_STATETION = 0;
    public static final int BAT_CAMERA = 1;
    public static final int BAT_CAMERA2 = 8;
    public static final int CLOUD_STORGE = 2;
    public static final int DOOR_SENSOR = 2;
    public static final int FLOODLIGHT_CAM = 3;
    public static final int LOACL_AND_CLOUD = 3;
    public static final int LOCAL_STORGE = 1;
    public static final int POWER_MODE_CUSTOM = 2;
    public static final int POWER_MODE_NORMAL = 1;
    public static final int POWER_MODE_SAVE = 0;
    public static final int PUSH_MSG_MODE_ALL = 2;
    public static final int PUSH_MSG_MODE_BODY = 0;
    public static final int PUSH_MSG_MODE_FACE = 1;
    public static final int SUB1G_REP_BIND_FAILED = 2109;
    public static final int SUB1G_REP_CHARGE_STATE = 2108;
    public static final int SUB1G_REP_POWER_OFF = 2110;
    public static final int SUB1G_REP_RUNTIME_STATE = 2107;
    public static final int SUB1G_REP_UNPLUG_POWER_LINE = 2111;
    public static final int VIDEO_HIGH_QUALITY = 1;
    public static final int VIDEO_LOW_QUALITY = 3;
    public static final int VIDEO_MID_QUALITY = 2;
    public static final int XM_VIDEO_MIRRMODE_HORIZONTALLY = 1;
    public static final int XM_VIDEO_MIRRMODE_HORIZVERTI = 3;
    public static final int XM_VIDEO_MIRRMODE_NORMAL = 0;
    public static final int XM_VIDEO_MIRRMODE_VERTICALLY = 2;
}