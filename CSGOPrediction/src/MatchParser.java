import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public abstract class MatchParser {
	protected Document currentPage;
	protected int currentMatchID = 0;
	protected  String outputFileName;
	
	protected final String BASEURL;
	
	public MatchParser(String baseURL) {
		this.BASEURL = baseURL;
		this.outputFileName = "output.csv";
	}
	
	/**
	 * Fetches the Document for a given matchID.
	 * @param matchID desired matchID to be loaded
	 */
	public void getPage(int matchID) {
		currentMatchID = matchID;
		String pageURL = BASEURL + currentMatchID;
		try {
			currentPage = Jsoup.connect(pageURL).userAgent(
					"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:34.0) Gecko/20100101 Firefox/34.0").get();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Increments currentMatchID and fetches the page.
	 */
	public void getNextMatch() {
		getPage(currentMatchID + 1);
	}
	
	/**
	 * Returns an int of the currentMatchID.
	 * @return the currentMatchID
	 */
	public int getCurrentMatchID() {
		return currentMatchID;
	}
	
	/**
	 * Adds the currentMatchID information to a file if outputName already exists, otherwise creates the file, 
	 * adds a header, and adds the currentMatchID information.
	 * @param outputFileName the name of the file to be produced or extended
	 */
	public void writeMatchToFile(String outputName) {
		// Try-with-resources for writing to file
		try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(outputName, true)))) {
			// If the file "output.csv" is empty add the header
			if (new File(outputName).length() > 0) {
				out.println(getTeam1Name() + ',' + getTeam2Name() + ',' + getMapName() + ',' + getScore() + 
						',' + getWinner() + ',' + getGameName() + ',' + getFormat() + ',' + 
						getTeam1Percentage() + ',' + getTeam2Percentage() + ',' + getDataSource() + 
						',' + getCurrentMatchID());
			} else {
				out.println("Team1Name,Team2Name,Map,Score,Winner,GameName,Format,Team1Bet,"
						+ "Team2Bet,DataSource,MatchID");
				out.println(getTeam1Name() + ',' + getTeam2Name() + ',' + getMapName() + ',' + getScore() + 
							',' + getWinner() + ',' + getGameName() + ',' + getFormat() + ',' + 
							getTeam1Percentage() + ',' + getTeam2Percentage() + ',' + getDataSource() + 
							',' + getCurrentMatchID());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds the currentMatchID information to a file if outputFileName already exists, otherwise creates the file, 
	 * adds a header, and adds the currentMatchID information.
	 */
	public void writeMatchToFile() {
		writeMatchToFile(outputFileName);
	}
	
	// Abstract Methods
	/**
	 * Returns a String of the source for match data for the currentMatchID.
	 * @return	name of the data source
	 */
	public abstract String getDataSource();
	
	/**
	 * Returns a String of the name of the Counter Strike version the match was on.
	 * @return name of the CS version used for the match
	 */
	public abstract String getGameName();
	
	/**
	 * Returns a String of the match format i.e. "Best of 3".
	 * @return name of match format
	 */
	public abstract String getFormat();
	
	/**
	 * Returns a String of the name of the map used in the currentMatchID.
	 * @return name of map
	 */
	public abstract String getMapName();
	
	/**
	 * Returns a String of the name of the first team in the currentMatchID.
	 * @return name of team 1
	 */
	public abstract String getTeam1Name();
	
	/**
	 * Returns a String of the name of the second team in the currentMatchID.
	 * @return name of team 2
	 */
	public abstract String getTeam2Name();
	
	/**
	 * Returns a String of the betting percentage of the first team in the currentMatchID.
	 * @return bet percentage of team 1
	 */
	public abstract String getTeam1Percentage();
	
	/**
	 * Returns a String of the betting percentage of the second team in the currentMatchID.
	 * @return bet percentage of team 2
	 */
	public abstract String getTeam2Percentage();
	
	/**
	 * Returns a String of the score for the currentMatchID.
	 * @return score of match
	 */
	public abstract String getScore();
	
	/**
	 * Returns a String of the name of the winner for the currentMatchID.
	 * @return team name of winner
	 */
	public abstract String getWinner();
}
