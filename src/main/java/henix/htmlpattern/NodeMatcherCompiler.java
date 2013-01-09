package henix.htmlpattern;

import java.util.ArrayList;
import org.apache.commons.lang3.CharUtils;
import com.google.common.base.Preconditions;

/**
 * 将表示 css query 的字符串编译成 NodeMatcher
 *
 * 一个递归下降的 PEG (Parsing Expression Grammar) parser，参考 [Mouse](http://www.romanredz.se/freesoft.htm) 实现
 *
 * @author henix
 */
public class NodeMatcherCompiler {

	private String input;

	private int pos;
	private int endpos;

	public NodeMatcherCompiler(String input) {
		this.input = Preconditions.checkNotNull(input);
		pos = 0;
		endpos = input.length();
	}

	boolean next(char ch) {
		if (pos < endpos && input.charAt(pos) == ch) {
			pos++;
			return true;
		}
		return false;
	}

	boolean nextNot(char ch) {
		if (pos < endpos && input.charAt(pos) != ch) {
			pos++;
			return true;
		}
		return false;
	}

	/**
	 * !_
	 */
	boolean aheadNot() {
		if (pos < endpos) {
			return false;
		}
		return true;
	}

	/**
	 * alpha: [A-Za-z0-9]
	 */
	boolean alphanum() {
		if (pos < endpos && CharUtils.isAsciiAlphanumeric(input.charAt(pos))) {
			pos++;
			return true;
		}
		return false;
	}

	/**
	 * id: [A-Za-z0-9_-]
	 */
	boolean id() {
		if (pos < endpos) {
			char c = input.charAt(pos);
			if (CharUtils.isAsciiAlphanumeric(c) || c == '_' || c == '-') {
				pos++;
				return true;
			}
		}
		return false;
	}

	/**
	 * spaces: [ ]*
	 */
	boolean spaces() {
		while (next(' '));
		return true;
	}

	/**
	 * tag: alpha+
	 */
	String tag() {
		int begin = pos;
		if (!alphanum()) return null;
		while (alphanum());
		return input.substring(begin, pos);
	}

	/**
	 * idname: "#" id+
	 */
	String idname() {
		int mark = pos;
		if (!next('#')) return null;
		if (!id()) { pos = mark; return null; }
		while (id());
		return input.substring(mark + 1, pos);
	}

	/**
	 * className: "." id+
	 */
	String className() {
		int mark = pos;
		if (!next('.')) return null;
		if (!id()) { pos = mark; return null; }
		while (id());
		return input.substring(mark + 1, pos);
	}

	/**
	 * attr: "[" id+ ^? "=" [^]]* "]"
	 */
	NodeMatcher attr() {
		int mark = pos;
		if (!next('[')) return null;
		if (!id()) { pos = mark; return null; }
		while (id());
		int attrend = pos;
		boolean isStart = next('^');
		if (!next('=')) { pos = mark; return null; }
		int valuestart = pos;
		while (nextNot(']'));
		if (!next(']')) { pos = mark; return null; }
		if (isStart) {
			return new AttrStartMatcher(input.substring(mark + 1, attrend), input.substring(valuestart, pos - 1));
		} else {
			return new AttrEqMatcher(input.substring(mark + 1, attrend), input.substring(valuestart, pos - 1));
		}
	}

	/**
	 * cssex: tag? (idname | className | attr)*
	 */
	NodeMatcher cssex() {
		String tag = tag();
		ArrayList<NodeMatcher> matchers = new ArrayList<NodeMatcher>();
		String id = null;
		String tmp;
		NodeMatcher nv;
		while (true) {
			if ((tmp = idname()) != null) {
				Preconditions.checkArgument(id == null, "You can't specify 2 ids: " + tmp);
				id = tmp;
				continue;
			}
			if ((tmp = className()) != null) {
				matchers.add(new ClassNameMatcher(tmp));
				continue;
			}
			if ((nv = attr()) != null) {
				matchers.add(nv);
				continue;
			}
			break;
		}
		if (id != null) {
			// 把 id 放在第一个，因为 id 一般是比较强的条件，可短路后面的条件
			matchers.add(0, new AttrEqMatcher("id", id));
		}
		if (tag != null) {
			// 把 tag 放在最后一个，因为 tag name 一般是比较弱的条件
			matchers.add(new TagNameMatcher(tag));
		}
		if (matchers.size() == 1) {
			return matchers.get(0);
		} else if (matchers.size() > 1) {
			return new AndNodeMatcher(matchers);
		}
		return null;
	}

	/**
	 * spaces "|" spaces cssex
	 */
	NodeMatcher orex_0() {
		int mark = pos;
		spaces();
		if (!next('|')) { pos = mark; return null; }
		spaces();
		NodeMatcher matcher = cssex();
		if (matcher == null) { pos = mark; return null; }
		return matcher;
	}

	/**
	 * orex: cssex ( spaces "|" spaces cssex )*
	 */
	NodeMatcher orex() {
		int mark = pos;
		NodeMatcher matcher = cssex();
		if (matcher == null) { pos = mark; return null; }

		ArrayList<NodeMatcher> matchers = new ArrayList<NodeMatcher>();
		matchers.add(matcher);
		do {
			matcher = orex_0();
			if (matcher != null) {
				matchers.add(matcher);
			}
		} while (matcher != null);

		if (matchers.size() == 1) {
			return matchers.get(0);
		} else if (matchers.size() > 1) {
			return new OrNodeMatcher(matchers);
		}
		return null;
	}

	/**
	 * @see java.util.concurrent.Callable
	 *
	 * 但不抛异常
	 */
	public NodeMatcher call() {
		NodeMatcher ret = orex();
		if (ret == null) {
			throw new IllegalArgumentException(this.input + " is not valid css query");
		}
		if (!aheadNot()) {
			throw new IllegalArgumentException("bad input: " + input.substring(pos));
		}
		return ret;
	}

	public static NodeMatcher compile(String input) {
		return new NodeMatcherCompiler(input).call();
	}
}
