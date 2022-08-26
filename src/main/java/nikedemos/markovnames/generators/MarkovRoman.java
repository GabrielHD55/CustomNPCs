package nikedemos.markovnames.generators;

import java.util.Random;

import net.minecraft.util.text.TranslationTextComponent;
import nikedemos.markovnames.MarkovDictionary;

public class MarkovRoman extends MarkovGenerator {
	public MarkovDictionary markov2;
	public MarkovDictionary markov3;

	public MarkovRoman(int seqlen) {
		this.markov = new MarkovDictionary("roman_praenomina.txt", seqlen);
		this.markov2 = new MarkovDictionary("roman_nomina.txt", seqlen);
		this.markov3 = new MarkovDictionary("roman_cognomina.txt", seqlen);
	}

	public MarkovRoman() {
		this(3);
	}

	@Override
	public String feminize(String element, boolean flag) {
		// change "us" into "a" and "o" at the end into "a"
		if (element.endsWith("us")) {
			element = element.substring(0, element.length() - 2) + "a";
		} 
		else if (element.endsWith("o")) {
			element = element.substring(0, element.length() - 2) + "a";
		}		

		return element;
	}

	@Override
	public String fetch(int gender) {

		String seq1 = markov.generateWord();
		String seq2 = markov2.generateWord();
		String seq3 = markov3.generateWord();

		// check the gender.
		// 0 = random gender, 1 = male, 2 = female
		// if there's no gender specified (0),
		// now it's time to pick it at random
		//
		if (gender == 0) {
			gender = MarkovDictionary.rng.nextBoolean() == true ? 1 : 2;
		}

		// now if it's 2 - a lady - feminize the 3 sequences
		if (gender == 2) {
			seq1 = feminize(seq1, false);
			seq2 = feminize(seq2, false);
			seq3 = feminize(seq3, true);
		}

		return (new StringBuilder(seq1).append(" ").append(seq2).append(" ").append(seq3).toString());
	}
}
