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

import com.googlecode.hiberpcmlgen.meta.Pcml;
import com.googlecode.hiberpcmlgen.meta.Util;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JPackage;
import java.io.File;
import junit.framework.TestCase;

/**
 *
 * @author John Arevalo <johnarevalo@gmail.com>
 */
public class GeneratorTest extends TestCase {

    public GeneratorTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of generate method, of class Generator.
     */
    public void testGenerate() throws Exception {
        System.out.println("generate");
        JCodeModel cm = new JCodeModel();
        JPackage _package = cm._package("testing.data");
        File target = new File("./target");
        Pcml pcml = Util.load(new File("C:\\config\\ppv421b.pcml"));
        Generator instance = new Generator();
        instance.generate(_package, pcml);
        cm.build(target);
        assert true;
    }
}
