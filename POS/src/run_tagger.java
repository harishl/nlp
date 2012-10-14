import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class run_tagger {
	static POSHelper ph;
	private static Object[][] HiddenState;

	public static void main(String args[]) {
		File modelFile = new File("/home/user/NLP/a2_data/model_file");
		File inputFile = new File("/home/user/NLP/a2_data/sents.test");
		File outputFile=new File("/home/user/NLP/a2_data/out.text");
		FileReader reader;
		BufferedReader buffRead;
		FileWriter wr;
		BufferedWriter buffWrite;
		HiddenState[][] hs;
		try {
			wr=new FileWriter(outputFile);
			buffWrite=new BufferedWriter(wr);
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(
					modelFile));
			ph = (POSHelper) in.readObject();
			reader = new FileReader(inputFile);
			buffRead = new BufferedReader(reader);
			String temp;
			while(null!=(temp= buffRead.readLine())){
			List<String> tags = new ArrayList<String>(ph.pennTreeBankTagSet);
		
			StringTokenizer token = new StringTokenizer(temp, " ");
		
			System.out.println(token.countTokens());
			int n = token.countTokens();
			hs = new HiddenState[n][45];
			String firstWord = token.nextToken();
			System.out.println(ph.emissionProbability.get("unions|NNS"));
			System.out.println(ph.transitionProbability.get("NN|NNS"));
			System.out.println(ph.transitionProbability.get("NNP|NNS"));
			for (int i = 0; i < 45; i++) {

				hs[0][i] = new HiddenState();
				if (ph.wordVocabulary.contains(firstWord)) {
					if (null != ph.emissionProbability.get(firstWord + "|"
							+ tags.get(i)))
						hs[0][i].maxValue = calcProbability(
								ph.transitionProbability.get("<s>|"
										+ tags.get(i)),
								ph.emissionProbability.get(firstWord + "|"
										+ tags.get(i)));
					else
						hs[0][i].maxValue = 0.0;
				} else {
					if (null != ph.emissionProbability.get("<unknown>" + "|"
							+ tags.get(i)))
						hs[0][i].maxValue = calcProbability(
								ph.transitionProbability.get("<s>|"
										+ tags.get(i)),
								ph.emissionProbability.get("<unknown>" + "|"
										+ tags.get(i)));
					else
						hs[0][i].maxValue = 0.0;
				}
				if (hs[0][i].maxValue != 0.0)
					System.out.println(tags.get(i) + " " + i);
			}
			int j = 1;
			Double cv = null;
			for (; j < n; j++) {
				String w = token.nextToken();
				Double max = null;
				int tag = 0;

				for (int i = 0; i < 45; i++) {
					max=null;

					for (int k = 0; k < 45; k++) {

						if (ph.wordVocabulary.contains(w)) {
							if (null != ph.emissionProbability.get(w + "|"
									+ tags.get(i)))
								cv = calcProbability(
										hs[j - 1][k].maxValue,
										ph.transitionProbability.get(tags
												.get(k) + "|" + tags.get(i)),
										ph.emissionProbability.get(w + "|"
												+ tags.get(i)));
							else
								cv = calcProbability(
										hs[j - 1][k].maxValue,
										ph.transitionProbability.get(tags
												.get(k) + "|" + tags.get(i)),
										0.0);
						} else {
							if (null != ph.emissionProbability.get("<unknown>"
									+ "|" + tags.get(i)))
								cv = calcProbability(
										hs[j - 1][k].maxValue,
										ph.transitionProbability.get(tags
												.get(k) + "|" + tags.get(i)),
										ph.emissionProbability.get("<unknown>"
												+ "|" + tags.get(i)));
							else
								cv = calcProbability(
										hs[j - 1][k].maxValue,
										ph.transitionProbability.get(tags
												.get(k) + "|" + tags.get(i)),
										0.0);

						}
						if (null != max) {
							if (cv > max) {
								max = cv;
								tag = k;
							}
						} else {
							max = cv;
							tag = k;
						}
					}
					hs[j][i] = new HiddenState();
					hs[j][i].maxValue = max;
					hs[j][i].tagList.addAll(hs[j - 1][tag].tagList);
					hs[j][i].tagList.add(tags.get(tag));
				}
			}
			Double max=null;
			int tag=0;
			for (int i = 0; i < 45; i++) {
			cv=calcProbability(hs[j-1][i].maxValue, ph.transitionProbability.get(tags.get(i)+"|<\\s>"));
			if (null != max) {
				if (cv > max) {
				max = cv;
				tag = i;
			}
			}
			else
			{
				max = cv;
				tag = i;
			}
			}

hs[j-1][tag].tagList.add(tags.get(tag));

StringTokenizer tokenSent=new StringTokenizer(temp, " ");

String output="";
int outCount=0;
while(tokenSent.hasMoreTokens())
{
	output+=tokenSent.nextToken()+"/"+hs[j-1][tag].tagList.get(outCount++)+" ";
	
}
buffWrite.append(output+"\n");
			}
			buffWrite.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static Double calcProbability(Double firstValue, Double secondValue) {
		return Math.exp(Math.log(firstValue) + Math.log(secondValue));
	}

	static Double calcProbability(Double firstValue, Double secondValue,
			Double thirdValue) {
		return Math.exp(Math.log(firstValue) + Math.log(secondValue)
				+ Math.log(thirdValue));
	}

}
