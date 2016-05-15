import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;


public class SpanishFlashcards {

	Scanner in, userIn;
	FileOutputStream out;
	final String source = "C:/Users/kkort_000/workspace/SpanishFlashcards/wordBank.txt";
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

		for(Card c : cardStack){
			System.out.println(c);
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
		String input = "";
		ArrayList<Integer> done = new ArrayList<Integer>();
		int next = -1;
		Random randGen = new Random();
		while(!input.equals("-1")){
			if(done.size() == cardStack.size()){
				done = new ArrayList<Integer>();
			}

			do{
				next = randGen.nextInt(cardStack.size());
			}while(done.contains(next));

			System.out.print(cardStack.get(next).getSpanish() + ":  ");
			input = userIn.nextLine().toLowerCase();
			if(input.equals("-1")){
				System.out.println("Back to Menu");
			}else{
				if(input.equals(cardStack.get(next).getEnglish())){
					System.out.println("Correct!");
					cardStack.get(next).spnCorrect();
				} else {
					System.out.println("Incorrect! Correct Answer: " + cardStack.get(next).getEnglish());
					cardStack.get(next).spnIncorrect();
				}
			}
			done.add(next);
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
		cardStack.add(new Card(spanish, english, 0, 0));
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

	public int getNewSpanishScore() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void save(){
		try{
			//			Files.delete(FileSystems.getDefault().getPath(source));
			//			File file = new File(source);
			FileWriter out = new FileWriter(source);
			for(Card c : cardStack.getCards()){
				out.write(c.toString() + "\n");
			}
			out.close();
		}catch(Exception e){
			System.out.println("Error accessing path: " + e);
		}
	}


	public class FlashcardStack{

		OrderedStackList spanStack, engStack;

		public FlashcardStack(){

		}

		private void createStack() {
			try {
				in = new Scanner(new File(source));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			String[] inputStr;
			while(in.hasNext()){
				inputStr = in.nextLine().split("|");
				if(spanStack == null){
					spanStack = new OrderedStackList(null, null, Integer.parseInt(inputStr[2]), new Card(inputStr[0],inputStr[1],Integer.parseInt(inputStr[2]),Integer.parseInt(inputStr[3])));
				} else {
					spanStack.addCardSpan(new Card(inputStr[0],inputStr[1],Integer.parseInt(inputStr[2]),Integer.parseInt(inputStr[3])));
				}
				if(engStack == null){
					engStack = new OrderedStackList(null, null, Integer.parseInt(inputStr[2]), new Card(inputStr[0],inputStr[1],Integer.parseInt(inputStr[2]),Integer.parseInt(inputStr[3])));
				} else {
					engStack.addCardEng(new Card(inputStr[0],inputStr[1],Integer.parseInt(inputStr[2]),Integer.parseInt(inputStr[3])));
				}		
			}

			in.close();
		}

		public ArrayList<Card> getCards() {
			return null;
		}

		private class OrderedStackList {
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

			public void addCardSpan(Card c){
				int newScore = c.getSpanScore();
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
							greater.addCardSpan(c);
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

			static final int incorrectMod = 3, correctMod = 1;
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
				return spanish + "-" + english + "-" + spnScore + "-" + engScore;

			}
			public String getSpanish(){
				return spanish;
			}

			public String getEnglish(){
				return english;
			}

			public void spnIncorrect(){
				spnScore -= incorrectMod;
			}

			public void spnCorrect(){
				spnScore += correctMod;
			}

			public void engIncorrect(){
				engScore -= incorrectMod;
			}

			public void engCorrect(){
				engScore += correctMod;
			}
		}
	}
}
