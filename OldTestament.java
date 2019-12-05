package javafx.example;

public class OldTestament {

	public static final int numOfChapters=187;
	public static final int avgCharactersInChapter=2000;
	public static final int GensisChapters=50;
	public static final int ExodusChapters=40;
	public static final int LeviticusChapters=27;
	public static final int NumbersChapters=36;
	public static final int DeuteronomyChapters=34;
	private String oldTestament;
	
	public OldTestament(String str)
	{
		oldTestament=str;
	}
	public String getOldTestament()
	{
		return oldTestament;
	}
	public String toString()
	{
		return String.format(oldTestament);
	}
}
