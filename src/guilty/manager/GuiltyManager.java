package guilty.manager;


import guilty.manager.http.GuiltyHttp;
import guilty.manager.util.GuiltyUtil;

import java.io.File;

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
		for(String page: http.todasPaginasManga(manga))
		{
			System.out.println("Processando pagina: "+page);
			processMangaPage(manga, page);
		}	
	}
}
