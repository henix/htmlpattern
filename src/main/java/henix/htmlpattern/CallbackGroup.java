package henix.htmlpattern;

import org.apache.xerces.xni.XMLAttributes;

public interface CallbackGroup {
	public void start(String $tag, XMLAttributes $attrs);
	public void end(IHtmlPattern $);
}
