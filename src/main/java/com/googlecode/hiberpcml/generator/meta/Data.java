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
package com.googlecode.hiberpcml.generator.meta;

import java.io.Serializable;

/**
 *
 * @author John Arevalo <johnarevalo@gmail.com>
 * @see http://publib.boulder.ibm.com/infocenter/iseries/v5r4/topic/rzahh/pcmldttg.htm
 */
public class Data implements Serializable {

    private String type;
    private String bidiStringType;
    private String ccsid;
    private String charType;
    private int count = 0;
    private String init;
    private String length;
    private String maxvrm;
    private String minvrm;
    private String name;
    private String offset;
    private String offsetFrom;
    private String outputSize;
    private String passby;
    private String precision;
    private String struct;
    private String trim;
    private String usage;
    private String label;

    public String getBidiStringType() {
        return bidiStringType;
    }

    public void setBidiStringType(String bidiStringType) {
        this.bidiStringType = bidiStringType;
    }

    public String getCcsid() {
        return ccsid;
    }

    public void setCcsid(String ccsid) {
        this.ccsid = ccsid;
    }

    public String getCharType() {
        return charType;
    }

    public void setCharType(String charType) {
        this.charType = charType;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getInit() {
        return init;
    }

    public void setInit(String init) {
        this.init = init;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
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

    public String getPassby() {
        return passby;
    }

    public void setPassby(String passby) {
        this.passby = passby;
    }

    public String getPrecision() {
        return precision;
    }

    public void setPrecision(String precision) {
        this.precision = precision;
    }

    public String getStruct() {
        return struct;
    }

    public void setStruct(String struct) {
        this.struct = struct;
    }

    public String getTrim() {
        return trim;
    }

    public void setTrim(String trim) {
        this.trim = trim;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isStruct() {
        return "struct".equals(type);
    }
}
