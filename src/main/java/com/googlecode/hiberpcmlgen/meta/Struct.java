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
 * @see http://publib.boulder.ibm.com/infocenter/iseries/v5r4/topic/rzahh/pcmlsttg.htm
 */
public class Struct implements Serializable {

    private String name;
    private String count;
    private String maxvrm;
    private String minvrm;
    private String offset;
    private String offsetFrom;
    private String outputSize;
    private String usage;
    private String label;
    private List<Data> dataElements = new ArrayList<Data>();

    public List<Data> getDataElements() {
        return dataElements;
    }

    public void setDataElements(List<Data> dataElements) {
        this.dataElements = dataElements;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getMaxvrm() {
        return maxvrm;
    }

    public void setMaxvrm(String maxvrm) {
        this.maxvrm = maxvrm;
    }

    public String getMinvrm() {
        return minvrm;
    }

    public void setMinvrm(String minvrm) {
        this.minvrm = minvrm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public String getOffsetFrom() {
        return offsetFrom;
    }

    public void setOffsetFrom(String offsetFrom) {
        this.offsetFrom = offsetFrom;
    }

    public String getOutputSize() {
        return outputSize;
    }

    public void setOutputSize(String outputSize) {
        this.outputSize = outputSize;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
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
