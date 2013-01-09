package henix.htmlpattern;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import net.jcip.annotations.NotThreadSafe;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLString;

import com.google.common.base.Preconditions;

/**
 * HtmlPattern 是一个树模式。它可以在一颗 DOM 树上匹配一个 DOM 子树。
 *
 * 一个 HtmlPattern 由一个 NodeMatcher（匹配根节点）和若干 child HtmlPattern 构成。
 * 而这些 chile HtmlPattern 与当前 HtmlPattern 有两种连接方式：
 * 
 * * CHILD - 表示这个 child HtmlPattern 必须匹配当前根节点下的一个子树
 * * DESCENDENCE - 表示这个 child HtmlPattern 可以从当前根节点的任意一个后代开始匹配子树
 *
 * 而 CollectOption 表示可以收集此 HtmlPattern 中的 text/html/owntext ，以供回调函数使用
 *
 * 不可多个线程同时改变 HtmlPattern
 *
 * @author henix
 */
@NotThreadSafe
public class HtmlPattern implements IHtmlPattern {

	public static final int RangeDescendence = -1;
	public static final int RangeChild = 0;
	public static final int SeqAll = 0;

	private static class ChildLinkSeq {
		public IHtmlPattern childPattern;
		/**
		 * 指定此子 pattern 的匹配范围：
		 *
		 * * 0 : 所有孩子
		 * * -1 ：所有子孙
		 * * >= 1 : 第几个孩子
		 */
		public int matchRange;
		/**
		 * 匹配第几次出现，为 0 则是所有
		 */
		public int seq;
		/**
		 * 状态变量：实际匹配了多少次，需要重置
		 */
		public int matchCount = 0;
		public ChildLinkSeq(int matchRange, int seq, IHtmlPattern treePattern) {
			this.matchRange = matchRange;
			this.seq = seq;
			this.childPattern = treePattern;
		}
	}

	private NodeMatcher nodeMatcher;
	private CallbackGroup callbacks;

	private List<ChildLinkSeq> childs = null;
	private IHtmlPattern curChild = null;
	private int depth = 0;
	private int depthMark = -1;
	private int curChildNum = 0;
	private boolean ended = false;

	private StringBuilder text = null;
	private StringBuilder html = null;
	private StringBuilder owntext = null;

	public HtmlPattern(NodeMatcher nodeMatcher, EnumSet<IHtmlPattern.CollectOption> collectOptions, CallbackGroup callbacks) {
		this.nodeMatcher = Preconditions.checkNotNull(nodeMatcher);
		this.callbacks = callbacks;

		if (collectOptions != null) {
			if (collectOptions.contains(IHtmlPattern.CollectOption.TEXT)) {
				text = new StringBuilder();
			}
			if (collectOptions.contains(IHtmlPattern.CollectOption.HTML)) {
				html = new StringBuilder();
			}
			if (collectOptions.contains(IHtmlPattern.CollectOption.OWNTEXT)) {
				owntext = new StringBuilder();
			}
		}
	}

	public HtmlPattern(NodeMatcher nodeMatcher, CallbackGroup callbacks) {
		this(nodeMatcher, null, callbacks);
	}

	public HtmlPattern(NodeMatcher nodeMatcher) {
		this(nodeMatcher, null, null);
	}

	public HtmlPattern(String nodePatt, EnumSet<IHtmlPattern.CollectOption> collectOptions, CallbackGroup callbacks) {
		this(NodeMatcherCompiler.compile(nodePatt), collectOptions, callbacks);
	}

	public HtmlPattern(String nodePatt, CallbackGroup callbacks) {
		this(nodePatt, null, callbacks);
	}

	public HtmlPattern(String nodePatt) {
		this(nodePatt, null, null);
	}

	public HtmlPattern sub(int matchRange, int seq, IHtmlPattern treePattern) {
		if (childs == null) {
			childs = new ArrayList<HtmlPattern.ChildLinkSeq>();
		}
		childs.add(new ChildLinkSeq(matchRange, seq, treePattern));
		return this;
	}

	public HtmlPattern child(IHtmlPattern treePattern) {
		sub(RangeChild, SeqAll, treePattern);
		return this;
	}

	/**
	 * 匹配第 seq 次出现
	 */
	public HtmlPattern child(int seq, IHtmlPattern treePattern) {
		Preconditions.checkArgument(seq > 0);
		sub(RangeChild, seq, treePattern);
		return this;
	}

