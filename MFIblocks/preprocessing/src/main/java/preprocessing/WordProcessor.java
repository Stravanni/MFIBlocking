package preprocessing;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.ngram.NGramTokenFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;


public class WordProcessor {
	
	StandardAnalyzer analyzer;
	private final static int MIN_NGRAM_SIZE =3;
	private final static int MAX_NGRAM_SIZE =3;
	
	private int min_ngram_size = MIN_NGRAM_SIZE;
	private int max_ngram_size = MAX_NGRAM_SIZE;
		
	public WordProcessor(File stopwordsFile){
		analyzer = new StandardAnalyzer(Version.LUCENE_48);
	}
	
	public WordProcessor(File stopwordsFile, int minNgramSize, int maxNgramSize){
		this(stopwordsFile);
		this.min_ngram_size = minNgramSize;
		this.max_ngram_size = maxNgramSize;
	}
	
	public List<String> processValue(String value){
		List<String> retVal = new ArrayList<String>();
		TokenStream ts_shortWords = null;
		try {
			value = value.replaceAll(replaceExpr, "");
			StringReader sr = new StringReader(value);
			StringReader sr_short = new StringReader(value);
			TokenStream ts = analyzer.tokenStream(value, sr);
			ts_shortWords = analyzer.tokenStream(value, sr_short);

            NGramTokenFilter ngtf = new NGramTokenFilter(ts, min_ngram_size, max_ngram_size);
            ts_shortWords.reset();
			while(ts_shortWords.incrementToken()){
				ts_shortWords.getAttribute(CharTermAttribute.class);
				String term = convertTokenStreamToString(ts_shortWords);
				if(term.length() < min_ngram_size){
					retVal.add(term.trim().toLowerCase());
				}
			}
			ts_shortWords.end();
			while(ngtf.incrementToken()){
				String term = convertTokenStreamToString(ngtf);
				retVal.add(term.trim().toLowerCase());
			}
		} catch (IOException e) {
			System.err.println("Failed to parse: " + value);
			e.printStackTrace();
		}
		finally {
			if (ts_shortWords != null) {
				try {
					ts_shortWords.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return retVal;
	}

	private String convertTokenStreamToString(TokenStream ts_shortWords) {
		CharTermAttribute m = ts_shortWords.getAttribute(CharTermAttribute.class);
		String term = new String(m.buffer());
		return term;
	}
	
	public final static String replaceExpr = "-|\\|/|\\/|\\.|,|\'|(|)";
	public List<String> removeStopwordsAndSpecialChars(String value){
		List<String> retVal = new ArrayList<String>();
		TokenStream ts = null;
		try {
			value = value.replaceAll(replaceExpr, "");
			StringReader sr = new StringReader(value);
			ts = analyzer.tokenStream(value, sr);
			ts.reset();
			while(ts.incrementToken()){
				String term = convertTokenStreamToString(ts);
				retVal.add(term);
			}
			ts.end();
		} catch (IOException e) {
			System.err.println("Failed to parse: " + value);
			e.printStackTrace();
		} finally {
			if (ts != null) {
				try {
					ts.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return retVal;
	}
	
	public static void main(String[] args){
		File f = new File("D:\\Batya\\EntityResolution\\tools\\cora-all-id\\stopwords.txt");
		WordProcessor wp = new WordProcessor(f);
		String value = "sony vegas 6";
		List<String> ngrams = wp.processValue(value);
		System.out.println(ngrams);
		
	}
	
	public static <T> String concatListMembers(List<T> list){
		StringBuilder sb = new StringBuilder();
		for (Object object : list) {
			sb.append(object.toString().trim()).append(" ");
		}
		return sb.toString();
	}
}
