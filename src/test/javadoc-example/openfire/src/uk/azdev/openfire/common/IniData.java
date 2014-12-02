/*
 * IniData - an INI file parser and generator.
 * 
 * Based on QuickIni, written by Mike Wallace (mfwallace at gmail.com).
 * Modified by Iain McGinniss to include support for reading and
 * writing from streams and associated classes.
 * QuickIni is available Mike Wallace's web site: 
 * http://mfwallace.googlepages.com/
 * 
 * Copyright (c) 2006 Mike Wallace.
 * 
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom
 * the Software is furnished to do so, subject to the following
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package uk.azdev.openfire.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 * Class to read and write INI files.
 * 
 * @author Iain McGinniss, Mike Wallace
 */
public final class IniData {

	private String lineFeed = "\r\n";
	private LinkedHashMap<String, LinkedHashMap<String, String>> contents = createNewContents();

	public IniData() {
		// default constructor doesn't need to do anything
	}
	
	public IniData(InputStream stream) throws IOException {
		read(stream);
	}

	/**
	 * Convenience constructor that creates a new QuickIni instance and
	 * reads data from a {@link File}.
	 * 
	 * @param fileName
	 *            the name of the input file
	 * @throws IOException if an error occurs while reading the file
	 * @throws FileNotFoundException if the file does not exist
	 */
	public IniData(File file) throws IOException {
		read(file);
	}
	
	/**
	 * Convenience constructor that creates a new QuickIni instance and
	 * reads data from a file, at the indicated path.
	 * 
	 * @param fileName
	 *            the name of the input file
	 * @throws IOException if an error occurs while reading the file
	 * @throws FileNotFoundException if the file does not exist
	 */
	public IniData(final String fileName) throws IOException {
		this(new File(fileName));
	}
	
	public void reset() {
		// a linked hash map is used to store the order of the sections
		// as found in the original file, or as added
		contents = createNewContents();
	}

	private LinkedHashMap<String, LinkedHashMap<String, String>> createNewContents() {
		return new LinkedHashMap<String, LinkedHashMap<String, String>>(5);
	}

	/**
	 * Process a line of data.
	 * 
	 * @param line
	 *            the line of data from the file
	 * @param currSection
	 *            the name of the current section
	 * @return the name of the current section
	 */
	private String process(final String line, final String currSection) {
		// Check the input
		if ((line == null) || (line.length() < 3)) {
			// It's null or empty, so return
			return currSection;
		}

		// Check for a comment
		if ((line.charAt(0) == ';') || (line.charAt(0) == '#')) {
			// It's a comment, so skip it
			return currSection;
		}

		// Check if the line starts with a '['. If it
		// does, it's a section name
		if (line.charAt(0) == '[') {
			return parseSectionName(line, currSection);
		}
		
		// Check if we're in a section
		if ((currSection == null) || (currSection.length() < 1)) {
			// We're not, so return
			return currSection;
		}

		// Check if it's a property with a value
		final int nEqualIndex = line.indexOf('=');
		if (nEqualIndex <= 0) {
			// No equals sign, or it's the start of the line,
			// so skip the line
			return currSection;
		}

		// Save the length of the line
		final int nLineLen = line.length();
		if (nEqualIndex == (nLineLen - 1)) {
			// The line ends with the equals sign,
			// so skip the line
			return currSection;
		}

		// Get the property and the value
		final String prop = line.substring(0, nEqualIndex).trim();
		final String value = line.substring(nEqualIndex + 1);

		// Check the property and value
		if ((prop.length() < 1) || (value.length() < 1)) {
			// Something is empty, so return it
			return currSection;
		}

		// Get the hashset for this section
		HashMap<String, String> props = contents.get(currSection);

		// See if the property has already been found
		if (props.get(prop) != null) {
			// It's already in the map, so return
			return currSection;
		}

		// Add the property and value
		props.put(prop, value);

		// Return the name of this section
		return currSection;
	}

