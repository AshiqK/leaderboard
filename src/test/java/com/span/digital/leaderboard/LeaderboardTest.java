package com.span.digital.leaderboard;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LeaderboardTest {

	Path resourceDirectory;
	Leaderboard leaderboard;
		
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final PrintStream originalOut = System.out;
	
	
    @BeforeEach
    public void beforeEach() {
		leaderboard = new Leaderboard();
    }
    
    @Test
    public void Sample_Data_Should_Match_Sample_Output() throws Exception {
	    System.setOut(new PrintStream(outContent));
		resourceDirectory = Paths.get("src","test","resources","scores_sample");
		leaderboard.setPathToSourceFile(resourceDirectory);
		List<Team> rankings = leaderboard.calcualateRanking();
		leaderboard.formatAndPrintRankings(rankings);
		assertEquals(outContent.toString(),"1. Tarantulas, 6 pts\n"
				+ "2. Lions, 5 pts\n"
				+ "3. FC Awesome, 1 pt\n"
				+ "4. Snakes, 1 pt\n"
				+ "");
	    System.setOut(originalOut);
    }
    
    @Test
    public void When_All_Teams_Rank_Equally_Leaderboard_Should_Be_Purely_In_Alphabetical_Order() throws Exception {
	    System.setOut(new PrintStream(outContent));
		resourceDirectory = Paths.get("src","test","resources","scores_draws");
		leaderboard.setPathToSourceFile(resourceDirectory);
		List<Team> rankings = leaderboard.calcualateRanking();
		leaderboard.formatAndPrintRankings(rankings);
		assertEquals(outContent.toString(),"1. FC Awesome 22, 4 pts\n"
				+ "2. Grouches, 4 pts\n"
				+ "3. Lions, 4 pts\n"
				+ "4. Snakes, 4 pts\n"
				+ "5. Tarantulas, 4 pts\n"
				+ "");
	    System.setOut(originalOut);
    }
    
    @Test
    public void Teams_Can_Have_Numbers_In_Names() throws Exception {
	    System.setOut(new PrintStream(outContent));
		resourceDirectory = Paths.get("src","test","resources","scores_numbers_in_names");
		leaderboard.setPathToSourceFile(resourceDirectory);
		List<Team> rankings = leaderboard.calcualateRanking();
		leaderboard.formatAndPrintRankings(rankings);
		assertEquals(outContent.toString(),"1. Lions 777, 3 pts\n"
				+ "2. xXx T.T, 3 pts\n"
				+ "3. FC Awesome 22, 2 pts\n"
				+ "4. Grouches 0, 2 pts\n"
				+ "5. Snakes 888, 2 pts\n"
				+ "6. Tarantulas 2.4, 2 pts\n"
				+ "7. zzz 101010, 1 pt\n"
				+ "");
	    System.setOut(originalOut);
    }
}
