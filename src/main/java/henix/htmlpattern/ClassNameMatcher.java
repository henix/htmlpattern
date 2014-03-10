package henix.htmlpattern;

import org.apache.xerces.xni.XMLAttributes;

public class ClassNameMatcher implements NodeMatcher {

	private final String className;

	public ClassNameMatcher(String className) {
		if (className == null) throw new NullPointerException();
		this.className = className;
	}

	public boolean matches(String tag, XMLAttributes attrs) {
		final String clazz = attrs.getValue("class");
		if (clazz != null) {
			final int start = clazz.indexOf(className);
			if (start != -1) {
				final int end = start + className.length();
				if ((start == 0 || clazz.charAt(start - 1) == ' ') && (end == clazz.length() || clazz.charAt(end) == ' ')) {
					return true;
				}
			}
		}
		return false;
	}
}
