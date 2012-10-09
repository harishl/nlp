import java.util.HashSet;

public class POSHelper {
	public HashSet<String> pennTreeBankTagSet;

	public void generatePennTreeBankTagSet() {
	pennTreeBankTagSet=new HashSet<String>();
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
	pennTreeBankTagSet.add("'");
	pennTreeBankTagSet.add("(");
	pennTreeBankTagSet.add(")");
	pennTreeBankTagSet.add(",");
	pennTreeBankTagSet.add(".");
	pennTreeBankTagSet.add(":");
	pennTreeBankTagSet.add("\""); 	
}
}
