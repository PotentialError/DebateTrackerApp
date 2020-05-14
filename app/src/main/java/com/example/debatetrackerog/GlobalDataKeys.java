package com.example.debatetrackerog;

public class GlobalDataKeys {
    //integer used to tell MainMenu which fragment to go to on onCreate 0 = View History 1 = Edit Rounds -1 = Profile Fragment
    public static final String FRAG_REQUEST_EDIT_KEY = "MainMenuFrag";
    //String used to store all debate rounds locally
    public static final String ROUNDS_KEY = "rounds";
    //String used to store the user's default debate style
    public static final String DEBATE_STYLE_KEY = "Debate Style";
    //String users name
    public static final String NAME_KEY = "Name";
    //String users grade 8 = middle school 9-12 high school 13 adult
    public static final String GRADE_KEY = "Grade";
    //String users school
    public static final String USER_SCHOOL_KEY = "School Name";
    //boolean if true = simply trying to edit a round false = starting a round with timer
    public static final String IS_EDIT_KEY = "isEdit";
    //String users pronoun
    public static final String PRONOUN_KEY = "Pronoun";
    //String amount of years user has been debating
    public static final String YEARS_KEY = "Years Debating";
    //String fun fact of user
    public static final String FUN_FACT_KEY = "Fun Fact";
    //integer number of wins user has
    public static final String WIN_KEY = "Wins";
    //integer number of losses user has
    public static final String LOSS_KEY = "Loss";

}
