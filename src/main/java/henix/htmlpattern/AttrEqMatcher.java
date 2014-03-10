package henix.htmlpattern;

import org.apache.xerces.xni.XMLAttributes;

public class AttrEqMatcher implements NodeMatcher {

	private final String attrName;
	private final String expectValue;

	/**
	 *
	 * @param attr
	 * @param expect can be null
	 */
	public AttrEqMatcher(String attr, String expect) {
		if (attr == null) throw new NullPointerException();
		this.attrName = attr;
		this.expectValue = expect;
	}

	public boolean matches(String tag, XMLAttributes attrs) {
		final String value = attrs.getValue(attrName);
		if (expectValue == null) {
			return value == null;
		} else {
			return expectValue.equals(value);
		}
	}
}
