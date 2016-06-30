package love.image.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class StudentDao {

	private String dbPath;

	public StudentDao(String p) {
		this.dbPath = p;
	}

	/**
	 * Add a new student to the database
	 */
	public boolean create(Student s) throws Exception {
		boolean flag = false;
		if (s != null) {
			Document doc = getDocument();
			Element root = doc.getRootElement();
			Element newElement = root.addElement("student");
			newElement.addAttribute("id", s.getId());
			newElement.addElement("name").setText(s.getName());
			write2xml(doc);
			flag = true;
		}
		return flag;
	}

	/**
	 * delete a student from the xml-database
	 */
	public boolean delete(String id) throws Exception {
		boolean flag = false;
		Document doc = getDocument();
		String xpath = "//student[@id='" + id + "']";
		Element element = (Element) doc.selectSingleNode(xpath);
		if (element != null) {
			element.getParent().remove(element);
			write2xml(doc);
			flag = true;
		} else {
			System.out.println("ID is not existed!");
		}
		return flag;
	}

	/**
	 * search the information of a specified student
	 */
	public void check(String id) throws Exception {
		Document doc = getDocument();
		String xpath = "//student[@id='" + id + "']";
		Element element = (Element) doc.selectSingleNode(xpath);
		if (element != null) {
			System.out.println("stu_id:" + element.attributeValue("id"));
			System.out.println("stu_name:" + element.elementText("name"));
		} else {
			System.out.println("ID is not existed!");
		}
	}

	/**
	 * search the length of db
	 */
	public int length() throws Exception {
		Document doc = getDocument();
		Element rootElement = doc.getRootElement();

		Iterator<Element> elementIterator = rootElement.elementIterator();
		int total = 0;
		while (elementIterator.hasNext()) {
			Element next = elementIterator.next();
			total++;
		}
		System.out.println(total);

		return total;
	}

	/**
	 * search the information of a specified student
	 */
	public List<Student> getall() throws Exception {
		Document doc = getDocument();
		Element rootElement = doc.getRootElement();

		Iterator<Element> elementIterator = rootElement.elementIterator();
		int total = 0;
		while (elementIterator.hasNext()) {
			Element next = elementIterator.next();
			total++;
		}
		System.out.println(total);

		List<Student> ls = new ArrayList<Student>();
		for (int i = 0; i < total; i++) {
			String xpath = "//student[@id='" + (i + 1) + "']";
			Element element = (Element) doc.selectSingleNode(xpath);
			if (element != null) {
				Student s = new Student();
				s.setId(element.attributeValue("id"));
				s.setName(element.elementText("name"));
				ls.add(s);
			} else {
				System.out.println("ID is not existed!");
			}
		}
		return ls;
	}

	/**
	 * update a specified student
	 */
	public boolean update(Student s) throws Exception {
		boolean flag = false;
		Document doc = getDocument();
		String xpath = "//student[@id='" + s.getId() + "']";
		Element element = (Element) doc.selectSingleNode(xpath);
		if (element != null) {
			element.element("name").setText(s.getName());
			write2xml(doc);
			flag = true;
		} else {
			System.out.println("ID is not existed!");
		}
		return flag;
	}

	/*
	 * the public function getDocument() & write2xml
	 */
	private void write2xml(Document doc) throws FileNotFoundException,
			UnsupportedEncodingException, IOException {
		OutputStream os = new FileOutputStream(new File(dbPath));
		OutputFormat format = OutputFormat.createPrettyPrint();
		XMLWriter xw = new XMLWriter(os, format);
		xw.write(doc);
		xw.close();
	}

	private Document getDocument() throws DocumentException {
		SAXReader sr = new SAXReader();

		if (false) {
			File a = new File("abcd.xml");
			try {
				a.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		Document doc = sr.read(new File(dbPath));
		return doc;
	}

}
