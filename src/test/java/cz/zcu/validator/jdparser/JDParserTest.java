package cz.zcu.validator.jdparser;

import static org.junit.Assert.fail;

import java.io.File;

import org.junit.Test;

import cz.zcu.validator.jdparser.api.JsoupJavaDocParser;
import cz.zcu.validator.jdparser.data.JavaDocParsingException;

/**
 * Unit test for simple App.
 */
public class JDParserTest {

    @Test
    public void standardJavaDocDocumentsTest() {

        JsoupJavaDocParser parser = new JsoupJavaDocParser();

        try {

//            // parses javadoc of vogar project http://code.google.com/p/vogar/
            parser.parseDataFromJavaDoc(new File("src/test/javadoc-example/vogar/doc"));
//
//            // parses javadoc of pdfjbim project
//            // http://code.google.com/p/pdfrecompressor/
            parser.parseDataFromJavaDoc(new File("src/test/javadoc-example/pdfjbim/doc"));
//
//            // parses javadoc of gtkjfilechooser project
//            // http://code.google.com/p/gtkjfilechooser/
            parser.parseDataFromJavaDoc(new File("src/test/javadoc-example/gtkjfilechooser/doc"));
//
//            // parses javadoc of jflowmap project
//            // http://code.google.com/p/jflowmap/
            parser.parseDataFromJavaDoc(new File("src/test/javadoc-example/jflowmap/doc"));
//
//            // parses javadoc of openfire project
//            // http://code.google.com/p/openfire/
            parser.parseDataFromJavaDoc(new File("src/test/javadoc-example/openfire/src/doc"));
//
//            // parses javadoc of zmanim project
//            // http://code.google.com/p/kosherjava/
            parser.parseDataFromJavaDoc(new File("src/test/javadoc-example/zmanim/doc"));
//            
//            // parses some testing javadocs
            parser.parseDataFromJavaDoc(new File("src/test/javadoc-example/old/doc"));

//            // parses javadoc of jdparser project
            parser.parseDataFromJavaDoc(new File("src/test/javadoc-example/jdparser/doc"));
            
//          // parses javadoc of jdparser project
            parser.parseDataFromJavaDoc(new File("src/test/javadoc-example/oop-cv3-manualjavadoc/doc"));

        } catch (JavaDocParsingException e) {

            fail(e.getMessage());
        }

    }
}
