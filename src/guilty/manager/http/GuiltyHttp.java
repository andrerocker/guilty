package guilty.manager.http;

import guilty.manager.util.GuiltyUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

public class GuiltyHttp 
{
	private static String indicePaginas = "http://74.222.1.191/cdm/%s/";
	private static String urlPagina = "http://74.222.1.191/cdm/%s/%s";
	private static String bypassUrl = "http://centraldemangas.com.br/online/%s/%s";
	
	private GuiltyUtil util = new GuiltyUtil();
	
	public InputStream obtemIndiceManga(String manga) throws Exception
	{
		GetMethod request = new GetMethod(String.format(indicePaginas, manga));
		new HttpClient().executeMethod(request);
		
		return request.getResponseBodyAsStream(); 
	}
	
	public InputStream obtemPaginaManga(String manga, String page) throws Exception
	{
		String url = String.format(urlPagina, manga, page);
		String bypass = String.format(bypassUrl, manga, util.extractEdicao(manga, page));
		System.out.println("Url: "+url+" bypass:"+bypass);
		
		
		GetMethod request = new GetMethod(url); 
		request.addRequestHeader("Referer", bypass);
		new HttpClient().executeMethod(request);
		
		return request.getResponseBodyAsStream();
	}
	
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
