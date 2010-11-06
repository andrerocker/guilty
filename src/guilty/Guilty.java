package guilty;

import guilty.manager.GuiltyManager;

public class Guilty 
{
	public static void main(String[] args) throws Exception
	{
		GuiltyManager manager = new GuiltyManager();
		manager.downloadManga("naruto");
	}
}
