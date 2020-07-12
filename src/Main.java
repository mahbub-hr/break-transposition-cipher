import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String cipher; //= "ERLENAOFALGNOGIPIAMAPRSOXEYCERTRVNTTINHSRLTITNIOEXRMLLGTRIWDATRTROLRBFADIVXTDWONAOOEHHELIEUTSBNEZVNSHAIMIDMWDOEUMNAPWTOGRAIMX";
		BufferedReader br = new BufferedReader(new FileReader("transposition-36.txt"));
		cipher = br.readLine();br.readLine();
		String string = br.readLine();
		String []w = string.split(", ");
		for(int i = 0; i< w.length; i++) {w[i]=w[i].trim();System.out.println(w[i]);}
		System.out.println(cipher);
		
		
		System.out.println("size: "+cipher.length()+"\nColumn: ");
		List<Integer> factor = new ArrayList<Integer>();
		for(int i = cipher.length()/2; i >=2 ; i--) {
			if(cipher.length()%i == 0) {
				System.out.format(" %d  ", i);
				factor.add(i);
			}
		}
		
		System.out.println(factor);
		List <String> strings2 = null;
		ArrayList<Integer> l = null;
		boolean isFinished = false;
		
		for(int a =0; a < factor.size(); a++) {
			
			int len = factor.get(a);
			int column = cipher.length()/len;
			List<String> strings= splitEqually(cipher, len);
			
			for(int i = 0; i< strings.size(); i++) System.out.println(strings.get(i));
			
			int list [] =new int[column]; for(int b=0; b<list.length; b++) list[b]=b;
			
			ArrayList<ArrayList<Integer>> per = permuteList(list);
			
			//iterate over all permutation			
			for(int i = per.size()-1; i >= 0; i--) {
				
				l = per.get(i);
				strings2 = new ArrayList<String>(strings.size());
				//System.out.println(l+" "+i);          //81
				//permuting strings and adding to strings2
				for(int j = 0; j < l.size(); j++) {			 
					strings2.add(j,strings.get(l.get(j)));
				}
				System.out.println("I: "+i);
				for(int p = 0; p< strings2.size(); p++) System.out.println(strings2.get(p));
				//finding a word
				int numOftrue=0;
				for(int wIdx = 0; wIdx<w.length; wIdx++) {
					if(findWord(w[wIdx], strings2) == true) numOftrue++;
				}
				if(numOftrue == w.length) {isFinished = true; break;}
				
			}
			
			if(isFinished == true ) break;
			
		}
		
		//************outputting to file****************
		String str = "";
		for(int j =0; j<strings2.get(0).length(); j++) {
			
			for(int i =0; i<strings2.size(); i++) {
				str+=Character.toLowerCase(strings2.get(i).charAt(j));
				
			}
		}
		
		PrintWriter writer = new PrintWriter("output.txt", "UTF-8");
		writer.println(str);
		writer.print("Key length: "); writer.println(l.size()); for(int i =0; i<l.size(); i++) l.set(i, l.get(i)+1);
		writer.println(l.toString());
		writer.flush();
		writer.close();
		
		System.out.println("\n");
		//for(int i = 0; i< strings2.size(); i++) System.out.println(strings2.get(i));
		System.out.println(str);
		
		String str2 =Encrypt(str, l);
		int count =0;
		for(int i =0; i< cipher.length(); i++) {
			if(cipher.charAt(i) == str2.charAt(i)) count++;
		}
		
		double percentAccuracy = count*100.0/cipher.length();
		System.out.println(percentAccuracy);
	}

	public static List<String> splitEqually(String str, int size) {
	    // Give the list the right capacity to start with.
	    List<String> ret = new ArrayList<String>((str.length() + size - 1) / size);

	    for (int start = 0; start < str.length(); start += size) {
	        ret.add(str.substring(start, Math.min(str.length(), start + size)));
	    }
	    return ret;
	}
	
	static int contains(List<String> strings,char c, int index, int next) {
		for(int i = next; i<strings.size(); i++) {
			char ch = strings.get(i).charAt(index);
			if(Character.toUpperCase(ch) == c || Character.toLowerCase(ch) == c) return i;
		}
		
		return -1;
	}
	
	static boolean findWord(String word, List<String> strings2) {
		
		boolean isFinished = false;
		int next =0;
		for(int j = 0 ; j< strings2.get(0).length(); j++ ) {
			
			int idx = contains(strings2, word.charAt(0), j, next);
			if(idx == -1) {next =0;continue;}
			int n = j;
			if(idx == strings2.size()-1) {n++; if(n >= strings2.get(0).length()) return false;}
			int k =1;

			for(; k<word.length(); k++) {
				idx = (idx+1)%strings2.size();
				char c1 = Character.toUpperCase(strings2.get(idx).charAt(n));
				char c2 = Character.toUpperCase(word.charAt(k));
				if(c1 != c2) {
					next =idx;
					if(n==j) j--;
					break;
				}
				if(idx == strings2.size()-1) {n++;if(n >= strings2.get(0).length()) return false;}
			}
			if(k == word.length()) { return true;}
		}
		return false;
	}
	
	//********** permutation *************//
	public static ArrayList<ArrayList<Integer>> permuteList(int[] Num) {
		ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
	 
		//start from an empty list
		result.add(new ArrayList<Integer>());
	 
		for (int i = 0; i < Num.length; i++) {
			//list of list in current iteration of the array num
			ArrayList<ArrayList<Integer>> now = new ArrayList<ArrayList<Integer>>();
	 
			for (ArrayList<Integer> list : result) {
				// # of locations to insert is largest index + 1
				for (int j = 0; j < list.size()+1; j++) {
					// + add num[i] to different locations
					list.add(j, Num[i]);
					ArrayList<Integer> temp = new ArrayList<Integer>(list);
					now.add(temp);
					list.remove(j);
				}
			}
	 
			result = new ArrayList<ArrayList<Integer>>(now);
		}
	 
		return result;
	}
	
	static String Encrypt(String str, List<Integer> list) {
		
		System.out.println(list);
		int row = str.length()/list.size();
	    String[] strings = new String[row];
	    int i = 0;
		for(int j = 0; j< strings.length; j++) {
			strings[j] = new String("");
			//System.out.print(str.charAt(i));
			for(;i < (j+1)*list.size(); i++) {
				
				strings[j]+=str.charAt(i);
			}
			//System.out.println();
		}
		
		String string = new String("");
		for(int k =1; k<=list.size(); k++ ) {
			int idx = list.indexOf(k);
			for(int j = 0; j< strings.length; j++) {
				string+=strings[j].charAt(idx);
			}
		}
		String st=string.toUpperCase();
		System.out.println(st);
		return st;
	}
	
}
