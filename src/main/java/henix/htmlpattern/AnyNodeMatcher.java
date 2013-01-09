package henix.htmlpattern;

import org.apache.xerces.xni.XMLAttributes;

public class AnyNodeMatcher implements NodeMatcher {

	public static AnyNodeMatcher instance = new AnyNodeMatcher();

	public boolean matches(String tag, XMLAttributes attrs) {
		return true;
	}
}
