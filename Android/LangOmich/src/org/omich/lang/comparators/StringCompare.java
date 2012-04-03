package org.omich.lang.comparators;

public class StringCompare {
		
	static int compare(String lString, String rString){
		
		int length = lString.length();
		
		if(length > rString.length()){
			length = rString.length();
		}
		
		for(int i=0; i<length; i++){
			if(lString.charAt(i) != rString.charAt(i)){
				if(lString.charAt(i) < rString.charAt(i)){
					return 1;
				}
				else{
					return -1;
				}
			}
		}
		return 0;
	}
}
