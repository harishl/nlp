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
			String tag, word,prevTag;
			int count = 0;
			List<String> sentences = new ArrayList<String>();
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

			Iterator<String> sentenceIterator = sentences.iterator();
			while (sentenceIterator.hasNext()) {
				i++;
				temp=sentenceIterator.next();
				//System.out.println(temp);
				StringTokenizer token = new StringTokenizer(
						temp, " ");
				prevTag = token.nextToken();
				wordTagPair = null;
				while (token.hasMoreTokens()) {
					StringTokenizer bigramToken = new StringTokenizer(
							token.nextToken(), "/");
					
					if (bigramToken.countTokens() >= 2) {
						List<String> words = new ArrayList<String>();
						for (int l = 1; l < bigramToken.countTokens(); l++)
							words.add(bigramToken.nextToken());
						tag = bigramToken.nextToken();
						addListofWordTags(words, tag);
						if (ph.pennTreeBankTagSet.contains(tag)) {
						  ph.tagCount.put(tag, ph.tagCount.get(tag) + 1);}
						bigramTags=prevTag+"|"+tag;
						addBigramTagTag(bigramTags);
						prevTag=tag;
					}
					else if(bigramToken.countTokens() == 1)
					{
						tag = bigramToken.nextToken();
						bigramTags=prevTag+"|"+tag;
						addBigramTagTag(bigramTags);
						prevTag=tag;
					}
				}
			
			}
			/*
			 * StringTokenizer token = new StringTokenizer(temp, "./.");
			 * System.out.println(temp); i++; if(i>3) break; while
			 * (token.hasMoreTokens()) { j++; String temp1 = token.nextToken();
			 * 
			 * StringTokenizer anothertoken = new StringTokenizer(temp1, "/");
			 * flag = true;
			 * 
			 * while (anothertoken.hasMoreTokens()) { String testToken =
			 * anothertoken.nextToken().trim(); if
			 * (ph.pennTreeBankTagSet.contains(testToken)) {
			 * ph.tagCount.put(testToken, ph.tagCount.get(testToken) + 1); flag
			 * = false; } else { if (ph.wordCount.containsKey(testToken)) {
			 * ph.wordCount.put(testToken, ph.wordCount.get(testToken) + 1); }
			 * else { ph.wordCount.put(testToken, 1); } } if
			 * (ph.breakTag.contains(testToken)) { li.add(testToken); if
			 * (ph.wordCount.containsKey(testToken)) {
			 * ph.wordCount.put(testToken, ph.wordCount.get(testToken) + 1); }
			 * else { ph.wordCount.put(testToken, 1); } break; } }
			 * 
			 * }
			 * 
			 * /* System.out.println(ph.tagCount); System.out.println(i);
			 * Iterator<String> itr = ph.tagCount.keySet().iterator(); k = 0;
			 * while (itr.hasNext()) k = k + ph.tagCount.get(itr.next());
			 * 
			 * System.out.println(k);
			 * System.out.println(ph.wordCount.keySet().size()); int h = 0;
			 * Iterator<String> itr1 = ph.wordCount.keySet().iterator(); while
			 * (itr1.hasNext()) h = h + ph.wordCount.get(itr1.next());
			 * System.out.println("h=" + h); File fe = new
			 * File("WordCount.txt"); FileWriter fw = new FileWriter(fe);
			 * BufferedWriter bw = new BufferedWriter(fw);
			 * bw.write(ph.wordCount.toString()); bw.close();
			 * System.out.println(count);
			 */
			System.out.println(ph.bigramWordCount.size());
			System.out.println(ph.tagCount.size());
			System.out.println(ph.bigramTagCount.size());
			System.out.println(ph.bigramTagCount.get("<s>|IN"));
			System.out.println(ph.tagVocabularyCount);
			Iterator itr=ph.bigramTagCount.keySet().iterator();
			k=0;
			while(itr.hasNext())
			{
				k=k+ph.bigramTagCount.get(itr.next());
			}
			
			System.out.println(sentences.size());
			Double f=Math.exp(Math.log(new Double(ph.bigramTagCount.get("<s>|IN")+1))-Math.log(new Double(ph.tagVocabularyCount+sentences.size())));
			System.out.println(f);
			System.out.println(k);

		} catch (FileNotFoundException e) {
			System.out
					.println("The file path is wrong for training file so unable to complete the model");
			System.exit(0);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void addListofWordTags(List<String> words, String tag) {
		Iterator<String> wordItr = words.iterator();
		while (wordItr.hasNext()) {
			String word = wordItr.next();
			String wordTagPair = word + "|" + tag;
			addBigramWordTags(wordTagPair);
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
}
