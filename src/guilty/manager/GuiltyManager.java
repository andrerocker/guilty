package guilty.manager;


import guilty.manager.http.GuiltyHttp;
import guilty.manager.thread.GuiltyWorker;
import guilty.manager.util.GuiltyUtil;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GuiltyManager 
{
	private GuiltyHttp http = new GuiltyHttp();
	private GuiltyUtil util = new GuiltyUtil();
	
	public File processMangaPage(String manga, String page) throws Exception
	{
		return util.store(http.obtemPaginaManga(manga, page), new File(page));
	}
	
	public void downloadManga(String manga) throws Exception
	{
		ExecutorService executor = Executors.newFixedThreadPool(10);
		
		for(String page: http.todasPaginasManga(manga))
			executor.execute(new GuiltyWorker(this, manga, page));
		
		executor.shutdown();
	}
}
