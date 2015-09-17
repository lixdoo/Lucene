package test;

import cn.lucene.participle.NlpirLibrary.CLibraryNlpir;

public class Test {
	
	public static void main(String[] args) {
		if(Nlpir_init()) {
			System.out.println(1);
			System.out.println(NLPIR_ParagraphProcess("我就是我",1));
		}
	}
	public static boolean Nlpir_init(){
		//logger.debug("初始化开始");
		String argu="";
		int init_flag = CLibraryNlpir.Instance.NLPIR_Init(argu, 1, "0");
		if (0 == init_flag) {
			//logger.debug("初始化失败！");
			return false;
		}
		//logger.debug("初始化成功。。。");
		return true;
	}
	
	public static String NLPIR_ParagraphProcess(String sSrc, int bPOSTagged){
		String ParticipleResult=CLibraryNlpir.Instance.NLPIR_ParagraphProcess(sSrc, bPOSTagged);
	//	logger.debug("分词结束");
		//System.out.println(ParticipleResult);
		return ParticipleResult;
	}
}
