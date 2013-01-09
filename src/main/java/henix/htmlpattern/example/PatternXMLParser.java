package henix.htmlpattern.example;

import henix.htmlpattern.HtmlPattern;
import org.apache.xerces.parsers.AbstractSAXParser;
import org.apache.xerces.parsers.XML11Configuration;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;

import com.google.common.base.Preconditions;

public class PatternXMLParser extends AbstractSAXParser{

	private HtmlPattern rootPattern;

	public PatternXMLParser(HtmlPattern rootPattern) {
		super(new XML11Configuration());
		this.rootPattern = Preconditions.checkNotNull(rootPattern);
	}

	@Override
	public void characters(XMLString xmlString, Augmentations augs) throws XNIException {
		rootPattern.characters(xmlString);
	}

	@Override
	public void startElement(QName qName, XMLAttributes attrs, Augmentations augs) throws XNIException {
		rootPattern.startTag(qName.rawname, attrs);
	}

	@Override
	public void endElement(QName qName, Augmentations augs) throws XNIException {
		rootPattern.endTag(qName.rawname);
	}
}
