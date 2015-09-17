package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import cn.lucnen.utils.FileUtils;

public class Test2 {
	 public static void main(String[] args) throws IOException {
		File file = new File("C:\\Word\\SVNK.doc");
		System.out.println(FileUtils.convertWord(new FileInputStream(file), null));
	 }
}
/*public static void main(String[] args) throws IOException {
	 int[] a={3, 9, -1, 5};  
	 int[] b={1,5,3,9,7,11};
	 Test2.quickSort(a, 0, a.length-1);
	 Test2.quickSort(b, 0, b.length-1);
		 for(int i=0; i < a.length; i++){
			 for(int j=0; j < b.length; j++) {
				 if(a[i] + b[j] > 10) {
					 break;
				 } else if(a[i] + b[j] == 10) {
					 System.out.println(a[i]);
					 System.out.println(b[j]);
				 }
			 }
		 }
}

   public static int getMiddle(int[] list, int low, int high) {  
       int tmp = list[low];    
       while (low < high) {  
           while (low < high && list[high] > tmp) {  
               high--;  
           }  
           list[low] = list[high];   
           while (low < high && list[low] < tmp) {  
               low++;  
           }  
           list[high] = list[low];   
       }  
       list[low] = tmp;             
       return low;                   
   }  
   
   public static void quickSort(int[] list, int low, int high) {  
       if (low < high) {  
           int middle = getMiddle(list, low, high);  
           quickSort(list, low, middle - 1);        
           quickSort(list, middle + 1, high);      
       }  
   }  */