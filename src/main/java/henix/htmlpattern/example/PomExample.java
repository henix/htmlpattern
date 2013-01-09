package henix.htmlpattern.example;

import henix.htmlpattern.example.Pom.Dependency;

import java.io.IOException;
import java.io.StringReader;

import org.xml.sax.SAXException;

public class PomExample {

	public static void main(String[] args) throws SAXException, IOException {
		PomParser pomParser = new PomParser();

		Pom pom = pomParser.parse(new StringReader(
"<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
"\txmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
"\txsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
"\t<modelVersion>4.0.0</modelVersion>\n" +
"\n" +
"\t<groupId>henix</groupId>\n" +
"\t<artifactId>htmlpattern</artifactId>\n" +
"\t<version>1.0</version>\n" +
"\t<packaging>jar</packaging>\n" +
"\n" +
"\t<name>htmlpattern</name>\n" +
"\t<url>https://github.com/henix/htmlpattern</url>\n" +
"\n" +
"\t<dependencies>\n" +
"\t\t<dependency>\n" +
"\t\t\t<groupId>org.apache.commons</groupId>\n" +
"\t\t\t<artifactId>commons-lang3</artifactId>\n" +
"\t\t\t<version>3.1</version>\n" +
"\t\t</dependency>\n" +
"\t\t<dependency>\n" +
"\t\t\t<groupId>net.jcip</groupId>\n" +
"\t\t\t<artifactId>jcip-annotations</artifactId>\n" +
"\t\t\t<version>1.0</version>\n" +
"\t\t</dependency>\n" +
"\n" +
"\t\t<dependency>\n" +
"\t\t\t<groupId>com.google.guava</groupId>\n" +
"\t\t\t<artifactId>guava</artifactId>\n" +
"\t\t\t<version>14.0-rc1</version>\n" +
"\t\t</dependency>\n" +
"\n" +
"\t\t<dependency>\n" +
"\t\t\t<groupId>xerces</groupId>\n" +
"\t\t\t<artifactId>xercesImpl</artifactId>\n" +
"\t\t\t<version>2.10.0</version>\n" +
"\t\t</dependency>\n" +
"\t</dependencies>\n" +
"\n" +
"\t<build>\n" +
"\t\t<finalName>htmlpattern</finalName>\n" +
"\n" +
"\t\t<plugins>\n" +
"\t\t\t<plugin>\n" +
"\t\t\t\t<groupId>org.apache.maven.plugins</groupId>\n" +
"\t\t\t\t<artifactId>maven-compiler-plugin</artifactId>\n" +
"\t\t\t\t<version>2.5.1</version>\n" +
"\t\t\t\t<configuration>\n" +
"\t\t\t\t\t<encoding>UTF-8</encoding>\n" +
"\t\t\t\t</configuration>\n" +
"\t\t\t</plugin>\n" +
"\t\t</plugins>\n" +
"\t</build>\n" +
"</project>"
));

		System.out.println("name: " + pom.name);
		for (Dependency dependency : pom.dependencies) {
			System.out.println(dependency.groupId + ":" + dependency.artifactId + ":" + dependency.version);
		}
	}
}
