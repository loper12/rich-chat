package io.github.ashley1227.richchat.formatting.extensions.underline;

import com.vladsch.flexmark.ast.Emphasis;
import com.vladsch.flexmark.parser.InlineParser;
import com.vladsch.flexmark.parser.core.delimiter.Delimiter;
import com.vladsch.flexmark.parser.delimiter.DelimiterProcessor;
import com.vladsch.flexmark.parser.delimiter.DelimiterRun;
import com.vladsch.flexmark.util.ast.DelimitedNode;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.sequence.BasedSequence;

public class UnderlineNode extends Node implements DelimitedNode {

	private BasedSequence openingMarker = BasedSequence.NULL;
	private BasedSequence text = BasedSequence.NULL;
	private BasedSequence closingMarker = BasedSequence.NULL;

	@Override
	public BasedSequence[] getSegments() {
		return new BasedSequence[]{openingMarker, text, closingMarker};
	}

	@Override
	public void getAstExtra(StringBuilder out) {
		Node.delimitedSegmentSpan(out, openingMarker, text, closingMarker, "text");
	}

	public UnderlineNode() {
	}

	public UnderlineNode(BasedSequence chars) {
		super(chars);
	}

	public UnderlineNode(BasedSequence openingMarker, BasedSequence text, BasedSequence closingMarker) {
		super(openingMarker.baseSubSequence(openingMarker.getStartOffset(), closingMarker.getEndOffset()));
		this.openingMarker = openingMarker;
		this.text = text;
		this.closingMarker = closingMarker;
	}

	public BasedSequence getOpeningMarker() {
		return openingMarker;
	}

	public void setOpeningMarker(BasedSequence openingMarker) {
		this.openingMarker = openingMarker;
	}

	public BasedSequence getText() {
		return text;
	}

	public void setText(BasedSequence text) {
		this.text = text;
	}

	public BasedSequence getClosingMarker() {
		return closingMarker;
	}

	public void setClosingMarker(BasedSequence closingMarker) {
		this.closingMarker = closingMarker;
	}

	static class UnderlineDelimiterProcessor implements DelimiterProcessor {

		private char openingCharacter = '_';
		private char closingCharacter = '_';

		@Override
		public char getOpeningCharacter() {
			return openingCharacter;
		}

		@Override
		public char getClosingCharacter() {
			return closingCharacter;
		}

		@Override
		public int getMinLength() {
			return 0;
		}

		@Override
		public int getDelimiterUse(DelimiterRun opener, DelimiterRun closer) {
			if (opener.length() == closer.length())
				return opener.length();
			return 0;
		}

		@Override
		public void process(Delimiter opener, Delimiter closer, int delimitersUsed) {
			UnderlineNode underlineNode;
			Emphasis emphasis;
			switch (delimitersUsed) {
				case 1:
					emphasis = new Emphasis(opener.getTailChars(delimitersUsed), BasedSequence.NULL,
							closer.getLeadChars(delimitersUsed));
					opener.moveNodesBetweenDelimitersTo(emphasis, closer);
					break;
				case 2:
					underlineNode = new UnderlineNode(opener.getTailChars(delimitersUsed), BasedSequence.NULL,
							closer.getLeadChars(delimitersUsed));
					opener.moveNodesBetweenDelimitersTo(underlineNode, closer);
					break;
			}
		}

		@Override
		public Node unmatchedDelimiterNode(InlineParser inlineParser, DelimiterRun delimiter) {
			return null;
		}

		@Override
		public boolean canBeOpener(String before, String after, boolean leftFlanking, boolean rightFlanking, boolean beforeIsPunctuation, boolean afterIsPunctuation, boolean beforeIsWhitespace, boolean afterIsWhiteSpace) {
			return leftFlanking;
		}

		@Override
		public boolean canBeCloser(String before, String after, boolean leftFlanking, boolean rightFlanking, boolean beforeIsPunctuation, boolean afterIsPunctuation, boolean beforeIsWhitespace, boolean afterIsWhiteSpace) {
			return rightFlanking;
		}

		@Override
		public boolean skipNonOpenerCloser() {
			return false;
		}
	}
}
