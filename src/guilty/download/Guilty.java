package guilty.download;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

public class Guilty 
{
	private HttpClient http = new HttpClient();
	private String pageUrlTemplate = "http://74.222.1.191/cdm/%s/%s";
	private String indexPagesUrlTemplate = "http://74.222.1.191/cdm/%s/";
	private String bypassUrlTemplate = "http://centraldemangas.com.br/online/%s/%s";
	
	public static void main(String[] args) throws Exception
	{
		Guilty teste = new Guilty();
		teste.download("naruto");
	}

	private void download(String manga) throws Exception
	{
		for(String page: obtemListaPaginas(manga))
		{
			System.out.println("Processando pagina: "+page);
			requestMangaPage(manga, page, String.format(pageUrlTemplate, manga, page));
		}
	}
	
	private File request(String manga) throws Exception
	{
		GetMethod get = new GetMethod(String.format(indexPagesUrlTemplate, manga));
		http.executeMethod(get);
		
		File temp = File.createTempFile("guilty", "txt");
		temp.deleteOnExit();
		
		store(get.getResponseBodyAsStream(), temp);
		
		return temp;
	}
	
	private void requestMangaPage(String manga, String page, String url)
	{
		try 
		{
			GetMethod mangaPage = bypass(manga, page, new GetMethod(url));
			http.executeMethod(mangaPage);
			
			store(mangaPage.getResponseBodyAsStream(), new File(page));
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	private GetMethod bypass(String manga, String page, GetMethod method)
	{
		//FIX: O Pulo do Gato :P
		method.addRequestHeader("Referer", String.format(bypassUrlTemplate, manga, extractEdicao(page))); 
		return method;
	}
	
	private void store(InputStream input, File file)
	{
		try 
		{
			BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(file));
			byte[] buffer = new byte[2048];
			int len = 0;
			
			while((len = input.read(buffer))>0)
				output.write(buffer, 0, len);
			
			output.flush();
			output.close();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	private List<String> obtemListaPaginas(String manga) throws Exception
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(request(manga))));
		String current = null;
		
		List<String> result = new ArrayList<String>();
		
		while((current = br.readLine())!=null)
		{
			if(current.indexOf("<li><a href=\"")>-1)
			{
				String link = extractPageLink(current);
				
				if(link.endsWith("jpg"))
					result.add(link);
			}
		}
		
		return result;
	}
	
	private String extractPageLink(String rawLink)
	{
		String temp = rawLink.substring("<li><a href=\"".length());
		return temp.substring(0, temp.indexOf("\""));
	}
	
	private String extractEdicao(String page)
	{
		String temp = page.substring("naruto".length());
		return temp.substring(temp.lastIndexOf("-"));
	}
}
