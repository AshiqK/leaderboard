package com.span.digital.leaderboard;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class Leaderboard 
{
	Map<String,Team> leaderBoard = new HashMap<String,Team>();	
	private Path pathToSourceFile;
	
    public static void main( String[] args ) throws URISyntaxException, IOException {
    	String filePath = args[0];
    	Leaderboard leaderboard =  new Leaderboard();
    	Path path = Paths.get(filePath);
    	leaderboard.setPathToSourceFile(path);
    	List<Team> teams = leaderboard.calcualateRanking();
    	leaderboard.formatAndPrintRankings(teams);
    
    }

	public List<Team> calcualateRanking() throws IOException {


        BufferedReader reader = Files.newBufferedReader(this.getPathToSourceFile());
        String match = reader.readLine();
        while(match != null) {
        	List<Team> matchParticipants = new ArrayList<Team>();
        	String[] matchResults = match.split(",");
        	for(String matchResult: matchResults) {
        		Team participant = new Team();
        		String[] matchResultTokens = matchResult.split(" ");
        		String matchScore = matchResultTokens[matchResultTokens.length-1];
        		matchResultTokens = Arrays.copyOf(matchResultTokens, matchResultTokens.length-1);
        		
        		String matchParticipantName = "";
        		for(String token : matchResultTokens) {
        			matchParticipantName = matchParticipantName+token+" ";
        		}
        		matchParticipantName = matchParticipantName.trim();
        		
        		participant.setName(matchParticipantName);
        		participant.setMatchScore(Integer.valueOf(matchScore));
        		matchParticipants.add(participant);
        	}
        	Team[] matchPaticipantsArr = matchParticipants.toArray(new Team[matchParticipants.size()]);        	
        	Team leftTeam = matchPaticipantsArr[0];
        	Team rightTeam = matchPaticipantsArr[1];
        	        	
        	if(leftTeam.getMatchScore()>rightTeam.getMatchScore()) {
        		updateLeaderBoard(leftTeam,3);
        	} else if(rightTeam.getMatchScore()>leftTeam.getMatchScore()) {
        		updateLeaderBoard(rightTeam,3);
        	} else if((rightTeam.getMatchScore() == leftTeam.getMatchScore())) {
        		updateLeaderBoard(rightTeam,1);
        		updateLeaderBoard(leftTeam,1);
        	}
        	
        	match = reader.readLine();
        }
         
         List<Team> teams = new ArrayList<>(leaderBoard.values());
         Collections.sort(teams,new TeamComparator());
         return teams;

	}

	private void updateLeaderBoard(Team team, Integer points) {
		Team leaderboardEntry = leaderBoard.get(team.getName());
		if(leaderboardEntry == null) {
			leaderboardEntry = new Team();
			leaderboardEntry.setName(team.getName());    		
		}
		leaderboardEntry.addPoints(points);
		leaderBoard.put(leaderboardEntry.getName(), leaderboardEntry);		
		
	}

	public void formatAndPrintRankings(List<Team> teams) {
        int position = 1;
        for(Team team: teams) {
       	 System.out.println(position+". " +team.getName()+", "+team.getLeaguePoints()+" pt"+(team.getLeaguePoints()==1?"":"s"));
       	 position++;
        }
		
	}
    
    
}
