package com.span.digital.leaderboard;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class Leaderboard 
{
	private static final int WINNERS_POINTS = 3;
	private static final int LOSERS_POINTS = 0;
	private static final int TIE_POINTS = 1;
	private static final int TOP_RANK = 1;
	
	Map<String,Team> leaderBoard = new HashMap<String,Team>();
	
    public static void main( String[] args ) {
    	String filePath = args[0];
    	Leaderboard leaderboard =  new Leaderboard();
    	leaderboard.calculateAndPrintRankings(filePath);
    
    }

	public void calculateAndPrintRankings(String filePath) {
    	List<LeagueMatch> parsedMatchResults = parseMatchResults(filePath);
    	List<Team> orderedAndRanked = calcualateRanking(parsedMatchResults);
    	formatAndPrintRankings(orderedAndRanked);
		
	}

	private List<LeagueMatch> parseMatchResults(String matchResulultsInputPath) {
    	
		List<LeagueMatch> parsedMatchResults = new ArrayList<LeagueMatch>();
		
		try (Stream<String> stream = Files.lines(Paths.get(matchResulultsInputPath))) {
			
			parsedMatchResults = stream
					.map(Leaderboard::extractLeagueMatch)
					.collect(Collectors.toList());

		} catch (IOException ioe) {
			log.error("There was a problem reading the input file specified at location  "+matchResulultsInputPath+" .",ioe);
		} catch (Exception e) {
			log.error("An unexpected error occurred while processing the input specified at location  "+matchResulultsInputPath+" .",e);
		}
		
		return  parsedMatchResults;
		
	}

	private static LeagueMatch extractLeagueMatch(String unparsedLeagueMatchLine) {
		LeagueMatch leagueMatch = new LeagueMatch();
		String[] matchResults = unparsedLeagueMatchLine.split(",");
		leagueMatch.setLeftTeam(extractResultsForTeam(matchResults[0]));
		leagueMatch.setRightTeam(extractResultsForTeam(matchResults[1]));
		return leagueMatch;
	}

	private static Team extractResultsForTeam(String matchResult) {
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

	private List<Team> calcualateRanking(List<LeagueMatch> parsedMatchResults) {
		
		for(LeagueMatch leagueMatch : parsedMatchResults) {

			Team leftTeam = leagueMatch.getLeftTeam();
			Team rightTeam = leagueMatch.getRightTeam();

			if (leftTeam.getMatchScore() > rightTeam.getMatchScore()) {
				updatePoints(leftTeam, WINNERS_POINTS);
				updatePoints(rightTeam, LOSERS_POINTS);
			} else if (rightTeam.getMatchScore() > leftTeam.getMatchScore()) {
				updatePoints(rightTeam, WINNERS_POINTS);
				updatePoints(leftTeam, LOSERS_POINTS);
			} else if ((rightTeam.getMatchScore() == leftTeam.getMatchScore())) {
				updatePoints(rightTeam, TIE_POINTS);
				updatePoints(leftTeam, TIE_POINTS);
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

	private void updateRankings(List<Team> orderedTeamsList) {

		Team previousLeaderboardEntry = null;
        for(Team currentLeaderBoardEntry: orderedTeamsList) {
        	
        	if(previousLeaderboardEntry == null) {
        		currentLeaderBoardEntry.setLeagueRanking(TOP_RANK);
        	} else {

            	if(currentLeaderBoardEntry.getLeaguePoints() < previousLeaderboardEntry.getLeaguePoints()) {
            		currentLeaderBoardEntry.setLeagueRanking(previousLeaderboardEntry.getLeagueRanking()+1);
            	} else if(currentLeaderBoardEntry.getLeaguePoints() == previousLeaderboardEntry.getLeaguePoints()) {
            		currentLeaderBoardEntry.setLeagueRanking(previousLeaderboardEntry.getLeagueRanking());
            	} else {
            		String errorMessage = "Teams should be oredered by league points and name.";
            		log.error(errorMessage);
            		throw new RuntimeException(errorMessage);
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
