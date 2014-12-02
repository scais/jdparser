package cz.zcu.validator.jdparser.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Design pattern - Library class.
 * 
 * Helping class for getting files with JavaDoc information from given folder.
 * 
 * @author Stepan Cais, scais@kiv.zcu.cz, 3.12.2012
 */
public class JavaDocFilesFinder {

    /**
     * Name of file, where are saved information about all classes. It contains
     * the same informations as {@code ALLCLASSES_NOFRAME_HTML}.
     */
    private static final String ALLCLASSES_FRAME_HTML = "allclasses-frame.html";

    /**
     * Name of file, where are saved information about all classes. It contains
     * the same informations as {@code ALLCLASSES_FRAME_HTML}.
     */
    private static final String ALLCLASSES_NOFRAME_HTML = "allclasses-noframe.html";

    /**
     * If {@code ALLCLASSES_FRAME_HTML} or {@code ALLCLASSES_NOFRAME_HTML} are
     * found, both of them contain information about classes. It is not
     * important which file is used for gathering information.
     */
    private static final int DEFAULT_CLASS_INF_FILE_INDEX = 0;

    /**
     * String constant for html element 'a'.
     */
    private static final String HTML_TAG_A = "a";

    /**
     * String constant for html element 'href'.
     */
    private static final String HTML_TAG_HREF = "href";

    /**
     * Finds all JavaDoc class files in given folder. Firstly check, whether
     * {@code ALLCLASSES_FRAME_HTML} or {@code ALLCLASSES_NOFRAME_HTML} are in
     * given folder. If so, opens one of this file and extract information about
     * class html files into list of files and returns this list.
     * 
     * @param javaDocDir
     *            Folder with JavaDoc files.
     * @return Collection of files. Every file in this collection is real html
     *         file, which represents one class.
     * 
     * @throws JavaDocParsingException
     *             If {@code ALLCLASSES_FRAME_HTML} or
     *             {@code ALLCLASSES_NOFRAME_HTML} do not exist in given folder,
     *             or any exception during reading these files occur, or if
     *             files mentioned in these files do not exist, this exception
     *             is thrown.
     */
    public static Collection<File> getJavaDocClassFiles(File javaDocDir) throws JavaDocParsingException {

        List<File> classInformationFiles = new ArrayList<File>(2);
        find(javaDocDir, classInformationFiles);

        if (classInformationFiles.isEmpty()) {

            throw new JavaDocParsingException("Files " + ALLCLASSES_FRAME_HTML + " and " + ALLCLASSES_NOFRAME_HTML + " do not exist in "
                    + javaDocDir.getAbsolutePath() + " and it's subdirectories.");
        }

        Document doc = null;

        try {

            doc = Jsoup.parse(classInformationFiles.get(DEFAULT_CLASS_INF_FILE_INDEX), "UTF-8");

        } catch (IOException e) {

            throw new JavaDocClassFilesNotFoundException("Exception during parsing " + ALLCLASSES_FRAME_HTML + " or " + ALLCLASSES_NOFRAME_HTML, e);
        }

        Collection<File> javaDocClassFiles = new ArrayList<File>();

        Elements indexContainerElements = doc.getElementsByTag(HTML_TAG_A);
        for (Element elements : indexContainerElements) {

            javaDocClassFiles.add(new File(javaDocDir + "/" + elements.attributes().get(HTML_TAG_HREF)));
        }

        return javaDocClassFiles;
    }

    /**
     * Recursively goes through all folders of given folder and looks after
     * {@code ALLCLASSES_FRAME_HTML} or {@code ALLCLASSES_NOFRAME_HTML} files.
     * 
     * @param adr
     *            Parent folder, all subfolders of this folder will be searched.
     * @param list
     *            List with found files.
     */
    private static void find(File adr, List<File> list) {

        for (File f : adr.listFiles()) {

            if (f.getName().equals(ALLCLASSES_FRAME_HTML) || f.getName().equals(ALLCLASSES_NOFRAME_HTML)) {

                list.add(f);
            }

            if (f.isDirectory()) {

                find(f, list);
            }
        }
    }
}
