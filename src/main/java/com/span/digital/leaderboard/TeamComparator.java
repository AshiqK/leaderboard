package com.span.digital.leaderboard;

import java.util.Comparator;

public class TeamComparator implements Comparator<Team> {

	@Override
	public int compare(Team t1, Team t2) {
		
		//We want the 0 indexed position in the leaderboard to be numerically lower.

		if (t1.getLeaguePoints() > t2.getLeaguePoints()) {
			return -1;
		} else if (t1.getLeaguePoints() < t2.getLeaguePoints()) {
			return 1;
		} else {
			return t1.getName().compareTo(t2.getName());
		}

	}
}
