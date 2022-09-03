package com.company.name.leaderboard;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LeaderboardTest {

	Path resourceDirectory;
	Leaderboard leaderboard;
		
	private ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final PrintStream originalOut = System.out;
	
	
    @BeforeEach
    public void beforeEach() {
		leaderboard = new Leaderboard();
	    System.setOut(new PrintStream(outContent));
    }
    
    @AfterEach
    public void afterEach() {
    	outContent = new ByteArrayOutputStream();
	    System.setOut(originalOut);
    }
    
    @Test
    public void Sample_Data_Should_Match_Sample_Output() throws Exception {

		resourceDirectory = Paths.get("src","test","resources","scores_sample");		
		leaderboard.calculateAndPrintRankings(resourceDirectory.toString());
		assertEquals(outContent.toString(),"1. Tarantulas, 6 pts\n"
				+ "2. Lions, 5 pts\n"
				+ "3. FC Awesome, 1 pt\n"
				+ "3. Snakes, 1 pt\n"
				+ "4. Grouches, 0 pts\n"
				+ "");

    }
    
    @Test
    public void When_All_Teams_Rank_Equally_Leaderboard_Should_Be_Purely_In_Alphabetical_Order() throws Exception {
		resourceDirectory = Paths.get("src","test","resources","scores_draws");
		leaderboard.calculateAndPrintRankings(resourceDirectory.toString());
		assertEquals(outContent.toString(),"1. FC Awesome 22, 4 pts\n"
				+ "1. Grouches, 4 pts\n"
				+ "1. Lions, 4 pts\n"
				+ "1. Snakes, 4 pts\n"
				+ "1. Tarantulas, 4 pts\n"
				+ "");
    }
    
    
    @Test
    public void Losing_Teams_With_No_Wins_Should_Be_Ranked() throws Exception {

		resourceDirectory = Paths.get("src","test","resources","scores_teams_with_no_wins");
		leaderboard.calculateAndPrintRankings(resourceDirectory.toString());
		assertEquals(outContent.toString(),
				"1. Lions, 9 pts\n"
				+ "2. Tarantulas, 6 pts\n"
				+ "3. FC Awesome, 0 pts\n"
				+ "3. Grouches, 0 pts\n"
				+ "3. Snakes, 0 pts\n"
				+ "");
    	
    }
        
    @Test
    public void Teams_Can_Have_Numbers_In_Names() throws Exception {
		resourceDirectory = Paths.get("src","test","resources","scores_numbers_in_names");
		leaderboard.calculateAndPrintRankings(resourceDirectory.toString());		
		assertEquals(outContent.toString(),"1. Lions 777, 3 pts\n"
				+ "1. xXx T.T, 3 pts\n"
				+ "2. FC Awesome 22, 2 pts\n"
				+ "2. Grouches 0, 2 pts\n"
				+ "2. Snakes 888, 2 pts\n"
				+ "2. Tarantulas 2.4, 2 pts\n"
				+ "3. zzz 101010, 1 pt\n"
				+ "");
    }
}
