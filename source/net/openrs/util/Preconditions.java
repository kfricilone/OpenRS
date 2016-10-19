package net.openrs.util;

public class Preconditions {
	
	 public static void checkArgument(boolean condition, String message){
		 if(!condition){
			 throw new IllegalArgumentException(message);
		 }
	 }
}
