package guilty;

import guilty.manager.GuiltyManager;


/**
 * Downloader de mangas do centraldemangas.com.br
 * 
 * @author andresouza aka andrerocker
 */
public class Guilty 
{
	
	/**
	 * Ponto inicial da aplicacao, futuramente fara uso dos parametros
	 * passados a inicializacao.
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception
	{
		GuiltyManager manager = new GuiltyManager();
		manager.downloadManga("death_note");
	}
}
