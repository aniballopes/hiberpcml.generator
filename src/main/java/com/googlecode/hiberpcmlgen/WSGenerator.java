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
package com.googlecode.hiberpcmlgen;

import com.googlecode.hiberpcml.Manager;
import com.sun.codemodel.JAnnotationUse;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JPackage;
import com.sun.codemodel.JType;
import com.sun.codemodel.JVar;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import com.googlecode.hiberpcmlgen.meta.Data;
import com.googlecode.hiberpcmlgen.meta.Pcml;
import com.googlecode.hiberpcmlgen.meta.Program;
import com.googlecode.hiberpcmlgen.meta.Util;

/**
 *
 * @author John Arevalo <johnarevalo@gmail.com>
 */
public class WSGenerator {

    private JPackage _package;
    private JCodeModel cm;
    private JDefinedClass spiClass;
    private JFieldVar pcmlManager;

    /**
     * Initialize a Web Service Generator
     * @param packageName name of the package which contains the generated classes
     * @param name The name of the web service. see {@link WebService#name()}
     * @param serviceName The service name of the Web Service. see {@link WebService#serviceName()}
     * @throws Exception 
     */
    public WSGenerator(String packageName, String name, String serviceName) throws Exception {
        cm = new JCodeModel();
        _package = cm._package(packageName);
        spiClass = _package._class(com.googlecode.hiberpcml.Util.toCamelCase(serviceName));
        JAnnotationUse annotate = spiClass.annotate(WebService.class);
        annotate.param("name", name);
        annotate.param("serviceName", serviceName);
        pcmlManager = spiClass.field(JMod.PRIVATE, Manager.class, "pcmlManager");
    }

    public void addMethod(Pcml pcml) throws Exception {
        Generator generator = new Generator();
        generator.generate(_package, pcml);
        //adding webMethod
        JMethod method;
        JDefinedClass returnClass = getReturnClass(generator);
        JFieldVar returnField = null;
        if (returnClass.fields().size() > 1) {
            method = spiClass.method(JMod.PUBLIC, returnClass, pcml.getWebMethodName());
        } else {
            returnField = returnClass.fields().get(returnClass.fields().keySet().iterator().next());
            method = spiClass.method(JMod.PUBLIC, returnField.type(), pcml.getWebMethodName());
            _package.remove(returnClass);
            returnClass = null;
        }

        method.annotate(WebMethod.class);
        JDefinedClass structClass;
        Class type;
        JDefinedClass generatedClass = generator.getDefinedClass();
        JVar pcmlObject = method.body().decl(generatedClass, "pcml", JExpr._new(generatedClass));

        //Setting params for webservice method
        for (Data data : pcml.getProgram().getDataElements()) {
            if (Data.PARAM_MODE.equals(data.getMode())) {
                if (data.isStruct()) {
                    structClass = generator.getStructClass(data.getName());
                    JVar param = method.param(structClass, data.getLabel());
                    JMethod setter = generatedClass.getMethod(
                            "set" + com.googlecode.hiberpcml.Util.toCamelCase(
                            data.getLabel()), new JType[]{structClass});

                    method.body().invoke(pcmlObject, setter).arg(param);
                    JAnnotationUse annotate = param.annotate(WebParam.class);
                    annotate.param("name", param.name());
                } else {
                    type = Util.getType(data.getType());
                    JVar param = method.param(type, data.getLabel());
                    JType jType = cm._ref(type);
                    JMethod setter = generatedClass.getMethod(
                            "set" + com.googlecode.hiberpcml.Util.toCamelCase(
                            data.getLabel()), new JType[]{jType});
                    method.body().invoke(pcmlObject, setter).arg(param);
                    JAnnotationUse annotate = param.annotate(WebParam.class);
                    annotate.param("name", param.name());
                }
            }
        }

        method.body().invoke(pcmlManager, "invoke").arg(pcmlObject);

        if (returnClass == null) {
            JMethod getter = generatedClass.getMethod(
                    "get" + com.googlecode.hiberpcml.Util.toCamelCase(returnField.name()), new JType[]{});
            method.body()._return(JExpr.invoke(pcmlObject, getter));
        } else {
            JVar returnObject = method.body().decl(returnClass, "returnObject", JExpr._new(returnClass));
            Iterator<Entry<String, JFieldVar>> iterator = returnClass.fields().entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<String, JFieldVar> next = iterator.next();
                JMethod setter = returnClass.getMethod(
                        "set" + com.googlecode.hiberpcml.Util.toCamelCase(next.getKey()), new JType[]{next.getValue().type()});
                JMethod getter = generatedClass.getMethod(
                        "get" + com.googlecode.hiberpcml.Util.toCamelCase(next.getKey()), new JType[]{});
                method.body().invoke(returnObject, setter).arg(JExpr.invoke(pcmlObject, getter));
            }
            method.body()._return(returnObject);
        }
    }

    public void build(File target) throws IOException {
        target.mkdirs();
        cm.build(target);
    }

    public JDefinedClass getReturnClass(Generator generator) throws Exception {
        Object returnClass = null;
        Program program = generator.getProgram();
        JDefinedClass complexClass = null;

        for (Data data : program.getDataElements()) {
            if (Data.RETURN_MODE.equals(data.getMode())) {
                if (returnClass != null) {
                    if (data.isStruct()) {
                        complexClass.field(JMod.PRIVATE, Util.getType(data.getType()), data.getLabel());
                        Util.generateGetterAndSetter(complexClass, Util.getType(data.getType()), data.getLabel());
                    } else {
                        returnClass = Util.getType(data.getType());
                        complexClass.field(JMod.PRIVATE, (Class) returnClass, data.getLabel());
                        Util.generateGetterAndSetter(complexClass, (Class) returnClass, data.getLabel());
                    }
                } else {
                    complexClass = _package._class(program.getLabel() + "ReturnType");
                    if (data.isStruct()) {
                        returnClass = generator.getStructClass(data.getName());
                        complexClass.field(JMod.PRIVATE, Util.getType(data.getType()), data.getLabel());
                        Util.generateGetterAndSetter(complexClass, Util.getType(data.getType()), data.getLabel());
                    } else {
                        returnClass = Util.getType(data.getType());
                        complexClass.field(JMod.PRIVATE, (Class) returnClass, data.getLabel());
                        Util.generateGetterAndSetter(complexClass, (Class) returnClass, data.getLabel());
                    }
                }
            }
        }

        return complexClass;
    }
}
