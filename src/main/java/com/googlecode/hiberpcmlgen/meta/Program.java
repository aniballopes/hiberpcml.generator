/*
 * The MIT License
 *
 * Copyright 2011 John Arevalo <johnarevalo@gmail.com>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.googlecode.hiberpcmlgen.meta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author John Arevalo <johnarevalo@gmail.com>
 * @see http://publib.boulder.ibm.com/infocenter/iseries/v5r4/topic/rzahh/pcmlpgtg.htm
 */
public class Program implements Serializable {

    private String name;
    private String entryPoint;
    private String epccsid;
    private String path;
    private String parseOrder;
    private String returnValue;
    private String label;
    private boolean threadSafe = false;
    private List<Data> dataElements = new ArrayList<Data>();

    public List<Data> getDataElements() {
        return dataElements;
    }

    public void setDataElements(List<Data> dataElements) {
        this.dataElements = dataElements;
    }

    public String getEntryPoint() {
        return entryPoint;
    }

    public void setEntryPoint(String entryPoint) {
        this.entryPoint = entryPoint;
    }

    public String getEpccsid() {
        return epccsid;
    }

    public void setEpccsid(String epccsid) {
        this.epccsid = epccsid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParseOrder() {
        return parseOrder;
    }

    public void setParseOrder(String parseOrder) {
        this.parseOrder = parseOrder;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(String returnValue) {
        this.returnValue = returnValue;
    }

    public boolean isThreadSafe() {
        return threadSafe;
    }

    public void setThreadSafe(boolean threadSafe) {
        this.threadSafe = threadSafe;
    }

    public void addData(Data data) {
        dataElements.add(data);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
