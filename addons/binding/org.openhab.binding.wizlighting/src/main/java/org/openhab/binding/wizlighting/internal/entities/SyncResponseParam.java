/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.wizlighting.internal.entities;

/**
 * This POJO represents one Wiz Lighting Sync params
 *
 * @author Sriram Balakrishnan - Initial contribution
 *
 */
public class SyncResponseParam extends Param {
    public String mac;
    public boolean state;
    public int sceneId;
    public boolean play;
    public int speed;
    public int r;
    public int g;
    public int b;
    public int c;
    public int w;
    public int dimming;
    public int rssi;
}
