package henix.htmlpattern;

import org.apache.xerces.xni.XMLAttributes;

/**
 * Match a HTML tag
 */
public class TagNameMatcher implements NodeMatcher {

	private final String tagName;

	public TagNameMatcher(String tagName) {
		this.tagName = tagName;
	}

	public boolean matches(String tagName, XMLAttributes attrs) {
		return this.tagName.equalsIgnoreCase(tagName);
	}
}
