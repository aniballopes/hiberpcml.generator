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

import com.sun.codemodel.*;
import java.io.File;
import java.io.OutputStream;
import java.math.BigDecimal;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 *
 * @author John Arevalo <johnarevalo@gmail.com>
 */
public class Util {

    /**
     * Get the class according with the type param
     * @param type the type loaded from pcml definition
     * @return  the class used to map the value
     * @see <a href="http://publib.boulder.ibm.com/infocenter/iseries/v5r4/topic/rzahh/pcmldttg.htm">PCML type attribute</a>
     */
    public static Class getType(String type) {
        if (type == null) {
            return null;
        } else if ("char".equals(type)) {
            return String.class;
        } else if ("packed".equals(type)) {
            return BigDecimal.class;
        } else if ("zoned".equals(type)) {
            return BigDecimal.class;
        } else if ("int".equals(type)) {
            return Long.class;
        } else if ("float".equals(type)) {
            return Float.class;
        } else if ("byte".equals(type)) {
            return byte[].class;
        } else if ("struct".equals(type)) {
            return String.class;
        } else {
            return null;
        }
    }

    public static Pcml load(File file) throws Exception {
        JAXBContext context = JAXBContext.newInstance(Pcml.class);
        Pcml pcml = (Pcml) context.createUnmarshaller().unmarshal(file);
        Program program = pcml.getProgram();
        pcml.setFileName(file.getName());
        if (program == null || isEmpty(program.getLabel())) {
            pcml.getProgram().setLabel(program.getName());
        }
        for (Data data : pcml.getProgram().getDataElements()) {
            if (Util.isEmpty(data.getLabel())) {
                data.setLabel(Util.clean(data.getName()));
            }
        }
        return pcml;
    }

    public static void store(Pcml pcml, OutputStream output) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Pcml.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty("jaxb.fragment", Boolean.TRUE);
        marshaller.setProperty("jaxb.formatted.output", Boolean.TRUE);
        //Delete fileName property to be ignored in .pcml file 
        pcml.setFileName(null);
        marshaller.marshal(pcml, output);
    }

    public static String clean(String name) {
        return name == null
                ? null
                : name.replaceAll("[\\W]", "");
    }

    public static void generateGetterAndSetter(JDefinedClass _class, Class type, String name) {
        String camelCased = com.googlecode.hiberpcml.Util.toCamelCase(name);
        //Getter
        JMethod getter;
        getter = _class.method(JMod.PUBLIC, type, "get" + camelCased);
        getter.body()._return(JExpr.ref(name));
        //Setter
        JMethod setter = _class.method(JMod.PUBLIC, void.class, "set" + camelCased);
        setter.param(type, name);
        setter.body().assign(JExpr._this().ref(name), JExpr.ref(name));
    }

    public static void generateGetterAndSetter(JDefinedClass _class, JType jType, String name) {
        String camelCased = com.googlecode.hiberpcml.Util.toCamelCase(name);
        //Getter
        JMethod getter;
        getter = _class.method(JMod.PUBLIC, jType, "get" + camelCased);
        getter.body()._return(JExpr.ref(name));
        //Setter
        JMethod setter = _class.method(JMod.PUBLIC, void.class, "set" + camelCased);
        setter.param(jType, name);
        setter.body().assign(JExpr._this().ref(name), JExpr.ref(name));
    }

    /**
     * checks if string is empty
     */
    public static boolean isEmpty(String string) {
        return string == null || string.trim().isEmpty();
    }
}
