import org.jsoup.nodes.Element;


public class HLTVParser extends MatchParser {
	
	public HLTVParser() {
		super("http://www.hltv.org/?pageid=188&matchid=");
		this.outputFileName = "HLTV.csv";
	}
	
	public Element getMainDiv() {
		return currentPage.select("div.centerFade").first();
	}
	
	public String getMainDivHTML() {
		return getMainDiv().html();
	}
	
	public Element getGameInfoSection() {
		return getMainDiv().select("div.covGroupBoxContent").first();
	}
	
	public String getGameInfoSectionHTML() {
		return getGameInfoSection().html();
	}
	
	public String getGameName() {
		int index;
		switch (getPageVariant()) {
			case "new":
				index = 6;
				break;
			case "old":
				index = 8;
				break;
			default:
				return "Error";
		}
		
		try {
			String raw = getGameInfoSection().select("div.covSmallHeadline").get(index).text();
			if (raw.contains("Global Offensive")) {
				return "CS: GO";
			} else {
				return "CS 1.6";
			}
		} catch (Exception e){
			return "Error";
		}
	}
	
	public String getMapName() {
		try {
			return getGameInfoSection().select("div.covSmallHeadline").get(2).text();
		} catch (Exception e){
			return "Error";
		}
	}
	
	public String getPageVariant() {
		if (currentPage.text().contains("Score at 16 rounds")) {
			return "old";
		} else {
			return "new";
		}
	}
	
	public String getTeam1Name() {
		int index;
		switch (getPageVariant()) {
		case "new":
			index = 10;
			break;
		case "old":
			index = 12;
			break;
		default:
			return "Error";
	}
		
		try {
			return getGameInfoSection().select("div.covSmallHeadline").get(index).text();
		} catch (Exception e) {
			return "Error";
		}
	}
	
	public String getTeam2Name() {
		int index;
		switch (getPageVariant()) {
		case "new":
			index = 12;
			break;
		case "old":
			index = 14;
			break;
		default:
			return "Error";
	}
		
		try {
			return getGameInfoSection().select("div.covSmallHeadline").get(index).text();
		} catch (Exception e) {
			return "Error";
		}
	}
	
	public String getScore() {
		int index;
		switch (getPageVariant()) {
		case "new":
			index = 8;
			break;
		case "old":
			index = 10;
			break;
		default:
			return "Error";
	}
		
		try {
			return getGameInfoSection().select("div.covSmallHeadline").get(index).text();
		} catch (Exception e) {
			return "Error";
		}
	}
	
	public String getWinner() {
		try {
			String score = getScore();
			int team1Score = 0;
			int team2Score = 0;
			score = score.replaceAll("[()]", "");
			String[] halfScores = score.split("\\s");
			String[] totalScore = halfScores[0].split(":");
			team1Score = Integer.parseInt(totalScore[0]);
			team2Score = Integer.parseInt(totalScore[1]);
			
			if (team1Score == team2Score) {
				return "Tie";
			} else if (team1Score > team2Score) {
				return getTeam1Name();
			} else if (team2Score > team1Score) {
				return getTeam2Name();
			} else {
				return "Unavailable";
			}
		} catch (Exception e) {
			return "Error";
		}
	}
	
	@Override
	public String getDataSource() {
		return "HLTV";
	}

	@Override
	public String getFormat() {
		return "Unavailable";
	}

	@Override
	public String getTeam1Percentage() {
		return "Unavailable";
	}

	@Override
	public String getTeam2Percentage() {
		return "Unavailable";
	}
	
	public static void main(String[] args) {
		HLTVParser instance = new HLTVParser();
		for (int i = 1; i < 18598; i++) {
			instance.getPage(i);
			instance.writeMatchToFile();
			System.out.println("Currently processing match ID " + i);
		}
	}
}
