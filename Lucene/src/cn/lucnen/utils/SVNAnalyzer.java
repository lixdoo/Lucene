package cn.lucnen.utils;

import java.io.Reader;
import java.util.Arrays;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.snowball.SnowballFilter;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.util.Version;

public class SVNAnalyzer extends Analyzer{
	
	private Version version;
	public static final CharArraySet ENGLISH_STOP_WORDS_SET;
	
	public SVNAnalyzer(Version version) {
		this.version = version;
	}

	@Override
	protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
		Tokenizer tokenizer = new WhitespaceTokenizer(version, reader);
		TokenFilter lowerCaseFilter = new LowerCaseFilter(version, tokenizer); 
		TokenFilter stopFilter = new StopFilter(version, lowerCaseFilter, ENGLISH_STOP_WORDS_SET );
		TokenFilter snowballFilter = new SnowballFilter(stopFilter, "English");
		return new TokenStreamComponents(tokenizer, snowballFilter);
	}  

	static {
		java.util.List<String> stopWords = Arrays.asList(new String[] {
            "a", "an", "and", "are", "as", "at", "be", "but", "by", "for", 
            "if", "in", "into", "is", "it", "no", "not", "of", "on", "or", 
            "such", "that", "the", "their", "then", "there", "these", "they", 
            "this", "to", "was", "will", "with", "b", "c", "d", "e", "f", "g", 
            "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u",
            "v", "w", "x", "y", "z", ",", ".", "?", "<", ">", "<>",":", ";", "'", 
            "\"\"", "{", "}","{}", "[", "]", "[]", "|", "\\", "/","`", "~", "!", 
            "@", "#", "$", "$", "%", "%", "^","&", "*", "(", ")", "()", "-", "_", 
            "=", "+", "&&","||", "''", "\"", "，", "。", "、", "《", "》", "《》",
            "？", "：", "；", "’", "‘", "“", "”", "’‘", "“”", "【", "】", "【】", 
            "{", "}", "{}", "、", "|", "·", "~", "！", "@", "#", "￥", "%", "……", 
            "&", "*", "（", "）", "（）","-", "——", "+", "=", "的", "吗", "了", "我",
            "我们", "你", "你们", "他", "他们", "呀", "唉", "吧", "地", "嗯", "么", "是", "啊",
            "阿"
		});
        CharArraySet stopSet = new CharArraySet(Version.LUCENE_47, stopWords, true);
        ENGLISH_STOP_WORDS_SET = CharArraySet.unmodifiableSet(stopSet);
    }
	
	/* public static void main(String[] args) throws IOException {  
		   final String text = "chenguanlin@zucc.edu.cn asking";
		   Version matchVersion = Version.LUCENE_47; 
		   SVNAnalyzer analyzer = new SVNAnalyzer(matchVersion);
		   TokenStream stream = analyzer.tokenStream("field", new StringReader(text));
		   CharTermAttribute termAtt = stream.addAttribute(CharTermAttribute.class);
		   try {
			   stream.reset();
			   while (stream.incrementToken()) {
		        System.out.println(termAtt.toString());
		        }
		      stream.end();
		    } finally {
		      stream.close();
		    }
		  }*/
}
