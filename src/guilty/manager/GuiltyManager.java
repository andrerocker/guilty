package guilty.manager;


import guilty.manager.http.GuiltyHttp;
import guilty.manager.thread.GuiltyWorker;
import guilty.manager.util.GuiltyUtil;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Gerenciador de downloads, obtem e enfileira paginas do
 * manga solicitado para o download. 
 * 
 * @author andresouza aka andrerocker
 */
public class GuiltyManager 
{
	private GuiltyHttp http = new GuiltyHttp();
	private GuiltyUtil util = new GuiltyUtil();
	
	/**
	 * Processa individualmente uma das paginas do manga a ser feito 
	 * o download, esta implementado de forma a salvar a pagina unicamente
	 * do disco rigido e em um diretorio especifico, futuramente isso
	 * sera incrementado.
	 * @param manga
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public File processMangaPage(String manga, String page) throws Exception
	{
		return util.store(http.obtemPaginaManga(manga, page), new File(page));
	}
	
	
	/**
	 * Realiza efetivamente o inicio do download do manga solicitado,
	 * trabalha internamente com a api concurrent do java para enfileirar
	 * e executar o download das paginas paralelamente.
	 * @param manga
	 * @throws Exception
	 */
	public void downloadManga(String manga) throws Exception
	{
		ExecutorService executor = Executors.newFixedThreadPool(10);
		
		for(String page: http.todasPaginasManga(manga))
			executor.execute(new GuiltyWorker(this, manga, page));
		
		executor.shutdown();
	}
}
