package henix.htmlpattern;

import org.apache.xerces.xni.XMLAttributes;

import com.google.common.base.Preconditions;

public class AndNodeMatcher implements NodeMatcher {

	private NodeMatcher[] arSubMatchers;
	private Iterable<NodeMatcher> subMatchers;

	public AndNodeMatcher(NodeMatcher... nodeMatchers) {
		Preconditions.checkArgument(nodeMatchers.length > 0);
		this.arSubMatchers = nodeMatchers;
	}

	public AndNodeMatcher(Iterable<NodeMatcher> subMatchers) {
		this.subMatchers = Preconditions.checkNotNull(subMatchers);
		Preconditions.checkArgument(subMatchers.iterator().hasNext());
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
