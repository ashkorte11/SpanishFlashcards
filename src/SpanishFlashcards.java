import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class SpanishFlashcards {

	static final int incorrectMod = -3, correctMod = 1;
	Scanner in, userIn;
	FileOutputStream out;
	final static String source = (new File("")).getAbsolutePath() + "\\wordBank.txt";
	FlashcardStack cardStack;

	public SpanishFlashcards() {
		cardStack = new FlashcardStack();
		cardStack.createStack();

		userIn = new Scanner(System.in);
		String userChoice = "";
		int userPos = 0;

		while(notDone(userChoice)){
			switch (userPos){
			case 0:
				printMenu();
				userPos = getResponse();
				break;
			case 1:
				addWord();
				userPos = 0;
				break;
			case 2:
				engToSpan();
				userPos = 0;
				break;
			case 3:
				spanToEng();
				userPos = 0;
				break;
			case 4:
				save();
				userPos = 0;
				break;
			case 5:
				System.exit(0);
				break;
			default:
				System.out.println("Something wrong with user position!");
			}

		}
	}

	private int getResponse() {
		System.out.print("Choose an option: ");
		String resp = userIn.nextLine().toUpperCase();
		switch (resp){
		case "A":
			return 1;
		case "E":
			return 2;
		case "S":
			return 3;
		case "V":
			return 4;
		case "X":
			return 5;
		default:
			System.out.println("Invalid selection, please choose again");
			return 0;
		}
	}

	private void spanToEng() {
		//TODO
		Random rand = new Random();
		FlashcardStack.OrderedStackList cur = cardStack.getSpanishHighest();
		ArrayList<FlashcardStack.Card> curCards = cur.getCards();
		FlashcardStack.Card curC; 
		int curSize = curCards.size();
		int randChoice;
		while(!in.equals("-1")){
			if(!((curCards.size() > (curSize / 2)) || cur.equals(cardStack.getSpanishHighest())) || curCards.size() == 0){
				if(curCards.size() == 0){
					cardStack.removeSpanishRow(cur.getScore());
				}
				cur = cardStack.getSpanishHighest();
				curCards = cur.getCards();
				curSize = curCards.size();
				System.out.println(cur.getScore());
			}
			if(curCards.size() > 1){
				randChoice = rand.nextInt(curCards.size() - 1);
			} else {
				randChoice = 0;
			}

			curC = curCards.get(randChoice);
			curCards.remove(randChoice);

			System.out.println(curC.getSpanish());
			if(userIn.nextLine().toLowerCase().equals(curC.getEnglish())){
				System.out.println("Correct!");
				curC.spnCorrect();
				cur.moveCard(curC, cur.getScore() + correctMod);
			} else {
				System.out.println("Incorrect");
				curC.spnIncorrect();
				cur.moveCard(curC, cur.getScore() + incorrectMod);
			}
			
			cardStack.getSpanishHighest().printScoreDist();
		}
	}

	private void engToSpan() { 
		// TODO Auto-generated method stub

	}

	private void addWord() {
		System.out.print("Spanish: ");
		String spanish = userIn.nextLine().toLowerCase();
		System.out.print("English: ");
		String english = userIn.nextLine().toLowerCase();
		cardStack.addCard(spanish, english);
	}

	private void printMenu() {
		System.out.println("Spanish Flashcards\n---------------------");
		System.out.println("Please select an option:");
		System.out.println("\tA  - Add Card");
		System.out.println("\tE  - English to Spanish");
		System.out.println("\tS  - Spanish to English");
		System.out.println("\tV - Save");
		System.out.println("\tX  - Exit");
	}

	public boolean notDone(String input){
		try{
			if(Integer.parseInt(input) == -1){
				return false;
			}
			return true;
		} catch (NumberFormatException e){
			return true;
		}
	}

	public static void main(String[] args) {
		new SpanishFlashcards();
	}

	public void save(){
		try{
			//			Files.delete(FileSystems.getDefault().getPath(source));
			//			File file = new File(source);
			FileWriter out = new FileWriter(source);
			for(String c : cardStack.getCards()){
				out.write(c);
			}
			out.close();
		}catch(Exception e){
			System.out.println("Error accessing path: " + e);
		}
	}


	public class FlashcardStack{

		OrderedStackList spanStack, engStack;

		private void createStack() {
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
				getSpanishHighest().printScoreDist();
				System.out.println();
			}

			in.close();
		}

		public void removeSpanishRow(int s) {
			// TODO Auto-generated method stub
			spanStack.getHighest().removeRow(s);
			
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

		public class OrderedStackList {
			OrderedStackList greater, lesser;
			ArrayList<Card> cards;
			int score;

			public OrderedStackList(OrderedStackList g, OrderedStackList l, int s, Card c) {
				greater = g;
				lesser = l;
				score = s;
				cards = new ArrayList<Card>();
				cards.add(c);
			}

			public void removeRow(int s) {
				if(score == s){
					if(greater != null){
						lesser.greater = greater;
					} else {
						lesser.greater = null;
					}
					
					if(lesser != null){
						greater.lesser = lesser;
					} else {
						greater.lesser = null;
					}
				}
			}

			public void printScoreDist() {
				if(lesser == null){
					System.out.println(score + " " + cards.size());
				} else {
					System.out.println(score + " " + cards.size());
					lesser.printScoreDist();
				}
			}

			public void moveCard(Card c, int target) {
				if(target == score){
					cards.add(c);
				} else if(target > score){
					if(greater != null){
						if(greater.getScore() > target && target > score){
							OrderedStackList temp = new OrderedStackList(greater, this, target, c);
							greater.setLesser(temp);
							greater = temp;
						} else {
							greater.moveCard(c, target);
						}
					} else {
						greater = new OrderedStackList(null, this, target, c);
					}
				} else {
					if(lesser != null){
						if(lesser.getScore() < target && target < score){
							OrderedStackList temp = new OrderedStackList(this, lesser, target, c);
							lesser.setGreater(temp);
							lesser = temp;
						} else {
							lesser.moveCard(c, target);
						}
					} else {
						lesser = new OrderedStackList(this, null, target, c);
					}
				}
			}

			private void setLesser(OrderedStackList l) {
				lesser = l;
			}

			private void setGreater(OrderedStackList g) {
				greater = g;
			}

			public ArrayList<Card> getCards(){
				return cards;
			}

			public int getScore(){
				return score;
			}

			public OrderedStackList getHighest(){
				if(greater == null){
					if(cards.size() == 0){
						return lesser;
					}
					return this;
				} else {
					if(greater.hasGreater()){
						return greater.getHighest();
					} else {
						if(greater.getSize() == 0){
							return this;
						} else {
							return greater.getHighest();
						}
					}
				}
			}
			
			public OrderedStackList getLowest(){
				if(lesser == null){
					if(cards.size() == 0){
						return greater;
					}
					return this;
				} else {
					if(lesser.hasLesser()){
						return lesser.getLowest();
					} else {
						if(lesser.getSize() == 0){
							return this;
						} else {
							return lesser.getLowest();
						}
					}
				}
			}


			private int getSize() {
				return cards.size();
			}

			private boolean hasGreater() {
				if(greater == null){
					return false;
				}
				return true;
			}

			private boolean hasLesser() {
				if(lesser == null){
					return false;
				}
				return true;
			}
			
			public ArrayList<String> printStack(){
				if(greater == null){
					return printStack(new ArrayList<String>());
				} else {
					return greater.printStack();
				}
			}

			public ArrayList<String> printStack(ArrayList<String> p){
				if(lesser == null){
					return p;
				} else {
					for(Card c: cards){
						p.add(c.toString() + "\n");
					}
					return lesser.printStack(p);
				}
			}

			public void addCardSpan(Card c){
				int newScore = c.getSpanScore();
				if(newScore == score){
					cards.add(c);
				} else if(newScore > score){
					if(greater == null){
						greater = new OrderedStackList(null, this, newScore, c);
					} else {
						if(greater.score > newScore){
							OrderedStackList newG = new OrderedStackList(this.greater, this, newScore, c);
							greater.lesser = newG;
							greater = newG;
						} else {
							greater.addCardSpan(c);
						}
					}
				} else {
					if(lesser == null){
						lesser = new OrderedStackList(this, null, newScore, c);
					} else {
						if(lesser.score < newScore){
							OrderedStackList newG = new OrderedStackList(this, this.lesser, newScore, c);
							lesser.greater = newG;
							lesser = newG;
						} else {
							lesser.addCardSpan(c);
						}
					}
				}
			}

			public void addCardEng(Card c){
				int newScore = c.getEngScore();
				if(newScore == score){
					cards.add(c);
				} else if(newScore > score){
					if(greater == null){
						greater = new OrderedStackList(null, this, newScore, c);
					} else {
						if(greater.score != newScore){
							OrderedStackList newG = new OrderedStackList(this.greater, this, newScore, c);
							greater.lesser = newG;
							greater = newG;
						} else {
							greater.addCardEng(c);
						}
					}
				} else {
					if(lesser == null){
						lesser = new OrderedStackList(this, null, newScore, c);
					} else {
						if(lesser.score != newScore){
							OrderedStackList newG = new OrderedStackList(this, this.lesser, newScore, c);
							lesser.greater = newG;
							lesser = newG;
						} else {
							lesser.addCardEng(c);
						}
					}
				}
			}
		}

		private class Card {

			String spanish, english;
			int spnScore, engScore;

			public Card(String spn, String eng, int spnS, int engS){
				spanish = spn;
				english = eng;
				spnScore = spnS;
				engScore = engS;
			}

			public int getEngScore() {
				return engScore;
			}

			public int getSpanScore() {
				return spnScore;
			}

			public String toString(){
				return spanish + " :|: " + english + " :|: " + spnScore + " :|: " + engScore;

			}

			public String getSpanish(){
				return spanish;
			}

			public String getEnglish(){
				return english;
			}

			public void spnIncorrect(){
				spnScore += incorrectMod;
			}

			public void spnCorrect(){
				spnScore += correctMod;
			}

			public void engIncorrect(){
				engScore += incorrectMod;
			}

			public void engCorrect(){
				engScore += correctMod;
			}
		}
	}
}
