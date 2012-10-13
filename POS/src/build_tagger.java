import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public class build_tagger {
	static POSHelper ph;

	public static void main(String args[]) {
		File file = new File("/home/user/NLP/a2_data/sents.train");
		File tuneFile = new File("/home/user/NLP/a2_data/sents.devt");
		FileReader read;
		BufferedReader buffRead;
		ph = new POSHelper();
		try {
			read = new FileReader(file);
			buffRead = new BufferedReader(read);
			String temp;
			int i = 0;
			int j = 0;
			int k = 0;
			String bigramTags, wordTagPair;
			boolean flag = true;
			List<String> li = new ArrayList<String>();
			String tag, word, prevTag;
			int count = 0;
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
				i++;
				temp = sentenceIterator.next();
				// System.out.println(temp);
				StringTokenizer token = new StringTokenizer(temp, " ");
				prevTag = token.nextToken();
				wordTagPair = null;
				while (token.hasMoreTokens()) {
					StringTokenizer bigramToken = new StringTokenizer(
							token.nextToken(), "/");

					if (bigramToken.countTokens() >= 2) {
						List<String> words = new ArrayList<String>();
						int countTokens = bigramToken.countTokens();
						for (int l = 1; l < countTokens; l++)
							words.add(bigramToken.nextToken());
						tag = bigramToken.nextToken();
						if (tag.equals("32"))
							System.out.println(bigramToken.countTokens());
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
			System.out.println("before tuning");
			System.out.println("bigramWordCount" + ph.bigramWordCount.size());
			System.out.println("tagCount" + ph.tagCount.size());
			System.out.println("bigramTagCount"
					+ ph.bigramTagCount.get("<s>|IN"));
			System.out.println("tagVocabularyCount" + ph.tagVocabularyCount);
			System.out.println("unkBigramWordCount"
					+ ph.unkBigramWordCount.size());
			Iterator<String> tuneSentIterator = tunedSentences.iterator();
			while (tuneSentIterator.hasNext()) {
				temp = tuneSentIterator.next();
				StringTokenizer token = new StringTokenizer(temp, " ");
				prevTag = token.nextToken();
				wordTagPair = null;

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

			System.out.println("after tuning");
			System.out.println("bigramWordCount" + ph.bigramWordCount.size());
			System.out.println("tagCount" + ph.tagCount.size());
			System.out.println("bigramTagCount"
					+ ph.bigramTagCount.size());
			System.out.println("tagVocabularyCount" + ph.tagVocabularyCount);
			System.out.println("unkBigramWordCount"
					+ ph.unkBigramWordCount.size());
			addOneSmoothing(sentences.size() + tunedSentences.size());
			calcEmissionProbabilityforUnkWords();
			Iterator itr = ph.bigramTagCount.keySet().iterator();
			k = 0;
			while (itr.hasNext()) {
				k = k + ph.bigramTagCount.get(itr.next());
			}

			System.out.println(sentences.size());
			// Used for add one smoothing
			Double f = Math.exp(Math.log(new Double(ph.bigramTagCount
					.get("<s>|IN") + 1))
					- Math.log(new Double(ph.tagVocabularyCount
							+ sentences.size())));
			System.out.println(f);
			System.out.println(ph.unkBigramWordCount.get("<unknown>|VB"));
			System.out.println(ph.emissionProbability.size());

		} catch (FileNotFoundException e) {
			System.out
					.println("The file path is wrong for training file so unable to complete the model");
			System.exit(0);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void calcEmissionProbabilityforUnkWords() {
		Iterator<String> emissionProbabilityIterator = ph.unkBigramWordCount
				.keySet().iterator();
		while (emissionProbabilityIterator.hasNext()) {
			String wordTagpair = emissionProbabilityIterator.next();
			String tag = wordTagpair
					.substring(wordTagpair.lastIndexOf("|") + 1);
			// System.out.println("tag: "+tag+" wordtag: "+wordTagpair);
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
		System.out.println("probMatrixsize:" + ph.transitionProbability.size());
	}

	static Double calcOneSmoothing(int bigramtagcount, int tagminusonecount) {
		Double f = Math
				.exp(Math.log(new Double(bigramtagcount + 1))
						- Math.log(new Double(ph.tagVocabularyCount
								+ tagminusonecount)));
		return f;

	}
}
