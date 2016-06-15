
public class Card {

	static final int incorrectMod = -3, correctMod = 1;
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
		return spanish + "/" + english + "/" + spnScore + "/" + engScore;
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