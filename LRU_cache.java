import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;
import java.text.DecimalFormat;

public class LRU_cache{

	public static void main(String[] args)throws IOException {

		//Variable Declaration
		long cache_hit=0;
		long cache_miss=0;
		float hit_ratio=0;
		long mem_accesses=0;
		String line = null;

		//Linked List as a data structure for cache initialization
		LinkedList<Integer> list = new LinkedList<Integer>();

		//User input for cache size
		//Scanner reader = new Scanner(System.in);  	
		//System.out.println("Enter the cache size: ");
		//int n = reader.nextInt();
		
		//User input for the path of the trace file to be read
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

//----------------------------------------------------Running the LRU Algorithm---------------------------------------------------------------------------------
				
				for(int i = r;i<r+s;i++){					//Reading the trace file line by line
					if(list.contains(i)){					//calculating the cache hits
						cache_hit=cache_hit+1;
						int loc = list.indexOf(i);
						list.remove(loc);
						list.addFirst(i);
					}
					else{									//calculating the cache misses
						if(list.size()>= n){
							list.removeLast();
							list.addFirst(i);
						}
						else{
							list.addFirst(i);
						}
						cache_miss=cache_miss+1;

					}
				}
			}				
			//Closing files
            br.close();
			//file.close();
			//reader.close();
			
            
			//Calculating the hit ratio
			mem_accesses = cache_hit + cache_miss;
			hit_ratio =(((float)cache_hit/(float)mem_accesses) * 100);
            DecimalFormat h = new DecimalFormat("#.##");

			//Displaying all the parameters
            System.out.println("Least Recently Used(LRU)");
			System.out.println("The size of the cache is:"+ list.size());
			System.out.println("The cache hits are:"+ cache_hit);
			System.out.println("The cache misses are:"+ cache_miss);
			System.out.println("The hit ratio of the tracefile is:"+hit_ratio+ "%");
			System.out.println("The hit ratio of the tracefile after rounding off:"+h.format(hit_ratio)+ "%");

		}
		catch(Exception e){
			System.out.println("The file you requested at the following path could not be found.");
			System.out.println("Please enter a valid trace file !");
			e.printStackTrace();
			
		}
	}

}



