import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public class build_tagger {
	static POSHelper ph;

	public static void main(String args[]) {
		if (args.length == 3) {
		File file = new File(args[0]);
		File tuneFile = new File(args[1]);
		File modelFile = new File(args[2]);
		FileReader read;
		BufferedReader buffRead;
		ObjectOutputStream out;
		ph = new POSHelper();
		try {
			read = new FileReader(file);
			buffRead = new BufferedReader(read);
			String temp;
			String bigramTags;
			String tag, prevTag;
			List<String> sentences = new ArrayList<String>();
			List<String> tunedSentences = new ArrayList<String>();
			while (null != (temp = buffRead.readLine())) {
				if (temp.contains("/.")) {
					while (temp.indexOf("/.") > 0) {
						sentences.add("<s> "
								+ temp.substring(0, temp.indexOf("/.") + 2)
								+ " <\\s>");
						temp = temp.substring(temp.indexOf("/.") + 2);
					}
				} else {
					sentences.add("<s> " + temp + " <\\s>");
				}

			}
			buffRead.close();

			read = new FileReader(tuneFile);
			buffRead = new BufferedReader(read);
			while (null != (temp = buffRead.readLine())) {
				if (temp.contains("/.")) {
					while (temp.indexOf("/.") > 0) {
						tunedSentences.add("<s> "
								+ temp.substring(0, temp.indexOf("/.") + 2)
								+ " <\\s>");
						temp = temp.substring(temp.indexOf("/.") + 2);
					}
				} else {
					tunedSentences.add("<s> " + temp + " <\\s>");
				}

			}

			Iterator<String> sentenceIterator = sentences.iterator();
			while (sentenceIterator.hasNext()) {

				temp = sentenceIterator.next();
				StringTokenizer token = new StringTokenizer(temp, " ");
				prevTag = token.nextToken();
				while (token.hasMoreTokens()) {
					StringTokenizer bigramToken = new StringTokenizer(
							token.nextToken(), "/");

					if (bigramToken.countTokens() >= 2) {
						List<String> words = new ArrayList<String>();
						int countTokens = bigramToken.countTokens();
						for (int l = 1; l < countTokens; l++)
							words.add(bigramToken.nextToken());
						tag = bigramToken.nextToken();
						addListofWordTags(words, tag);
						if (ph.pennTreeBankTagSet.contains(tag)) {
							ph.tagCount.put(tag, ph.tagCount.get(tag) + 1);
						}
						bigramTags = prevTag + "|" + tag;
						addBigramTagTag(bigramTags);
						prevTag = tag;
					} else if (bigramToken.countTokens() == 1) {
						tag = bigramToken.nextToken();
						bigramTags = prevTag + "|" + tag;
						addBigramTagTag(bigramTags);
						prevTag = tag;
					}
				}

			}
			ph.unkBigramWordCount = ph.bigramWordCount;

			Iterator<String> tuneSentIterator = tunedSentences.iterator();
			while (tuneSentIterator.hasNext()) {
				temp = tuneSentIterator.next();
				StringTokenizer token = new StringTokenizer(temp, " ");
				prevTag = token.nextToken();

				while (token.hasMoreTokens()) {
					StringTokenizer bigramToken = new StringTokenizer(
							token.nextToken(), "/");

					if (bigramToken.countTokens() >= 2) {
						List<String> words = new ArrayList<String>();
						int counttoken = bigramToken.countTokens();
						for (int l = 1; l < counttoken; l++)
							words.add(bigramToken.nextToken());
						tag = bigramToken.nextToken();
						addTunedListofWordTags(words, tag);
						if (ph.pennTreeBankTagSet.contains(tag)) {
							ph.tagCount.put(tag, ph.tagCount.get(tag) + 1);
						}
						bigramTags = prevTag + "|" + tag;
						addBigramTagTag(bigramTags);
						prevTag = tag;
					} else if (bigramToken.countTokens() == 1) {
						tag = bigramToken.nextToken();
						bigramTags = prevTag + "|" + tag;
						addBigramTagTag(bigramTags);
						prevTag = tag;
					}
				}

			}

			ph.startTagCount = sentences.size() + tunedSentences.size();
			addOneSmoothing(ph.startTagCount);
			calcEmissionProbabilityforUnkWords();
			out = new ObjectOutputStream(new FileOutputStream(modelFile));
			out.writeObject(ph);
			out.close();

		} catch (FileNotFoundException e) {
			System.out
					.println("The file path is wrong for training file so unable to complete the model");
			System.exit(0);

		} catch (IOException e) {
			e.printStackTrace();
		}
		} else {
			System.out.println("Need Three Parameters to run the POS tagger:\n"
					+ "1. Training data file Name with path \n"
					+ "2. Tuning data for unknown words file name\n"
					+ "3. Model file name to store the computed probability values" +
					"\n No Need to give path if all the files and code are in the same path just mention the file names");
			System.exit(0);
		}

	}

	private static void calcEmissionProbabilityforUnkWords() {
		Iterator<String> emissionProbabilityIterator = ph.unkBigramWordCount
				.keySet().iterator();
		while (emissionProbabilityIterator.hasNext()) {
			String wordTagpair = emissionProbabilityIterator.next();
			String tag = wordTagpair
					.substring(wordTagpair.lastIndexOf("|") + 1);
			Double prob = Math.exp(Math.log(ph.unkBigramWordCount
					.get(wordTagpair)) - Math.log(ph.tagCount.get(tag)));
			ph.emissionProbability.put(wordTagpair, prob);

		}

	}

	private static void addListofWordTags(List<String> words, String tag) {
		Iterator<String> wordItr = words.iterator();
		while (wordItr.hasNext()) {
			String word = wordItr.next();
			ph.wordVocabulary.add(word);
			String wordTagPair = word + "|" + tag;
			if (tag.equals("32")) {
				System.out.println("word:" + word + " Tag:" + tag);

			}
			addBigramWordTags(wordTagPair);
		}

	}

	private static void addTunedListofWordTags(List<String> words, String tag) {
		Iterator<String> wordItr = words.iterator();
		while (wordItr.hasNext()) {
			String word = wordItr.next();
			String wordTagPair;
			if (ph.wordVocabulary.contains(word))
				wordTagPair = word + "|" + tag;
			else
				wordTagPair = "<unknown>" + "|" + tag;
			addUnkBigramWordCount(wordTagPair);
		}

	}

	static void addUnkBigramWordCount(String wordTagPair) {
		if (ph.unkBigramWordCount.containsKey(wordTagPair)) {
			ph.unkBigramWordCount.put(wordTagPair,
					ph.unkBigramWordCount.get(wordTagPair) + 1);
		} else {
			ph.unkBigramWordCount.put(wordTagPair, 1);
		}
	}

	static void addBigramWordTags(String wordTagPair) {
		if (ph.bigramWordCount.containsKey(wordTagPair)) {
			ph.bigramWordCount.put(wordTagPair,
					ph.bigramWordCount.get(wordTagPair) + 1);
		} else {
			ph.bigramWordCount.put(wordTagPair, 1);
		}
	}

	static void addBigramTagTag(String bigramTags) {
		if (ph.bigramTagCount.containsKey(bigramTags)) {
			ph.bigramTagCount.put(bigramTags,
					ph.bigramTagCount.get(bigramTags) + 1);
		} else {
			ph.bigramTagCount.put(bigramTags, 1);
		}
	}

	static void addOneSmoothing(int startTagCount) {
		Iterator<String> outerIterator = ph.pennTreeBankTagSet.iterator();
		while (outerIterator.hasNext()) {
			String smoothString = "<s>|" + outerIterator.next();
			if (null != ph.bigramTagCount.get(smoothString))
				ph.transitionProbability.put(
						smoothString,
						calcOneSmoothing(ph.bigramTagCount.get(smoothString),
								startTagCount));
			else
				ph.transitionProbability.put(smoothString,
						calcOneSmoothing(0, startTagCount));

		}
		outerIterator = ph.pennTreeBankTagSet.iterator();
		while (outerIterator.hasNext()) {
			Iterator<String> innerIterator = ph.pennTreeBankTagSet.iterator();
			String tagMinusOne = outerIterator.next();
			while (innerIterator.hasNext()) {
				String smoothString = tagMinusOne + "|" + innerIterator.next();
				if (null != ph.bigramTagCount.get(smoothString))
					ph.transitionProbability.put(
							smoothString,
							calcOneSmoothing(
									ph.bigramTagCount.get(smoothString),
									ph.tagCount.get(tagMinusOne)));
				else
					ph.transitionProbability.put(smoothString,
							calcOneSmoothing(0, ph.tagCount.get(tagMinusOne)));

			}
		}
		outerIterator = ph.pennTreeBankTagSet.iterator();
		while (outerIterator.hasNext()) {
			String tagMinusOne = outerIterator.next();
			String smoothString = tagMinusOne + "|<\\s>";
			if (null != ph.bigramTagCount.get(smoothString))
				ph.transitionProbability.put(
						smoothString,
						calcOneSmoothing(ph.bigramTagCount.get(smoothString),
								ph.tagCount.get(tagMinusOne)));
			else
				ph.transitionProbability.put(smoothString,
						calcOneSmoothing(0, ph.tagCount.get(tagMinusOne)));
		}
		
	}

	static Double calcOneSmoothing(int bigramtagcount, int tagminusonecount) {
		Double f = Math
				.exp(Math.log(new Double(bigramtagcount + 1))
						- Math.log(new Double(ph.tagVocabularyCount
								+ tagminusonecount)));
		return f;

	}
}
