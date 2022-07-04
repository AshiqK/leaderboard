package com.span.digital.leaderboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Team {
	
	private String name;
	private Integer leaguePoints = 0;
	private Integer matchScore;
	
	public void addPoints(int pointstoAdd) {
		this.setLeaguePoints(this.getLeaguePoints()+pointstoAdd);
	}

}
