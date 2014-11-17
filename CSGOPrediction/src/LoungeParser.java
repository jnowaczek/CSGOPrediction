import org.jsoup.nodes.Element;


public class LoungeParser extends MatchParser {

	public LoungeParser() {
		super("http://csgolounge.com/match?m=");
		this.outputFileName = "CSGOL.csv";
	}
	
	public Element getMainDiv() {
		return currentPage.select("main").first();
	}
	
	public String getMainDivHTML() {
		return getMainDiv().html();
	}
	
	public Element getGradientSection() {
		return getMainDiv().select("section.box").first();
	}
	
	public String getGradientSectionHTML() {
		return getGradientSection().html();
	}
	
	public String getFormat() {
		try {
			return getGradientSection().select("div.half").get(1).text();
		} catch (Exception e){
			return "Error";
		}
	}
	
	public String getTeam1Name() {
		try {
			String rawName = getGradientSection().select("b").get(0).text();
			if (rawName.contains("(win)")) {
				return rawName.substring(0, rawName.length() - 6);
			}
			return rawName;
		} catch (Exception e) {
			return "Error";
		}
	}
	
	public String getTeam2Name() {
		try {
			String rawName = getGradientSection().select("b").get(1).text();
			if (rawName.contains("(win)")) {
				return rawName.substring(0, rawName.length() - 6);
			}
			return rawName;
		} catch (Exception e) {
			return "Error";
		}
	}
	
	public String getTeam1Percentage() {
		try {
			return getGradientSection().select("i").get(0).text();
		} catch (Exception e) {
			return "Error";
		}
	}
	
	public String getTeam2Percentage() {
		try {
			return getGradientSection().select("i").get(1).text();
		} catch (Exception e) {
			return "Error";
		}
	}
	
	public String getWinner() {
		try {
			if (getGradientSection().select("b").get(0).text().contains("(win")) {
				return getTeam1Name();
			} else if (getGradientSection().select("b").get(1).text().contains("(win")) {
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
		return "CSGOL";
	}

	@Override
	public String getGameName() {
		return "CS: GO";
	}

	@Override
	public String getMapName() {
		return "Unavailable";
	}

	@Override
	public String getScore() {
		return "Unavailable";
	}
	
	public static void main(String[] args) {
		LoungeParser instance = new LoungeParser();
		for (int i = 1; i < 1682; i++) {
			instance.getPage(i);
			instance.writeMatchToFile();
			System.out.println("Currently processing match ID " + i);
		}
	}
}
