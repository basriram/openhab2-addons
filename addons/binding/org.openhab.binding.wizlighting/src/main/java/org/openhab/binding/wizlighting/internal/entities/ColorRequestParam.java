/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.wizlighting.internal.entities;

import org.eclipse.smarthome.core.library.types.HSBType;

/**
 * This POJO represents Color Request Param
 *
 * @author Sriram Balakrishnan - Initial contribution
 *
 */
public class ColorRequestParam extends DimmingRequestParam {
    private int b; // blue 0-255
    private int g; // green 0-255
    private int r; // red 0-255
    private int w; // brightness
    private int c; // not sure 0-255

    public ColorRequestParam(HSBType color) {
        super(color.getBrightness().intValue());
        this.b = (int) Math.round(color.getBlue().intValue() * 2.55);
        this.g = (int) Math.round(color.getGreen().intValue() * 2.55);
        this.r = (int) Math.round(color.getRed().intValue() * 2.55);
        // strange logic here
        if (color.getSaturation().intValue() > 50) {
            this.w = 255;
        } else {
            this.w = 0;
        }

        if (this.r > 0 && this.b > 0 && this.g > 0) {
            this.w = 0;
        }
        // this.w = (int) Math.round(color.getSaturation().intValue() * 2.55);
        this.c = 0;
        // this.c = (int)Math.round(color.getSaturation().intValue() * 2.55);

    }

    public ColorRequestParam(int b, int g, int r, int w, int c) {
        super(100);
        this.b = b;
        this.g = g;
        this.r = r;
        this.w = w;
        this.c = c;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getC() {
        return c;
    }

    public void setC(int c) {
        this.c = c;
    }
}