	public HtmlPattern descend(IHtmlPattern treePattern) {
		sub(RangeDescendence, SeqAll, treePattern);
		return this;
	}

	/**
	 * 匹配第 n 个 child
	 */
	public HtmlPattern nthChild(int n, IHtmlPattern treePattern) {
		Preconditions.checkArgument(n > 0);
		sub(n, SeqAll, treePattern);
		return this;
	}

	/**
	 * 匹配第 seq 次出现
	 */
	public HtmlPattern descend(int seq, IHtmlPattern treePattern) {
		Preconditions.checkArgument(seq > 0);
		sub(RangeDescendence, seq, treePattern);
		return this;
	}

	public boolean matches(String tag, XMLAttributes attrs) {
		return nodeMatcher.matches(tag, attrs);
	}

	public void characters(XMLString xmlString) {
		Preconditions.checkState(!ended, "ended but char " + xmlString.toString());
		Preconditions.checkState(depth > 0, "depth = " + depth);
		if (text != null) {
			text.append(xmlString.ch, xmlString.offset, xmlString.length);
		}
		if (html != null) {
			html.append(xmlString.ch, xmlString.offset, xmlString.length);
		}
		if (owntext != null && depth == 1) {
			owntext.append(xmlString.ch, xmlString.offset, xmlString.length);
		}
		if (curChild != null) {
			curChild.characters(xmlString);
		}
	}

	private static void acchtml(String tag, XMLAttributes attrs, StringBuilder sb) {
		if (tag.equalsIgnoreCase("br")) {
			sb.append("<br/>");
		} else {
			sb.append('<').append(tag.toLowerCase());
			int len = attrs.getLength();
			for (int i = 0; i < len; i++) {
				sb.append(' ').append(attrs.getQName(i)).append("=\"").append(StringEscapeUtils.escapeXml(attrs.getValue(i))).append('"');
			}
			sb.append('>');
		}
	}

	private static void acchtmlEnd(String tag, StringBuilder sb) {
		if (!tag.equalsIgnoreCase("br")) {
			sb.append("</").append(tag.toLowerCase()).append('>');
		}
	}

	public void startTag(String tag, XMLAttributes attrs) {
		Preconditions.checkState(!ended, "ended but startTag " + tag);
		if (depth > 0) {
			if (html != null) {
				acchtml(tag, attrs, html);
			}
			if (depth == 1) {
				curChildNum++;
			}
			if (curChild == null && childs != null) {
				for (ChildLinkSeq child : childs) {
					if (child.matchRange == RangeDescendence || (depth == 1 && (child.matchRange == RangeChild || child.matchRange == curChildNum))) {
						if (child.childPattern.matches(tag, attrs)) {
							child.matchCount++;
							if (child.seq == SeqAll || child.seq == child.matchCount) {
								curChild = child.childPattern;
								depthMark = depth;
								break;
							}
						}
					}
				}
			}
			if (curChild != null) {
				curChild.startTag(tag, attrs);
			}
		} else if (callbacks != null) {
			callbacks.start(tag, attrs);
		}
		depth++;
	}

	public void endTag(String tag) {
		Preconditions.checkState(!ended, "ended but endTag " + tag);
		depth--;
		Preconditions.checkState(depth >= 0, "depth = " + depth);
		if (depth == 0) {
			if (callbacks != null) {
				callbacks.end(this);
			}
			ended = true;
		} else {
			if (html != null) {
				acchtmlEnd(tag, html);
			}
		}
		if (curChild != null) {
			curChild.endTag(tag);
			if (depth == depthMark) {
				curChild.reset();
				curChild = null;
				depthMark = -1;
			}
		}
	}

	public String text() {
		if (text != null) {
			return text.toString();
		}
		return null;
	}

	public String html() {
		if (html != null) {
			return html.toString();
		}
		return null;
	}

	public String owntext() {
		if (owntext != null) {
			return owntext.toString();
		}
		return null;
	}

	public void reset() {
		ended = false;
		depth = 0;

		curChild = null;
		depthMark = -1;
		curChildNum = 0;

		if (childs != null) {
			for (ChildLinkSeq child : childs) {
				child.matchCount = 0;
			}
		}

		if (text != null) {
			text = new StringBuilder();
		}
		if (html != null) {
			html = new StringBuilder();
		}
		if (owntext != null) {
			owntext = new StringBuilder();
		}
	}
}
