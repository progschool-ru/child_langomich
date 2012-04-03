package org.omich.lang.comparators;

import java.util.Comparator;

import org.omich.lang.words.Word;

public class ComparatorRatingDown implements Comparator<Word> {

	public int compare(Word lhs, Word rhs) {
		 return lhs.getRating() - rhs.getRating();
	}

}
