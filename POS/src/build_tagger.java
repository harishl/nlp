import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class build_tagger {
	public static void main(String args[]) {
		File file = new File("/home/user/NLP/a2_data/sents.train");
		FileReader read;
		BufferedReader buffRead;
		try {
			read = new FileReader(file);
			buffRead=new BufferedReader(read);
			String temp;
			int i=0;
			int j=0;
			int k=0;
			/*while(null!=(temp=buffRead.readLine()))
				i++;*/
	
			
			while(null!=(temp=buffRead.readLine())){
				StringTokenizer token=new StringTokenizer(temp," ");
				i++;
			while(token.hasMoreTokens())
			{j++;
				String temp1=token.nextToken();
			
				StringTokenizer anothertoken=new StringTokenizer(temp1,"/");
				while(anothertoken.hasMoreTokens())
				{
					String testToken=anothertoken.nextToken();
					if(testToken.equals("VBZ"))
					k++;	
				//System.out.println(testToken);
				}
			}}
			System.out.println(j);
			System.out.println(i);
			System.out.println(k);
		} catch (FileNotFoundException e) {
			System.out
					.println("The file path is wrong for training file so unable to complete the model");
			System.exit(0);

		}
		 catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
