package henix.htmlpattern;

import org.apache.xerces.xni.XMLAttributes;

import com.google.common.base.Preconditions;

public class AttrStartMatcher implements NodeMatcher {

	private String attr;
	private String prefix;

	public AttrStartMatcher(String attr, String prefix) {
		this.attr = Preconditions.checkNotNull(attr);
		this.prefix = Preconditions.checkNotNull(prefix);
	}

	public boolean matches(String tag, XMLAttributes attrs) {
		String value = attrs.getValue(this.attr);
		if (value != null && value.startsWith(this.prefix)) {
			return true;
		}
		return false;
	}
}
