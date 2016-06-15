import java.util.ArrayList;

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

			public OrderedStackList removeRow(int s) {
				if(score == s){
					if(greater == null){
						if(lesser != null){
							lesser.greater = null;
							return lesser;
						} else {
							return null;
						}
					}

					if(lesser == null){
						if(greater != null){
							greater.lesser = null;
							return greater;
						} else {
							return null;
						}
					}

					lesser.greater = greater;
					greater.lesser = lesser;
					return lesser;


				} else if (s > score){
					if(greater != null){
						return greater.removeRow(s);
					} else {
						return null;
					}
				} else {
					if(lesser != null){
						return lesser.removeRow(s);
					} else {
						return null;
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
					return this;
				} else {
					return greater.getHighest();
				}
			}

			public OrderedStackList getLowest(){
				if(lesser == null){
					return this;
				} else {
					return lesser.getLowest();
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