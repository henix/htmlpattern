package henix.htmlpattern;

import org.apache.xerces.xni.XMLAttributes;

import com.google.common.base.Preconditions;

public class ClassNameMatcher implements NodeMatcher {

	private String className;

	public ClassNameMatcher(String className) {
		this.className = Preconditions.checkNotNull(className);
	}

	public boolean matches(String tag, XMLAttributes attrs) {
		String clazz = attrs.getValue("class");
		if (clazz != null) {
			int start = clazz.indexOf(className);
			if (start != -1) {
				int end = start + className.length();
				if ((start == 0 || clazz.charAt(start - 1) == ' ') && (end == clazz.length() || clazz.charAt(end) == ' ')) {
					return true;
				}
			}
		}
		return false;
	}
}
