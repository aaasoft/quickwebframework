package com.quickwebframework.util.pattern;

public class WildcardPattern implements Implication {
	public static String WILDCARD = "*";
	private String pattern;

	public WildcardPattern(String pattern) {
		this.pattern = pattern;
		// System.out.println("created: " + pattern);
	}

	public boolean equals(Object other) {
		if (other instanceof WildcardPattern) {
			WildcardPattern o = (WildcardPattern) other;
			return this.pattern.equals(o.pattern);
		} else
			return this.pattern.equals(other.toString());
	}

	public int hashCode() {
		return pattern.hashCode();
	}

	public boolean implies(Object other) {
		if (other == null)
			return false;

		if (other instanceof WildcardPattern) {
			WildcardPattern o = (WildcardPattern) other;
			return implies(o.pattern);
		} else
			return impliesCheck(other.toString());
	}

	private boolean impliesCheck(String str) {
		if (pattern.equals(str))
			return true;

		int indx = pattern.lastIndexOf(WILDCARD);

		if (indx != -1) {
			String prematch = pattern.substring(0, indx);
			String postmatch = pattern.substring(indx + WILDCARD.length());

			// checks for patterns such as:
			// *.foo, boo*foo, /foo/*, /foo*, /*, *
			// This cannot check for multiple *'s
			if (str.startsWith(prematch) && str.endsWith(postmatch)) {
				return true;
			}
		}

		return false;
	}

	public String toString() {
		return pattern;
	}
}