	private String parseSectionName(final String line, final String currSection) {
		// See if there's a ]
		final int nCloseIndex = line.indexOf(']');
		if (nCloseIndex < 0) {
			// Not found. Ignore the line.
			return currSection;
		}

		// Save the string between the brackets
		String section = line.substring(1, nCloseIndex).trim();

		// Check if the name is non-empty
		if (section.length() < 1) {
			// It's an empty name, so set it to null and return
			return null;
		}

		// See if we already have the section name
		HashMap<String, String> props = contents.get(section);
		if (props == null) {
			// We don't have the name, so save it with an empty
			// (non-null) hashmap for the properties
			contents.put(section, new LinkedHashMap<String, String>(5));
		}

		// Return the name of the current section
		return section;
	}

	/**
	 * Get an integer property.
	 * 
	 * @param sectionName
	 *            the name of the section
	 * @param propertyName
	 *            the name of the property
	 * @return the property value. If the section and/or property name does not exist,
	 * 0 will be returned.
	 */
	public int getIntegerProperty(final String sectionName,
			final String propertyName) {
		return getIntegerProperty(sectionName, propertyName, 0);
	}

	/**
	 * Get an integer property.
	 * 
	 * @param sectionName
	 *            the name of the section
	 * @param propertyName
	 *            the name of the property
	 * @param defaultValue
	 *            the default value to return if the section and/or property does not exist.
	 * @return the property value. If the section and/or property name does not exist,
	 * defaultValue will be returned.
	 */
	public int getIntegerProperty(final String sectionName,
			final String propertyName, final int defaultValue) {
		// Get the string property
		final String s = getStringProperty(sectionName, propertyName);

		// Check for an illegal return value
		if ((s == null || (s.length() < 1))) {
			return defaultValue;
		}

		// Declare our variable that gets returned. Set it to
		// the default value in case an exception is thrown while
		// parsing the string.
		int val = defaultValue;

		// Parse the string as a number
		try {
			// Parse the string
			int tempValue = Integer.parseInt(s);

			// If we reach this point, it was a success
			val = tempValue;
		} catch (NumberFormatException nfe) {
			val = defaultValue;
		}

		// Return the value
		return val;
	}

	/**
	 * Returns the property value for the specified section and property.
	 * 
	 * @param sectionName
	 *            the name of the section
	 * @param propertyName
	 *            the name of the property
	 * @return the property value. If the section and/or property name is does not exist,
	 * false will be returned.
	 */
	public boolean getBooleanProperty(final String sectionName,
			final String propertyName) {
		return getBooleanProperty(sectionName, propertyName, false);
	}

	/**
	 * Returns the property value for the specified section and property.
	 * 
	 * @param sectionName
	 *            the name of the section
	 * @param propertyName
	 *            the name of the property
	 * @param defaultValue
	 *            the default value to return if the section and/or property does not exist.
	 * @return the property value. If the section and/or property name is does not exist,
	 * defaultValue will be returned.
	 */
	public boolean getBooleanProperty(final String sectionName,
			final String propertyName, final boolean defaultValue) {
		// Get the string from the input file
		final String str = getStringProperty(sectionName, propertyName);

		// Check if the returned string is null
		if ((str == null) || (str.length() < 1)) {
			// It is, so return the default value
			return defaultValue;
		}

		// Declare our boolean variable that gets returned.
		boolean val = defaultValue;

		// Convert the string into a boolean
		if ((str.equals("1")) || (str.equalsIgnoreCase("true"))) {
			// Set val to true for certain string values
			val = true;
		}

		// Return the boolean value
		return val;
	}

	/**
	 * Returns the property value for the specified section and property.
	 * 
	 * @param sectionName
	 *            the name of the section
	 * @param propertyName
	 *            the name of the property
	 * @return the property value. If the section and/or property name is does not exist,
	 * 0L will be returned.
	 */
	public long getLongProperty(final String sectionName,
			final String propertyName) {
		return getLongProperty(sectionName, propertyName, 0L);
	}

