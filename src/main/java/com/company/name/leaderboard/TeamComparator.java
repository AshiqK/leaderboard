package com.company.name.leaderboard;

import java.util.Comparator;

public class TeamComparator implements Comparator<Team> {

	@Override
	public int compare(Team t1, Team t2) {
		
		//The greater the league points, the lower the  0 indexed position 0n the leaderboard.
		if (t1.getLeaguePoints() > t2.getLeaguePoints()) {
			return -1;
		} else if (t1.getLeaguePoints() < t2.getLeaguePoints()) {
			return 1;
		} else {
			// In the case of a draw, we compare the names instead of the league points.
			return t1.getName().compareTo(t2.getName());
		}

	}
}
