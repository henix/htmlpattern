package henix.htmlpattern;

import org.apache.xerces.xni.XMLAttributes;

import com.google.common.base.Preconditions;

public class AttrEqMatcher implements NodeMatcher {

	private String attrName;
	private String expectValue;

	/**
	 *
	 * @param attr
	 * @param expect can be null
	 */
	public AttrEqMatcher(String attr, String expect) {
		this.attrName = Preconditions.checkNotNull(attr);
		this.expectValue = expect;
	}

	public boolean matches(String tag, XMLAttributes attrs) {
		String value = attrs.getValue(attrName);
		if (expectValue == null) {
			return value == null;
		} else {
			return expectValue.equals(value);
		}
	}
}
