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
package com.googlecode.hiberpcml.generator;

import com.googlecode.hiberpcml.Array;
import com.googlecode.hiberpcml.Command;
import com.googlecode.hiberpcml.Element;
import com.googlecode.hiberpcml.UsageType;
import com.sun.codemodel.JAnnotationUse;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JPackage;
import com.sun.codemodel.JType;
import java.util.ArrayList;
import java.util.HashMap;
import com.googlecode.hiberpcml.generator.meta.Data;
import com.googlecode.hiberpcml.generator.meta.Pcml;
import com.googlecode.hiberpcml.generator.meta.Program;
import com.googlecode.hiberpcml.generator.meta.Struct;
import com.googlecode.hiberpcml.generator.meta.Util;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JInvocation;
import java.math.BigDecimal;

/**
 *
 * @author John Arevalo <johnarevalo@gmail.com>
 */
public class Generator {

    private HashMap<String, JDefinedClass> structClasses = new HashMap<String, JDefinedClass>();
    private Program program;
    private JDefinedClass definedClass;

    public JDefinedClass getStructClass(String name) {
        return structClasses.get(name);
    }

    public void generate(JPackage _package, Pcml pcml) throws Exception {

        program = pcml.getProgram();
        definedClass = _package._class(program.getLabel());
        JAnnotationUse annotate;

        //Load structs before load data types in Program
        for (Struct struct : pcml.getStructElements()) {
            JDefinedClass structClass = _package._class(struct.getLabel());
            structClasses.put(struct.getName(), structClass);
            structClass.annotate(com.googlecode.hiberpcml.Struct.class);
            for (Data data : struct.getDataElements()) {
                addData(structClass, data);
            }
        }

        for (Data data : program.getDataElements()) {
            addData(definedClass, data);
        }

        annotate = definedClass.annotate(Command.class);
        annotate.param("programName", program.getName());
        annotate.param("documentName", program.getName());
    }

    public void addData(JDefinedClass _class, Data data) {
        Class type = null;
        JType jType = null;

        int count = 0;
        try {
            count = Integer.parseInt(data.getCount());
        } catch (Exception ex) {
            //Ignore exception, count attribute is null
        }
        if (count > 0) {
            type = ArrayList.class;
        } else if (data.isStruct()) {
            jType = structClasses.get(data.getName());
        } else {
            type = Util.getType(data.getType());
        }

        JFieldVar field;
        //Creating initial value
        if (data.isStruct()) {
            field = _class.field(JMod.PRIVATE, jType, data.getLabel(), JExpr._new(jType));
        } else {
            //TODO: validate if it is possible get refType without create new JCodeModel instance
            JInvocation initial = JExpr._new(new JCodeModel()._ref(type));
            if (type.equals(BigDecimal.class)) {
                initial.arg("0");
            }
            field = _class.field(JMod.PRIVATE, type, data.getLabel(), initial);
        }
        //Identifying the usage 
        UsageType usage = UsageType.INPUTOUTPUT;
        if (data.getUsage().equals(UsageType.INPUT.value())) {
            usage = UsageType.INPUT;
        }
        if (data.getUsage().equals(UsageType.OUTPUT.value())) {
            usage = UsageType.OUTPUT;
        }
        //annotation
        if (count > 0) {
            JAnnotationUse annotate = field.annotate(Array.class);
            annotate.param("pcmlName", data.getName());
            annotate.param("size", data.getCount());
            annotate.param("type", Util.getType(data.getType()));
            annotate.param("usage", usage);
        } else {
            JAnnotationUse annotate = field.annotate(Element.class);
            annotate.param("pcmlName", data.getName());
            annotate.param("usage", usage);
        }

        if (data.isStruct()) {
            Util.generateGetterAndSetter(_class, jType, data.getLabel());
        } else {
            Util.generateGetterAndSetter(_class, type, data.getLabel());
        }
    }

    public Object getType(Data data) {
        if (data.isStruct()) {
            return structClasses.get(data.getName());
        } else {
            return Util.getType(data.getType());
        }
    }

    public Program getProgram() {
        return program;
    }

    public JDefinedClass getDefinedClass() {
        return definedClass;
    }
}
