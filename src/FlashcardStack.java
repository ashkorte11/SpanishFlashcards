import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class FlashcardStack{

		OrderedStackList spanStack, engStack;
		Scanner in;
		
		public void createStack(String source) {
			try {
				in = new Scanner(new File(source));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			String[] inputStr;
			String eng, span;
			int engScore, spanScore;

			while(in.hasNext()){
				inputStr = in.nextLine().split("/");
				span = inputStr[0];
				eng = inputStr[1];
				spanScore = Integer.parseInt(inputStr[2]);
				engScore = Integer.parseInt(inputStr[3]);
				if(spanStack == null){
					spanStack = new OrderedStackList(null, null, spanScore, new Card(span,eng,spanScore,engScore));
				} else {
					spanStack.addCardSpan(new Card(span,eng,spanScore,engScore));
				}
				if(engStack == null){
					engStack = new OrderedStackList(null, null, spanScore, new Card(span,eng,spanScore,engScore));
				} else {
					engStack.addCardEng(new Card(span,eng,spanScore,engScore));
				}	
				getSpanishLowest().printScoreDist();
				System.out.println();
			}

			in.close();
		}

		public void printSpnDist() {
			spanStack.printScoreDist();
		}

		public void removeSpanishRow(int s) {
			spanStack = spanStack.getLowest().removeRow(s);
		}

		public OrderedStackList getSpanishHighest() {
			return spanStack.getHighest();
		}

		public OrderedStackList getSpanishLowest() {
			return spanStack.getLowest();
		}

		public void addCard(String spanish, String english){
			spanStack.addCardEng(new Card(spanish, english, 0, 0));
			engStack.addCardEng(new Card(spanish, english, 0, 0));
		}

		public ArrayList<String> getCards() {
			return spanStack.printStack();
		}
	}