	/**
	 * Returns the property value for the specified section and property.
	 * 
	 * @param sectionName
	 *            the name of the section
	 * @param propertyName
	 *            the name of the property
	 * @param defaultValue
	 *            the default value to return, if it's not found
	 * @return the property value. If the section and/or property name is does not exist,
	 * defaultValue will be returned.
	 */
	public long getLongProperty(final String sectionName,
			final String propertyName, final long defaultValue) {
		// Get the string from the input file
		final String str = getStringProperty(sectionName, propertyName);

		// Check if the returned string is null
		if (str == null) {
			// It is, so return the default value
			return defaultValue;
		}

		// Declare our variable that gets returned. Set it to
		// the default value in case an exception is thrown while
		// parsing the string.
		long val = defaultValue;

		// Parse the string as a number
		try {
			// Parse the string
			long tempValue = Long.parseLong(str);

			// If we reach this point, it was a success
			val = tempValue;
		} catch (NumberFormatException nfe) {
			val = defaultValue;
		}

		// Return the value
		return val;
	}

	/**
	 * Returns the property value for the specified section and property.
	 * 
	 * @param sectionName
	 *            the name of the section
	 * @param propertyName
	 *            the name of the property
	 * @return the property value. If the section and/or property name is does not exist,
	 * 0.0 will be returned.
	 */
	public double getDoubleProperty(final String sectionName,
			final String propertyName) {
		return getDoubleProperty(sectionName, propertyName, 0.0);
	}

	/**
	 * Returns the property value for the specified section and property.
	 * 
	 * @param sectionName
	 *            the name of the section
	 * @param propertyName
	 *            the name of the property
	 * @param defaultValue
	 *            the default value to return, if it's not found
	 * @return the property value. If the section and/or property name is does not exist,
	 * defaultValue will be returned.
	 */
	public double getDoubleProperty(final String sectionName,
			final String propertyName, final double defaultValue) {
		// Get the string from the input file
		final String str = getStringProperty(sectionName, propertyName);

		// Check if the returned string is null
		if ((str == null) || (str.length() < 1)) {
			// It is, so return the default value
			return defaultValue;
		}

		// Declare our variable that gets returned. Set it to
		// the default value in case an exception is thrown while
		// parsing the string.
		double val = defaultValue;

		// Parse the string as a number
		try {
			// Parse the string
			double tempValue = Double.parseDouble(str);

			// If we reach this point, it was a success
			val = tempValue;
		} catch (NumberFormatException nfe) {
			val = defaultValue;
		}

		// Return the value
		return val;
	}

	/**
	 * Returns the property value for the specified section and property.
	 * 
	 * @param sectionName
	 *            the name of the section
	 * @param propertyName
	 *            the name of the property
	 * @return the property value. If the section and/or property name is does not exist,
	 * {@code new Date(0)} will be returned.
	 */
	public Date getDateProperty(final String sectionName,
			final String propertyName) {
		return getDateProperty(sectionName, propertyName, new Date(0));
	}
	
	/**
	 * Returns the property value for the specified section and property. If the
	 * property is not found, defaultValue is returned instead.
	 * 
	 * @param sectionName
	 *            the name of the section
	 * @param propertyName
	 *            the name of the property
	 * @param defaultValue
	 *            the default value to return, if it's not found
	 * @return the property value. If the section and/or property name is does not exist,
	 * defaultValue will be returned.
	 */
	public Date getDateProperty(final String sectionName, final String propertyName, final Date defaultValue) {
		// Get the string from the input file
		final String str = getStringProperty(sectionName, propertyName);

		// Check if the returned string is null
		if ((str == null) || (str.length() < 1)) {
			// It is, so return the default value
			return defaultValue;
		}

		// Declare our variable that gets returned. Set it to
		// the default value in case an exception is thrown while
		// parsing the string.
		Date date = defaultValue;

		// Parse the string as a number
		try {
			// Parse the string
			long tempValue = Long.parseLong(str);

			// If we reach this point, the long was parsed correctly
			if (tempValue >= 0) {
				// Create a Date object using the temp value
				date = new Date(tempValue);
			}
		} catch (NumberFormatException nfe) {
			date = defaultValue;
		}

		// Return the value
		return date;
	}

