package com.span.digital.leaderboard;

import java.io.BufferedReader;
import java.io.IOException;
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
	
    public static void main( String[] args ) throws IOException, TeamsNotOrderedException {
    	String filePath = args[0];
    	Leaderboard leaderboard =  new Leaderboard();
    	leaderboard.calculateAndPrintRankings(filePath);
    
    }

	public void calculateAndPrintRankings(String filePath) throws IOException, TeamsNotOrderedException {
    	List<LeagueMatch> parsedMatchResults = parseMatchResults(filePath);
    	List<Team> orderedAndRanked = calcualateRanking(parsedMatchResults);
    	formatAndPrintRankings(orderedAndRanked);
		
	}

	private List<LeagueMatch> parseMatchResults(String matchResulultsInputPath) throws IOException, TeamsNotOrderedException {
    	List<LeagueMatch> parsedMatchResults = new ArrayList<LeagueMatch>();
		
    	Path path = Paths.get(matchResulultsInputPath);
		BufferedReader reader = Files.newBufferedReader(path);
		String match = reader.readLine();
		while (match != null) {		
			LeagueMatch leagueMatch = new LeagueMatch();
			String[] matchResults = match.split(",");
			leagueMatch.setLeftTeam(extractResultsForTeam(matchResults[0]));
			leagueMatch.setRightTeam(extractResultsForTeam(matchResults[1]));
			parsedMatchResults.add(leagueMatch);
			match = reader.readLine();
		}
		
		return  parsedMatchResults;

	}

	

	private Team extractResultsForTeam(String matchResult) {
		Team participant = new Team();
		String[] matchResultTokens = matchResult.split(" ");
		String matchScore = matchResultTokens[matchResultTokens.length - 1];
		matchResultTokens = Arrays.copyOf(matchResultTokens, matchResultTokens.length - 1);

		String matchParticipantName = "";
		for (String token : matchResultTokens) {
			matchParticipantName = matchParticipantName + token + " ";
		}
		matchParticipantName = matchParticipantName.trim();

		participant.setName(matchParticipantName);
		participant.setMatchScore(Integer.valueOf(matchScore));
		return participant;
	}

	private List<Team> calcualateRanking(List<LeagueMatch> parsedMatchResults) throws IOException, TeamsNotOrderedException {

		
		for(LeagueMatch leagueMatch : parsedMatchResults) {

			Team leftTeam = leagueMatch.getLeftTeam();
			Team rightTeam = leagueMatch.getRightTeam();

			if (leftTeam.getMatchScore() > rightTeam.getMatchScore()) {
				updatePoints(leftTeam, 3);
				updatePoints(rightTeam, 0);
			} else if (rightTeam.getMatchScore() > leftTeam.getMatchScore()) {
				updatePoints(rightTeam, 3);
				updatePoints(leftTeam, 0);
			} else if ((rightTeam.getMatchScore() == leftTeam.getMatchScore())) {
				updatePoints(rightTeam, 1);
				updatePoints(leftTeam, 1);
			}

			
		}
		

		List<Team> teams = new ArrayList<>(leaderBoard.values());
		Collections.sort(teams, new TeamComparator());
		updateRankings(teams);
		return teams;

	}

	private void updatePoints(Team team, Integer points) {
		Team leaderboardEntry = leaderBoard.get(team.getName());
		if(leaderboardEntry == null) {
			leaderboardEntry = new Team();
			leaderboardEntry.setName(team.getName());    		
		}
		leaderboardEntry.addPoints(points);
		leaderBoard.put(leaderboardEntry.getName(), leaderboardEntry);		
		
	}

	private void updateRankings(List<Team> orderedTeamsList) throws TeamsNotOrderedException {
        final int FIRST_PLACE_RANKING = 1;
		Team previousLeaderboardEntry = null;
        for(Team currentLeaderBoardEntry: orderedTeamsList) {
        	
        	if(previousLeaderboardEntry == null) {
        		currentLeaderBoardEntry.setLeagueRanking(FIRST_PLACE_RANKING);
        	} else {

            	if(currentLeaderBoardEntry.getLeaguePoints() < previousLeaderboardEntry.getLeaguePoints()) {
            		currentLeaderBoardEntry.setLeagueRanking(previousLeaderboardEntry.getLeagueRanking()+1);
            	} else if(currentLeaderBoardEntry.getLeaguePoints() == previousLeaderboardEntry.getLeaguePoints()) {
            		currentLeaderBoardEntry.setLeagueRanking(previousLeaderboardEntry.getLeagueRanking());
            	} else {
            		throw new TeamsNotOrderedException();
            	}

        	}
        	
        	previousLeaderboardEntry = currentLeaderBoardEntry;

        	
        }
		
	}
	
	private void formatAndPrintRankings(List<Team> teams) {
        for(Team team: teams) {
       	 System.out.println(team.getLeagueRanking()+". " +team.getName()+", "+team.getLeaguePoints()+" pt"+(team.getLeaguePoints()==1?"":"s"));
        }
		
	}
    
    
}
