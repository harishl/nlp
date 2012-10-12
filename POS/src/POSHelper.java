import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class POSHelper {
	public HashSet<String> pennTreeBankTagSet;
	public HashMap<String, Integer> tagCount;
	public HashMap<String, Integer> bigramTagCount;
	public HashMap<String, Integer> wordCount;
	public HashMap<String, Integer> bigramWordCount;
	public HashSet<String> breakTag; 
	public long tagVocabularyCount;
	POSHelper() {
		pennTreeBankTagSet = new HashSet<String>();
		tagCount = new HashMap<String, Integer>();
		wordCount = new HashMap<String, Integer>();
		bigramWordCount=new HashMap<String, Integer>();
		bigramTagCount=new HashMap<String, Integer>();
		breakTag=new HashSet<String>();
		tagVocabularyCount=((45*45)*44)+90;
		generatePennTreeBankTagSet();
		intializeHashMap();
	}

	private void intializeHashMap() {
		Iterator<String> itr = pennTreeBankTagSet.iterator();
		while (itr.hasNext()) {
			tagCount.put(itr.next(), 0);
		}

	}

	public void generatePennTreeBankTagSet() {

		pennTreeBankTagSet.add("CC");
		pennTreeBankTagSet.add("CD");
		pennTreeBankTagSet.add("DT");
		pennTreeBankTagSet.add("EX");
		pennTreeBankTagSet.add("FW");
		pennTreeBankTagSet.add("IN");
		pennTreeBankTagSet.add("JJ");
		pennTreeBankTagSet.add("JJR");
		pennTreeBankTagSet.add("JJS");
		pennTreeBankTagSet.add("LS");
		pennTreeBankTagSet.add("MD");
		pennTreeBankTagSet.add("NN");
		pennTreeBankTagSet.add("NNS");
		pennTreeBankTagSet.add("NNP");
		pennTreeBankTagSet.add("NNPS");
		pennTreeBankTagSet.add("PDT");
		pennTreeBankTagSet.add("POS");
		pennTreeBankTagSet.add("PRP");
		pennTreeBankTagSet.add("PRP$");
		pennTreeBankTagSet.add("RB");
		pennTreeBankTagSet.add("RBR");
		pennTreeBankTagSet.add("RBS");
		pennTreeBankTagSet.add("RP");
		pennTreeBankTagSet.add("SYM");
		pennTreeBankTagSet.add("TO");
		pennTreeBankTagSet.add("UH");
		pennTreeBankTagSet.add("VB");
		pennTreeBankTagSet.add("VBD");
		pennTreeBankTagSet.add("VBG");
		pennTreeBankTagSet.add("VBN");
		pennTreeBankTagSet.add("VBP");
		pennTreeBankTagSet.add("VBZ");
		pennTreeBankTagSet.add("WDT");
		pennTreeBankTagSet.add("WP");
		pennTreeBankTagSet.add("WP$");
		pennTreeBankTagSet.add("WRB");
		pennTreeBankTagSet.add("#");
		pennTreeBankTagSet.add("$");
		pennTreeBankTagSet.add("``");
		pennTreeBankTagSet.add("-LRB-");
		pennTreeBankTagSet.add("-RRB-");
		pennTreeBankTagSet.add(",");
		pennTreeBankTagSet.add(".");
		pennTreeBankTagSet.add(":");
		pennTreeBankTagSet.add("\'\'");
		breakTag.add("#");
		breakTag.add("$");
		breakTag.add("``");
		breakTag.add(",");
		breakTag.add(".");
		breakTag.add("\'\'");
		breakTag.add(":");
	}
}
