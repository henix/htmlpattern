package henix.htmlpattern.example;

import java.util.List;

/**
 * A simple struct for pom.xml
 */
public class Pom {

	public String name;

	public List<Dependency> dependencies;

	public static class Dependency {
		public String groupId;
		public String artifactId;
		public String version;
	}
}
