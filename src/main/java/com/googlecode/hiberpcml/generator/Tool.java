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

import com.googlecode.hiberpcml.generator.meta.Pcml;
import com.googlecode.hiberpcml.generator.meta.Util;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JPackage;
import java.io.File;
import java.util.ArrayList;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;

/**
 *
 * @author John Arevalo <johnarevalo@gmail.com>
 */
public final class Tool {

    public static void main(String arg[]) throws Exception {
        CommandLineParser parser = new PosixParser();
        Options options = new Options();

        Option destinyOpt = new Option(
                "t", "target", true, "target of the generated classes");
        destinyOpt.setArgName("target");
        options.addOption(destinyOpt);

        Option packageOpt = new Option(
                "p", "package", true, "Java Package of the generated classes");
        packageOpt.setArgName("package");
        options.addOption(packageOpt);

        Option webServiceOpt = new Option(
                "w", "webservice", true, "build webservice classes");
        webServiceOpt.setArgs(2);
        webServiceOpt.setArgName("serviceName service");
        options.addOption(webServiceOpt);

        options.addOption(webServiceOpt);

        CommandLine cmd = parser.parse(options, arg);
        if (cmd.getArgs().length == 0) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("generator [options] FILE|DIRECTORY.", options);
            System.exit(1);
        }

        Pcml pcml;
        ArrayList<File> filesToParse = getFilesToParse(cmd.getArgs()[0]);
        JCodeModel cm = new JCodeModel();
        JPackage _package = cm._package(cmd.getOptionValue("p", ""));
        Generator generator;
        WSGenerator wsGenerator = null;
        File targetFile = new File(cmd.getOptionValue("d", "./target"));

        targetFile.mkdirs();
        if (cmd.hasOption("w")) {
            wsGenerator = new WSGenerator(_package, cmd.getOptionValues("w")[0],
                    cmd.getOptionValues("w")[1]);
        }

        for (File file : filesToParse) {
            pcml = Util.load(file);
            if (cmd.hasOption("w")) {
                wsGenerator.addMethod(pcml);
            } else {
                generator = new Generator();
                generator.generate(_package, pcml);
            }
        }
        cm.build(targetFile);
    }

    private static ArrayList<File> getFilesToParse(String source) {
        ArrayList<File> files = new ArrayList<File>();
        File sourceFile = new File(source);
        if (!sourceFile.exists()) {
            return files;
        }

        if (sourceFile.isDirectory()) {
            for (File file : sourceFile.listFiles()) {
                if (file.isFile()
                        && file.getName().toLowerCase().endsWith(".pcml")) {
                    files.add(file);
                }
            }
        } else {
            files.add(sourceFile);
        }

        return files;
    }
}
