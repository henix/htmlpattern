package henix.htmlpattern;

import org.apache.xerces.xni.XMLAttributes;

import com.google.common.base.Preconditions;

public class OrNodeMatcher implements NodeMatcher {

	private NodeMatcher[] arSubMatchers;
	private Iterable<NodeMatcher> subMatchers;

	public OrNodeMatcher(NodeMatcher... nodeMatchers) {
		Preconditions.checkArgument(nodeMatchers.length > 0);
		this.arSubMatchers = nodeMatchers;
	}

	public OrNodeMatcher(Iterable<NodeMatcher> subMatchers) {
		this.subMatchers = Preconditions.checkNotNull(subMatchers);
		Preconditions.checkArgument(subMatchers.iterator().hasNext());
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
