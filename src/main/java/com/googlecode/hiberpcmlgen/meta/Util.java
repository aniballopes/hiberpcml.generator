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

import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JType;
import java.io.File;
import java.math.BigDecimal;
import org.apache.commons.digester.Digester;

/**
 *
 * @author John Arevalo <johnarevalo@gmail.com>
 */
public class Util {

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
        Digester digester = new Digester();
        digester.setValidating(false);

        digester.addObjectCreate("pcml", Pcml.class);
        digester.addSetProperties("pcml");

        digester.addObjectCreate("pcml/struct", Struct.class);
        digester.addSetProperties("pcml/struct");
        digester.addSetNext("pcml/struct", "addStruct");

        digester.addObjectCreate("pcml/struct/data", Data.class);
        digester.addSetProperties("pcml/struct/data");
        digester.addSetNext("pcml/struct/data", "addData");


        digester.addObjectCreate("pcml/program", Program.class);
        digester.addSetProperties("pcml/program");
        digester.addSetNext("pcml/program", "setProgram");

        digester.addObjectCreate("pcml/program/data", Data.class);
        digester.addSetProperties("pcml/program/data");
        digester.addSetNext("pcml/program/data", "addData");

        return (Pcml) digester.parse(file);
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
}
