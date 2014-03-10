package henix.htmlpattern;

import org.apache.xerces.xni.XMLAttributes;

public class OrNodeMatcher implements NodeMatcher {

	private NodeMatcher[] arSubMatchers;
	private Iterable<NodeMatcher> subMatchers;

	public OrNodeMatcher(NodeMatcher... nodeMatchers) {
		this.arSubMatchers = nodeMatchers;
	}

	public OrNodeMatcher(Iterable<NodeMatcher> subMatchers) {
		this.subMatchers = subMatchers;
	}

	public boolean matches(String tag, XMLAttributes attrs) {
		if (arSubMatchers != null) {
			for (NodeMatcher nodeMatcher : arSubMatchers) {
				if (nodeMatcher.matches(tag, attrs)) {
					return true;
				}
			}
		} else if (subMatchers != null) {
			for (NodeMatcher nodeMatcher : subMatchers) {
				if (nodeMatcher.matches(tag, attrs)) {
					return true;
				}
			}
		}
		return false;
	}
}
