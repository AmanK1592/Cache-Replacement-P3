import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.Scanner;
import java.text.DecimalFormat;

public class ARC_cache{
	//Global variable declaration
	static int p = 0;
	static int del1= 0;
	static int del2 =0;
	static double cache_hit = 0;
	static double cache_miss = 0;
	static double hit_ratio = 0;
	static double mem_accesses = 0;
	static String line = null;

	//Defining Linked Lists for L1 and L2 where L1=T1+B1 and L2=T2+B2
	static LinkedList<Integer> T1 = new LinkedList<Integer>();
	static LinkedList<Integer> T2 = new LinkedList<Integer>();
	static LinkedList<Integer> B1 = new LinkedList<Integer>();
	static LinkedList<Integer> B2 = new LinkedList<Integer>();


	public static void main(String[] args)throws IOException {

		//User input for cache size
		//Scanner reader = new Scanner(System.in);  	
		//System.out.println("Enter the cache size: ");
		//int n = reader.nextInt();

		//User input for the path of the trace file
		//Scanner file = new Scanner(System.in);		
		//System.out.println("Enter the full path of the tracefile to be read: ");
		//String filename = file.nextLine();

		//Command line arguments for running the file
		int n = Integer.parseInt(args[0]);
		String filename = args[1];


		try{
			BufferedReader br = new BufferedReader(new FileReader(filename));

			while ((line = br.readLine()) != null) {
				//Splitting the address field from the trace file
				int ind1 = line.indexOf(" ",0);		
				String a =line.substring(0,ind1);
				int r = Integer.parseInt(a);

				//Splitting the block length from the trace file
				int ind2 = line.indexOf(" ",ind1+1);
				String b = line.substring(ind1+1,ind2);
				int s = Integer.parseInt(b);

//-------------------------------------------------------------------------------Running the ARC Algorithm-----------------------------------------------------------------------

				for(int i = r;i<r+s;i++){						//Reading the trace file line by line
					if(T1.contains(i)){							//Implementing Case I
						cache_hit=cache_hit+1;
						int loc = T1.indexOf(i);
						T1.remove(loc);
						T2.addFirst(i);

					}else if(T2.contains(i)){
						cache_hit=cache_hit+1;
						int loc = T2.indexOf(i);
						T2.remove(loc);
						T2.addFirst(i);
					}
					else if(B1.contains(i)){					//Implementing Case II
						cache_miss=cache_miss+1;
						if(B1.size()>=B2.size()){
							del1 = 1;
						}else{
							del1 = (B2.size()/B1.size());
						}
						p=Math.min(p+del1,n);
						Replace(i,p);
						int loc = B1.indexOf(i);
						B1.remove(loc);
						T2.addFirst(i);	
					}
					else if(B2.contains(i)){					//Implementing Case III
						cache_miss=cache_miss+1;
						if(B2.size()>=B1.size()){
							del2 = 1;
						}else{
							del2 = (B1.size()/B2.size());
						}
						p=Math.max(p-del2,0);
						Replace(i,p);
						int loc = B2.indexOf(i);
						B2.remove(loc);
						T2.addFirst(i);	
					}
					else{
						cache_miss=cache_miss+1;				//Implementing Case IV
						if(T1.size()+B1.size()== n){			//Case IV(A)
							if(T1.size()<n){
								B1.removeLast();
								Replace(i,p);
							} else{
								T1.removeLast();
							}
						}
						else if(T1.size()+B1.size()<n){			//Case IV(B)
							if(T1.size()+T2.size()+B1.size()+B2.size()>=n){
								if(T1.size()+T2.size()+B1.size()+B2.size()==2*n){
									B2.removeLast();}
								Replace(i,p);	
							}
						}
						T1.addFirst(i);	
					}
				}
			}
			//Closing the files
            br.close();
			//reader.close();
			//file.close();
			

			//Calculating the hit ratio
			mem_accesses = cache_hit + cache_miss;
			hit_ratio =(((float)cache_hit/(float)mem_accesses) * 100);
			DecimalFormat df = new DecimalFormat("#.##");

			//Printing the values
			System.out.println("Adaptive Replacement Cache(ARC)");
			System.out.println("The cache hits are:"+ cache_hit);
			System.out.println("The cache misses are:"+ cache_miss);
			System.out.println("The hit ratio of the tracefile is:"+ hit_ratio + "%");
			System.out.println("The hit ratio of the tracefile after rounding off:"+df.format(hit_ratio)+ "%");

		}


		catch(Exception e){
			System.out.println("The file you requested at the following path could not be found.");
			System.out.println("Please enter a valid trace file!");
			e.printStackTrace();
		}
	}

	//Replace Method declaration and definition
	public static void Replace(int x,int p){
		if((T1.size()!=0) && ((T1.size() > p) || (B2.contains(x)&& (T1.size()== p)))){
			int a = T1.getLast();
			T1.removeLast();
			B1.addFirst(a);
		}else{
			int b = T2.getLast();
			T2.removeLast();
			B2.addFirst(b);
		}

	}

}
