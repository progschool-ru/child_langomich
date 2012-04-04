package org.omich.lang.comparators;

public class StringCompare {
		
	public static int compare(String lString, String rString){
		
		String leftString  = lString.toLowerCase();
		String rightString = rString.toLowerCase();
		
		int length = lString.length();
		
		if(length > rString.length()){
			length = rString.length();
		}
		
		if(leftString.equals(rightString)){return 0;}
		
		for(int i=0; i<length; i++){
			if(leftString.charAt(i) != rightString.charAt(i)){
				if(leftString.charAt(i) > rightString.charAt(i)){
					return 1;
				}
				else{
					return -1;
				}
			}
		}
		
		if(lString.length()>rString.length()){
			return -1;
		}else{
			return 1;
		}
	}
}
