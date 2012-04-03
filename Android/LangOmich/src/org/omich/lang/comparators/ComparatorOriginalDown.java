package org.omich.lang.comparators;

import java.util.Comparator;

import org.omich.lang.words.Word;

public class ComparatorOriginalDown implements Comparator<Word>{

	public int compare(Word lhs, Word rhs) {
		return (-1) * StringCompare.compare(lhs.getOriginal(), rhs.getOriginal());
	}

}