	/**
	 * Returns the value for the property in the section.
	 * 
	 * @param sectionName
	 *            the name of the section
	 * @param propertyName
	 *            the name of the property
	 * @return the property value. If the section and/or property name is does not exist,
	 * null will be returned.
	 */
	public String getStringProperty(final String sectionName,
			final String propertyName) {
		return getStringProperty(sectionName, propertyName, null);
	}

	/**
	 * Returns the value for the property in the section.
	 * 
	 * @param sectionName
	 *            the name of the section
	 * @param propertyName
	 *            the name of the property
	 * @param defaultValue
	 *            the default value to return if not found
	 * @return the property value. If the section and/or property name is does not exist,
	 * defaultValue will be returned.
	 */
	public String getStringProperty(final String sectionName,
			final String propertyName, final String defaultValue) {

		if ((sectionName == null) || (sectionName.length() < 1)) {
			throw new IllegalArgumentException("null or empty section name provided as an argument");
		}
		if((propertyName == null) || (propertyName.length() < 1)) {
			throw new IllegalArgumentException("null or empty property name provided as an argument");
		}

		// Get the property list for the section
		HashMap<String, String> props = contents.get(sectionName);
		if ((props == null) || (props.size() < 1)) {
			// Return
			return defaultValue;
		}

		// Get the value
		String value = props.get(propertyName);
		if (value == null) {
			// It was null, so it wasn't found
			return defaultValue;
		}

		// Return the value that was found
		return value;
	}

	/**
	 * Writes the propertyName=value to the specified section.
	 * 
	 * @param sectionName
	 *            the name of the section
	 * @param propertyName
	 *            the name of the property
	 * @param value
	 *            the property value
	 */
	public void setBooleanProperty(final String sectionName,
			final String propertyName, final boolean value) {
		// Save the value as a string
		final String sValue = Boolean.toString(value);

		// Pass the string to setStringProperty
		setStringProperty(sectionName, propertyName, sValue);
	}

	/**
	 * Writes the propertyName=value to the specified section.
	 * 
	 * @param sectionName
	 *            the name of the section
	 * @param propertyName
	 *            the name of the property
	 * @param value
	 *            the property value
	 */
	public void setIntegerProperty(final String sectionName,
			final String propertyName, final int value) {
		// Save the value as a string
		final String sValue = Integer.toString(value);

		// Pass the string to setStringProperty
		setStringProperty(sectionName, propertyName, sValue);
	}

	/**
	 * Writes the propertyName=value to the specified section.
	 * 
	 * @param sectionName
	 *            the name of the section
	 * @param propertyName
	 *            the name of the property
	 * @param value
	 *            the property value
	 */
	public void setLongProperty(final String sectionName,
			final String propertyName, final long value) {
		// Save the value as a string
		final String sValue = Long.toString(value);

		// Pass the string to setStringProperty
		setStringProperty(sectionName, propertyName, sValue);
	}

	/**
	 * Writes the propertyName=value to the specified section.
	 * 
	 * @param sectionName
	 *            the name of the section
	 * @param propertyName
	 *            the name of the property
	 * @param value
	 *            the property value
	 * @return the success of the operation
	 */
	public void setDoubleProperty(final String sectionName,
			final String propertyName, final double value) {
		// Save the value as a string
		final String sValue = Double.toString(value);

		// Pass the string to setStringProperty
		setStringProperty(sectionName, propertyName, sValue);
	}

