package henix.htmlpattern;

import net.jcip.annotations.Immutable;

import org.apache.xerces.xni.XMLAttributes;

/**
 * 在 SAX Parser 中匹配一个指定的 node
 *
 * @author henix
 */
@Immutable
public interface NodeMatcher {
	boolean matches(String tag, XMLAttributes attrs);
}
