package org.omich.lang.comparators;

import java.util.Comparator;

import org.omich.lang.words.Word;

public class ComparatorOriginalUp implements Comparator<Word> {

	public int compare(Word lhs, Word rhs) {
		return StringCompare.compare(lhs.getOriginal(), rhs.getOriginal());
	}

}
