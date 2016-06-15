import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class SpanishFlashcards {

	//TODO move score mods to one location, currently in main and Card
	static final int incorrectMod = -3, correctMod = 1;
	Scanner userIn;
	FileOutputStream out;
	final static String source = (new File("")).getAbsolutePath() + "\\ch11.txt";
	FlashcardStack cardStack;

	public SpanishFlashcards() {
		cardStack = new FlashcardStack();
		cardStack.createStack(source);

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
				cardStack.printSpnDist();
				break;
			case 6:
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
		case "P":
			return 5;
		case "X":
			return 6;
		default:
			System.out.println("Invalid selection, please choose again");
			return 0;
		}
	}

	private void spanToEng() {
		Random rand = new Random();
		OrderedStackList cur = cardStack.getSpanishLowest();
		ArrayList<Card> curCards = cur.getCards();
		Card curC; 
		int curSize = curCards.size();
		int randChoice;
		String userChoice = "";
		while(!userChoice.equals("-1")){
			if(!((curCards.size() > (curSize / 2)) || cur.equals(cardStack.getSpanishLowest())) || curCards.size() == 0){
				if(curCards.size() == 0){
					cardStack.removeSpanishRow(cur.getScore());
				}
				cur = cardStack.getSpanishLowest();
				curCards = cur.getCards();
				curSize = curCards.size();
			}
			if(curCards.size() > 1){
				randChoice = rand.nextInt(curCards.size() - 1);
			} else {
				randChoice = 0;
			}

			curC = curCards.get(randChoice);
			curCards.remove(randChoice);

			System.out.println(curC.getSpanish());
			userChoice = userIn.nextLine().toLowerCase();
			
			if(userChoice.equals("-1")){
				System.out.println("Exiting to Main Menu");
			}else if(userChoice.equals(curC.getEnglish().toLowerCase())){
				System.out.println("Correct!");
				curC.spnCorrect();
				cur.moveCard(curC, cur.getScore() + correctMod);
			} else {
				System.out.println("Incorrect, Correct Answer: " + curC.getEnglish());
				curC.spnIncorrect();
				cur.moveCard(curC, cur.getScore() + incorrectMod);
			}

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
		System.out.println("\tP  - Print Score Distribution");
		System.out.println("\tV  - Save");
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
			FileWriter out = new FileWriter(source);
			for(String c : cardStack.getCards()){
				out.write(c);
			}
			out.close();
		}catch(Exception e){
			System.out.println("Error accessing path: " + e);
		}
	}
}
