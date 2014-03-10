package henix.htmlpattern;

import org.apache.xerces.xni.XMLAttributes;

public class AttrStartMatcher implements NodeMatcher {

	private final String attr;
	private final String prefix;

	public AttrStartMatcher(String attr, String prefix) {
		if (attr == null) throw new NullPointerException();
		if (prefix == null) throw new NullPointerException();
		this.attr = attr;
		this.prefix = prefix;
	}

	public boolean matches(String tag, XMLAttributes attrs) {
		final String value = attrs.getValue(this.attr);
		if (value != null && value.startsWith(this.prefix)) {
			return true;
		}
		return false;
	}
}
