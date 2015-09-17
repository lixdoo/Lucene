package test;

import java.io.IOException;

public class Search {
	 public static void main(String[] args) throws IOException {
		 int[] a={3, 9, -1, 5};  
		 int[] b={1,5,3,9,7,11};
		 Search.quickSort(a, 0, a.length-1);
		 Search.quickSort(b, 0, b.length-1);
		 for(int i=0; i < a.length; i++) {
			 int answer = Search.binarySearch(b, 10-a[i]);
			 if(answer != -1) {
				 System.out.println(a[i]);
				 System.out.println(b[answer]);
			 }
			 
		 }
	 }
	 
	 public static void quickSort(int[] list, int low, int high) {  
         if (low < high) {  
             int middle = getMiddle(list, low, high);  
             quickSort(list, low, middle - 1);        
             quickSort(list, middle + 1, high);      
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
	    
	    
	    public static int binarySearch(int[] array, int target) {
	    	 int start = 0;
	         int end = array.length - 1;
	         while (start <= end) {
	             int middle = (start + end) / 2;
	             if (target < array[middle]) {
	                 end = middle - 1;
	             } else if (target > array[middle]) {
	                 start = middle + 1;
	             } else {
	                 return middle;
	             }
	         }
	         return -1;
	    }
}