	/**
	 * Writes the propertyName=value to the specified section.
	 * 
	 * @param sectionName
	 *            the name of the section
	 * @param propertyName
	 *            the name of the property
	 * @param value
	 *            the property value
	 */
	public void setDateProperty(final String sectionName,
			final String propertyName, final Date value) {
		// Check the date
		if (value == null) {
			throw new IllegalArgumentException("null value provided as an argument");
		}

		// Convert the date to milliseconds
		final long lDateInMS = value.getTime();

		// Convert the milliseconds to a string
		final String sDate = Long.toString(lDateInMS);

		// Pass the milliseconds string to setStringProperty
		setStringProperty(sectionName, propertyName, sDate);
	}

	/**
	 * Set the string property.
	 * 
	 * @param sectionName
	 *            the name of the section
	 * @param propertyName
	 *            the name of the property
	 * @param value
	 *            the value to set
	 */
	public void setStringProperty(final String sectionName,
			final String propertyName, final String value) {

		// Check the input
		if ((sectionName == null) || (sectionName.length() < 1)) {
			throw new IllegalArgumentException("null or empty section name provided as an argument");
		}
		
		if((propertyName == null) || (propertyName.length() < 1)) {
			throw new IllegalArgumentException("null or empty property name provided as an argument");
		}
		
		if(value == null) {
			throw new IllegalArgumentException("null value provided as an argument");
		}

		LinkedHashMap<String, String> section = contents.get(sectionName);
		if (section == null) {

			// Declare a holder for the property
			section = new LinkedHashMap<String, String>(5);

			// Store the property and value
			section.put(propertyName, value);

			// Add the property to the section
			contents.put(sectionName, section);

			return;
		}
		
		// Add the value
		section.put(propertyName, value);
	}
	
	public void removePropertyFromSection(String sectionName, String propertyName) {
		LinkedHashMap<String, String> section = contents.get(sectionName);
		if(section == null) {
			return;
		}
		
		section.remove(propertyName);
	}
	
	public void removeSection(String sectionName) {
		contents.remove(sectionName);
	}

	/**
	 * Reads the specified {@link File}.
	 * @param file the file to read.
	 * @throws IOException if an error occurred while reading the file.
	 * @throws FileNotFoundException if the specified file does not exist.
	 */
	public void read(File file) throws IOException, FileNotFoundException {
		FileInputStream stream = null;
		try {
			stream = new FileInputStream(file);
			read(stream);
		} finally {
			if(stream != null) {
				stream.close();
			}
		}
	}
	
	/**
	 * Reads ini data from the specified input stream using the default character set
	 * for the current platform. Note that this method will not close
	 * the input stream - this is left to the caller.
	 * @param stream the {@link InputStream} to read.
	 * @throws IOException if an error occurred while reading the stream.
	 */
	public void read(InputStream stream) throws IOException {
		read(new InputStreamReader(stream));
	}
	
	/**
	 * Reads ini data from the specificed Reader instance. Note that this method
	 * will not close the Reader - this is left to the caller.
	 * @param reader the {@link Reader} from which ini data will be read.
	 * @throws IOException if an error occurred while reading from the Reader.
	 */
	public void read(Reader reader) throws IOException {
		read(new BufferedReader(reader));
	}
	
	/**
	 * Reads ini data from the specificed BufferedReader instance. Note that this method
	 * will not close the Reader - this is left to the caller.
	 * @param reader the {@link BufferedReader} from which ini data will be read.
	 * @throws IOException if an error occurred while reading from the Reader.
	 */
	public void read(BufferedReader in) throws IOException {
		// This will hold the name of the current section
		String currSection = null;

		// Clear the content data
		reset();

		// Read the file and store the data
		String str;
		while ((str = in.readLine()) != null) {
			currSection = process(str, currSection);
		}
	}
	
	/**
	 * Writes the ini data to the specified {@link File}. The sections and properties
	 * will be written in the order they were added to this QuickIni instance.
	 * @param file the file which the ini data will be written to.
	 * @throws IOException if an error occurred while writing to the file.
	 * @throws FileNotFoundException if the file exists but is a directory rather than 
	 * a regular file, does not exist but cannot be created, or cannot be opened for 
	 * any other reason.
	 */
	public void write(File file) throws IOException, FileNotFoundException {
		write(new FileOutputStream(file));
	}

