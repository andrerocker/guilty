package guilty.manager.thread;

import guilty.manager.GuiltyManager;

/**
 * Worker para trabalhar de forma paralela com os 
 * downloads das paginas.
 * 
 * @author andresouza aka andrerocker
 */
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
	
	/**
	 * Executa donwload em nivel paralelo
	 */
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
