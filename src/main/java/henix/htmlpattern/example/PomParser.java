package henix.htmlpattern.example;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.EnumSet;

import org.apache.xerces.xni.XMLAttributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import henix.htmlpattern.HtmlPattern;
import henix.htmlpattern.IHtmlPattern.CollectOption;
import henix.htmlpattern.NopCallbackGroup;
import henix.htmlpattern.example.Pom.Dependency;

public class PomParser {

	private HtmlPattern rootPattern;

	private Pom pom;

	private Dependency curDependency;

	public PomParser() {
		rootPattern = new HtmlPattern("project")
			.child(new HtmlPattern("name", EnumSet.of(CollectOption.OWNTEXT), new NopCallbackGroup(){
				@Override
				public void end(HtmlPattern $) {
					pom.name = $.owntext();
				}
			}))
			.child(new HtmlPattern("dependencies", new NopCallbackGroup(){
				@Override
				public void start(String $tag, XMLAttributes $attrs) {
					pom.dependencies = new ArrayList<Pom.Dependency>();
				}
			})
				.child(new HtmlPattern("dependency", new NopCallbackGroup(){
					@Override
					public void start(String $tag, XMLAttributes $attrs) {
						curDependency = new Dependency();
					}
					@Override
					public void end(HtmlPattern $) {
						pom.dependencies.add(curDependency);
						curDependency = null;
					}
				})
					.child(new HtmlPattern("groupId", EnumSet.of(CollectOption.OWNTEXT), new NopCallbackGroup(){
						@Override
						public void end(HtmlPattern $) {
							curDependency.groupId = $.owntext();
						}
					}))
					.child(new HtmlPattern("artifactId", EnumSet.of(CollectOption.OWNTEXT), new NopCallbackGroup(){
						@Override
						public void end(HtmlPattern $) {
							curDependency.artifactId = $.owntext();
						}
					}))
					.child(new HtmlPattern("version", EnumSet.of(CollectOption.OWNTEXT), new NopCallbackGroup(){
						@Override
						public void end(HtmlPattern $) {
							curDependency.version = $.owntext();
						}
					}))
				)
			)
		;
	}

	public Pom parse(Reader input) throws SAXException, IOException {
		pom = new Pom();

		new PatternXMLParser(rootPattern).parse(new InputSource(input));
		rootPattern.reset();

		return pom;
	}
}