	/**
	 * Writes the ini data to the specified OutputStream. The default character set
	 * will be used. The sections and properties will be written in the order they 
	 * were added to this QuickIni instance. Note that the stream will not be closed,
	 * this is left to the caller.
	 * @param stream the stream to which the ini data will be written.
	 * @throws IOException if an error occurred while writing to the stream.
	 */
	public void write(OutputStream stream) throws IOException {
		write(new OutputStreamWriter(stream));
	}

	/**
	 * Writes the ini data to the specified Writer. The sections and properties 
	 * will be written in the order they were added to this QuickIni instance.
	 * @param writer the writer which will be used to write to the writer.
	 * Note that the writer will not be closed, this is left to the caller.
	 * @throws IOException if an error occurred while writing to the writer.
	 */
	public void write(Writer writer) throws IOException {
		write(new BufferedWriter(writer));
	}

	/**
	 * Writes the ini data to the specified BufferedWriter. The sections and properties 
	 * will be written in the order they were added to this QuickIni instance.
	 * @param writer the writer which will be used to write to the writer.
	 * Note that the writer will not be closed, this is left to the caller.
	 * @throws IOException if an error occurred while writing to the writer.
	 */
	public void write(BufferedWriter out) throws IOException {

		Set<String> sects = contents.keySet();
		for (String sect : sects) {
			// Write the section name
			out.write("[" + sect + "]" + lineFeed);

			// Get the list of values
			HashMap<String, String> values = contents.get(sect);

			// Get the set of properties for this section
			Set<String> props = values.keySet();
			for (String prop : props) {
				// Get the value for this property
				String value = values.get(prop);

				// Add to the string
				out.write(prop + "=" + value + lineFeed);
			}

			// Add a blank line after the section
			out.write(lineFeed);
		}
		
		out.flush();
	}

	/**
	 * Return the names of all sections.
	 * 
	 * @return the names of all sections
	 */
	public List<String> getAllSectionNames() {

		// Check if it has any data
		if ((contents == null) || (contents.size() < 1)) {
			return new ArrayList<String>(0);
		}

		// Get all the section names
		List<String> names = new ArrayList<String>(contents.size());

		// Iterate over the contents
		Set<String> sects = contents.keySet();
		for (String sect : sects) {
			names.add(sect);
		}

		// Return the list of names
		return names;
	}

	/**
	 * Return the list of all properties for this section.
	 * 
	 * @param sectionName
	 *            the section to get the properties from
	 * @return the list of properties in the section
	 */
	public List<String> getAllPropertyNames(final String sectionName) {

		// Check if it has any data, and check
		// the requested section name
		if ((contents == null) || (contents.size() < 1)
				|| (sectionName == null) || (sectionName.length() < 1)) {
			// Return an empty list
			return (new ArrayList<String>(0));
		}

		// Get the hashmap for this section
		HashMap<String, String> properties = contents.get(sectionName);
		if ((properties == null) || (properties.size() < 1)) {
			// Return an empty list
			return (new ArrayList<String>(0));
		}

		// Declare the array to return
		final int size = properties.size();
		List<String> propList = new ArrayList<String>(size);

		// Iterate over the properties
		Iterator<String> propSet = properties.keySet().iterator();
		while (propSet.hasNext()) {
			// Copy the current entry
			propList.add(propSet.next());
		}

		// Return the list
		return propList;
	}
	
	public boolean hasSection(final String sectionName) {
		return contents.containsKey(sectionName);
	}
	
	public boolean hasPropertyInSection(final String sectionName, final String propertyName) {
		if(!contents.containsKey(sectionName)) {
			return false;
		}
		
		HashMap<String, String> sectionMap = contents.get(sectionName);
		return sectionMap.containsKey(propertyName);
	}
	
	public String getLineFeedString() {
		return lineFeed;
	}
	
	public void setLineFeedString(String lineFeed) {
		this.lineFeed = lineFeed;
	}
}
