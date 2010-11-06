package guilty.manager.thread;

import guilty.manager.GuiltyManager;

public class GuiltyWorker implements Runnable 
{
	private GuiltyManager manager;
	private String manga;
	private String page;
	
	public GuiltyWorker(GuiltyManager manager, String manga, String page)
	{
		this.manager = manager;
		this.manga = manga;
		this.page = page;
	}
	
	public void run() 
	{
		try {
			System.out.println("Processando pagina: "+page);
			manager.processMangaPage(manga, page);
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 
}
