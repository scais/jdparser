package cz.zcu.validator.jdparser.api;

import java.io.File;

import cz.zcu.validator.jdparser.data.JavaDocParsingException;

/**
 * ONLY FOR LOCAL TESTING PURPOUSE.
 * 
 * @author Stepan Cais, scais@kiv.zcu.cz, 14.12.2012
 */
public class Main {

	/**
	 * @param args
	 *            Arguments from command line - nothing here.
	 */
	public static void main(String[] args) {

		JsoupJavaDocParser parser = new JsoupJavaDocParser();

		try {

			// parses javadoc of vogar project http://code.google.com/p/vogar/
			// parser.parseDataFromJavaDoc(new
			// File("src/test/javadoc-example/vogar/doc"));

			// parses javadoc of pdfjbim project
			// http://code.google.com/p/pdfrecompressor/
			// parser.parseDataFromJavaDoc(new
			// File("src/test/javadoc-example/pdfjbim/doc"));

			// parses javadoc of gtkjfilechooser project
			// http://code.google.com/p/gtkjfilechooser/
			// parser.parseDataFromJavaDoc(new
			// File("src/test/javadoc-example/gtkjfilechooser/doc"));

			// parses javadoc of jflowmap project
			// http://code.google.com/p/jflowmap/
			// parser.parseDataFromJavaDoc(new
			// File("src/test/javadoc-example/jflowmap/doc"));

			// parses javadoc of openfire project
			// http://code.google.com/p/openfire/
			// parser.parseDataFromJavaDoc(new
			// File("src/test/javadoc-example/openfire/src/doc"));

			// parses javadoc of zmanim project
			// http://code.google.com/p/kosherjava/
			// parser.parseDataFromJavaDoc(new
			// File("src/test/javadoc-example/zmanim/doc"));

			// parses javadoc of jdparser project
			parser.parseDataFromJavaDoc(new File("src/test/javadoc-example/jdparser/doc"));

//			Collection<ClassRepresentation> classRepre = parser
//					.parseDataFromJavaDoc(new File(
//							"src/test/javadoc-example/jdparser/doc.zip"));
			
			//parser.parseDataFromJavaDoc(new File("src/test/javadoc-example/oop-cv3/doc"));
			
			//Collection<ClassRepresentation> classRepre = parser.parseDataFromJavaDoc(new File("src/test/javadoc-example/oop-cv3-manualjavadoc/doc"));					
			//System.out.println(classRepre);

		} catch (JavaDocParsingException e) {

			System.out.println(e);
		}

	}

}
