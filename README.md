# htmlpattern

HTML/XML pattern matching based on Apache Xerces's `AbstractSAXParser`

## When to use htmlpattern?

* extract info from html / xml (into the form of your own data structure)

## when not use htmlpattern?

* remove some node from a html / xml , and return result html / xml

## What's the difference between htmlpattern and DOM API (e.g. jsoup)?

For example, you want to extract info from follow pieces of pom.xml:

```xml
	<dependencies>
		<dependency>
			<groupId>org.apache.commons</groupId>
			<artifactId>commons-lang3</artifactId>
			<version>3.1</version>
		</dependency>
		<dependency>
			<groupId>net.jcip</groupId>
			<artifactId>jcip-annotations</artifactId>
			<version>1.0</version>
		</dependency>

		<dependency>
			<groupId>com.google.guava</groupId>
			<artifactId>guava</artifactId>
			<version>14.0-rc1</version>
		</dependency>

		<dependency>
			<groupId>xerces</groupId>
			<artifactId>xercesImpl</artifactId>
			<version>2.10.0</version>
		</dependency>
	</dependencies>
```

You want to transform it into following data structure (expressed in json):

```js
dependencies: [
	{
		groupId: "org.apache.commons",
		artifactId: "commons-lang3",
		version: "3.1"
	},
	{
		groupId: "net.jcip",
		artifactId: "jcip-annotations",
		version: "1.0"
	},
	...
]
```

How to do it in normal DOM API (jsoup) ?

```java
Elements dependencies = document.getElementsByTagName("dependency");
for (Element dependency: dependencies) {
	Element groupId = dependency.getElementsByTagName("groupId").child(0);
	...
}
```

It's totally imperative.

What if you define a HtmlPattern ?

```java
	HtmlPattern rootPattern = new HtmlPattern("project")
		.child(new HtmlPattern("dependencies")
			.child(new HtmlPattern("dependency")
				.child(new HtmlPattern("groupId"))
				.child(new HtmlPattern("artifactId"))
				.child(new HtmlPattern("version"))
			)
		)
	;
```

The code structured just as how the XML structured. And it's declarative.

A complete example (including callback): [https://github.com/henix/htmlpattern/blob/master/src/main/java/henix/htmlpattern/example/PomParser.java](https://github.com/henix/htmlpattern/blob/master/src/main/java/henix/htmlpattern/example/PomParser.java)
