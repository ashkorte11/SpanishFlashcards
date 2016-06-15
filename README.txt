# SpanishFlashcards
Intended to be an Android application, this app provides Spanish flashcards for the user. The user will be able to chose from select chapters from the Vistas Spanish textbook, or they can enter their own word to practice. Flashcards are selected based on their score, the lowest score cards are selected. Wrong answers drops a words score by a greater factor that correct answers raises the score. 
Selected word sets are stored in doubly linked lists of array lists of cards where each linked list node contains all words that share a score. Cards are first pulled randomly from the list with the lowest score. When a card it pulled it is moved to the list that matches its new score. When half of the cards are pulled from that list, if there are any cards that were answered incorrectly and there is now a lower score the selected list is moved to this new lowest score. This continues in this way until the user answers all cards correctly in that score, at which point the selected list moves to the next highest score.

TODO
1) Separate classes
2) Document code, make more readable
3) Add English to Spanish
4) Add word category selection
5) Add adding new words to selected category functionality
6) Migrate to Android
7) Work on Android interface
