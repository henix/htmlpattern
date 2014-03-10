package henix.htmlpattern;

import org.apache.xerces.xni.XMLAttributes;

public class NotNodeMatcher implements NodeMatcher {

	private final NodeMatcher nodeMatcher;

	public NotNodeMatcher(NodeMatcher nodeMatcher) {
		this.nodeMatcher = nodeMatcher;
	}

	public boolean matches(String tag, XMLAttributes attrs) {
		return !nodeMatcher.matches(tag, attrs);
	}
}
