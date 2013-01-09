package henix.htmlpattern;

import org.apache.xerces.xni.XMLAttributes;

import com.google.common.base.Preconditions;

/**
 * Match a HTML tag
 */
public class TagNameMatcher implements NodeMatcher {

	private String tagName;

	public TagNameMatcher(String tagName) {
		this.tagName = Preconditions.checkNotNull(tagName);
	}

	public boolean matches(String tagName, XMLAttributes attrs) {
		return this.tagName.equalsIgnoreCase(tagName);
	}
}
