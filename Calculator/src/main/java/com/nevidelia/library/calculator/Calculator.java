package com.nevidelia.library.calculator;

import androidx.annotation.NonNull;

public class Calculator {
	
	// TODO: Prioritise by operation
	// TODO: '/' '*' '+' '-'
	
	private final char[] flags = {'+', '-', '*', '/', ' '};
	private StringBuilder buffer;
	
	@NonNull
	private String calculate(String string) {
		string += " "; double output = 0; char operation = ' '; this.buffer = new StringBuilder();
		for (char current : string.toCharArray()) {
			boolean proceed = (current >= '0' && current <= '9') || current == '.';
			if (this.calculable(proceed, current, operation)) {
				if (this.buffer.toString().length() > 0) {
					switch (operation) {
						case '+':
							output += Double.parseDouble(this.buffer.toString());
							break;
						case '-':
							output -= Double.parseDouble(this.buffer.toString());
							break;
						case '*':
							output *= Double.parseDouble(this.buffer.toString());
							break;
						case '/':
							output /= Double.parseDouble(this.buffer.toString());
							break;
						case ' ':
							output = Double.parseDouble(this.buffer.toString());
							break;
						default:
							throw new NumberFormatException();
					} this.buffer = new StringBuilder();
				} operation = current;
			}
		} return String.valueOf(output);
	}
	
	private boolean calculable(boolean proceed, char current, char operation) {
		boolean negative = false;
		if (current == '-') {
			for (char foo : this.flags) {
				if (operation == foo) {
					String temp = this.buffer.toString();
					if ((temp.length() > 0) && (temp.charAt(temp.length() - 1) == foo))
						negative = true;
					break;
				}
			}
		}
		if (!proceed && negative) {
			this.buffer.append(current);
		} else {
			if (proceed) this.buffer.append(current);
			else return true;
		} return false;
	}
	
	@NonNull
	private String extract(@NonNull String string) {
		int start = 0, end = 0;
		if (string.contains("(")) {
			for (int foo = 0; foo < string.length(); foo++) {
				char current = string.charAt(foo);
				if (current == '(') start = foo;
				if (current == ')') {
					end = foo + 1;
					break;
				}
			} return string.substring(start, end);
		} return string;
	}
	
	public double compile(@NonNull String string) {
		if (string.contains(" ")) string = string.replaceAll(" ", "");
		String extracted = this.extract(string);
		String calculated = this.calculate(extracted.replace("(", "").replace(")", ""));
		String output = string.replace(extracted, calculated);
		return string.contains("(") ? this.compile(output) : Double.parseDouble(output);
	}
}