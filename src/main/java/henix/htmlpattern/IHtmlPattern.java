package henix.htmlpattern;

import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLString;

public interface IHtmlPattern {

	public static enum CollectOption {
		TEXT,
		HTML,
		OWNTEXT,
	}

	public void startTag(String tag, XMLAttributes attrs);
	public void characters(XMLString xmlString);
	public void endTag(String tag);

	public boolean matches(String tag, XMLAttributes attrs);

	/**
	 * textContent
	 */
	public String text();

	/**
	 * innerHTML
	 */
	public String html();

	/**
	 * 此元素自己的 text ，不包括子元素中的 text
	 */
	public String owntext();

	/**
	 * 重置此 HtmlPattern 的状态，以便下一次使用
	 */
	public void reset();
}
