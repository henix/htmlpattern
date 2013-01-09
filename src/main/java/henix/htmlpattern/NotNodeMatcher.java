package henix.htmlpattern;

import org.apache.xerces.xni.XMLAttributes;

import com.google.common.base.Preconditions;

public class NotNodeMatcher implements NodeMatcher {

	private NodeMatcher nodeMatcher;

	public NotNodeMatcher(NodeMatcher nodeMatcher) {
		this.nodeMatcher = Preconditions.checkNotNull(nodeMatcher);
	}

	public boolean matches(String tag, XMLAttributes attrs) {
		return !nodeMatcher.matches(tag, attrs);
	}
}
