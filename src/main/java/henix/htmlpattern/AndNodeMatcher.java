package henix.htmlpattern;

import org.apache.xerces.xni.XMLAttributes;

public class AndNodeMatcher implements NodeMatcher {

	private NodeMatcher[] arSubMatchers;
	private Iterable<NodeMatcher> subMatchers;

	public AndNodeMatcher(NodeMatcher... nodeMatchers) {
		this.arSubMatchers = nodeMatchers;
	}

	public AndNodeMatcher(Iterable<NodeMatcher> subMatchers) {
		this.subMatchers = subMatchers;
	}

	public boolean matches(String tag, XMLAttributes attrs) {
		if (arSubMatchers != null) {
			for (NodeMatcher nodeMatcher : arSubMatchers) {
				if (!nodeMatcher.matches(tag, attrs)) {
					return false;
				}
			}
		} else if (subMatchers != null) {
			for (NodeMatcher nodeMatcher : subMatchers) {
				if (!nodeMatcher.matches(tag, attrs)) {
					return false;
				}
			}
		}
		return true;
	}
}
