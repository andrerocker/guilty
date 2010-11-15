package guilty.manager.http;

import guilty.manager.util.GuiltyUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 * Reponsavel por toda negociacao com o site atraves de Http.
 * internamente estou utilizando o HttpClient 3
 * @author andresouza aka andrerocker
 */
public class GuiltyHttp 
{
	private static String indicePaginas = "http://74.222.1.191/cdm/%s/";
	private static String urlPagina = "http://74.222.1.191/cdm/%s/%s";
	private static String bypassUrl = "http://centraldemangas.com.br/online/%s/%s";
	
	private GuiltyUtil util = new GuiltyUtil();
	
	/**
	 * Obtem a de forma crua(html) uma listagem completa de paginas que o manga
	 * possui.
	 * 
	 * @param manga --> nome do manga desejado.
	 * @return stream de dados para a listagem de forma crua (html)
	 * @throws Exception
	 */
	public InputStream obtemIndiceManga(String manga) throws Exception
	{
		GetMethod request = new GetMethod(String.format(indicePaginas, manga));
		new HttpClient().executeMethod(request);
		
		return request.getResponseBodyAsStream(); 
	}
	
	/**
	 * Retorna a pagina solicitada de um manga expecifico.
	 * @param manga --> manga desejado
	 * @param page --> pagina
	 * @return Stream de dados para a pagina do manga(jpg)
	 * @throws Exception
	 */
	public InputStream obtemPaginaManga(String manga, String page) throws Exception
	{
		String url = String.format(urlPagina, manga, page);
		String bypass = String.format(bypassUrl, manga, util.extractEdicao(manga, page));
		
		GetMethod request = new GetMethod(url); 
		request.addRequestHeader("Referer", bypass);
		new HttpClient().executeMethod(request);
		
		return request.getResponseBodyAsStream();
	}
	
	/**
	 * Trabalha em conjunto com obtemIndiceManga, retorna uma lista
	 * formatada com as paginas do manga.
	 * @param manga --> manga desejado
	 * @return lista pre formatada com todas paginas do manga.
	 * @throws Exception
	 */
	public List<String> todasPaginasManga(String manga) throws Exception
	{
		List<String> result = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new InputStreamReader(obtemIndiceManga(manga)));
		String current = null;
		
		while((current = br.readLine())!=null)
		{
			if(current.indexOf("<li><a href=\"")>-1)
			{
				String link = util.extractPageLink(current);
				
				if(link.endsWith("jpg"))
					result.add(link);
			}
		}
		
		return result;
	}
}
