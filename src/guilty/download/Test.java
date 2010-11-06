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

public class Test 
{
	private HttpClient http = new HttpClient();
	
	public static void main(String[] args) throws Exception
	{
		Test teste = new Test();
		teste.downlaod();
		//teste.requestMangaPage("http://74.222.1.191/cdm/naruto/naruto001-01.jpg");
		System.out.println("OK");
	}

	private void downlaod() throws Exception
	{
		String manga = "naruto";
		String url = "http://74.222.1.191/cdm/naruto/%s";
		
		for(String page: obtemListaPaginas(manga))
		{
			System.out.println("Processando pagina: "+page);
			requestMangaPage(page, String.format(url, page));
		}
	}
	
	private File request(String mangaName) throws Exception
	{
		GetMethod get = new GetMethod(String.format("http://74.222.1.191/cdm/%s/", mangaName));
		http.executeMethod(get);
		
		File temp = File.createTempFile("guilty", "txt");
		temp.deleteOnExit();
		
		store(get.getResponseBodyAsStream(), temp);
		
		return temp;
	}
	
	private void requestMangaPage(String page, String url)
	{
		try 
		{
			GetMethod mangaPage = new GetMethod(url);
			mangaPage.addRequestHeader("Referer", String.format("http://centraldemangas.com.br/online/Naruto/%s", extractEdicao(page))); //FIX: O Pulo do Gato :P 
			http.executeMethod(mangaPage);
			
			store(mangaPage.getResponseBodyAsStream(), new File(page));
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
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
