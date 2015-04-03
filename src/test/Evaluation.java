package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fc.Generator;

public class Evaluation {

	public static Generator g;
	public static String inFile = "aimlfiles.txt";
	
	/* fetch all files in folder & all rules in file */
	
	public static void main(String[] args) {
		double sum = 0.0;
		int count = 0;
		int fail_count = 0;
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(inFile));
			String f, line;

			// Read file line by line
			while ((f = br.readLine()) != null) {
				// f is an aiml file
				
				//			System.out.println(f);
				
				// read it
				BufferedReader aimlfile = new BufferedReader(new FileReader(f));
				String p_findTemplate = "><srai>([A-Z]|_)+( ?<)";
				Pattern r_findTemplate = Pattern.compile(p_findTemplate);
				
				HashSet<String> srai = new HashSet<String>();
				
				while ((line = aimlfile.readLine()) != null) {
					// trying to find srais in each line
					Matcher m_findTemplate = r_findTemplate.matcher(line);
					
					// we found a srai
					if (m_findTemplate.find()) {
						// TODO manage multiple srai in a template
						String found = m_findTemplate.group(0).substring(6).replace(" ","");
						found = found.substring(1,found.length()-1);
						if (!found.equals("ENDASR")){
							srai.add(found);
						}
					}
				}
				aimlfile.close();
				
				//			System.out.println(srai);
				
				// print file
				System.out.println("\n"+f);

				for (String string : srai) {
				
					g = new Generator(string,f);
					
					double imp = g.getImprovement();
					
					if (imp != 0){
						//System.out.println(g.toCSV());
						System.out.println(g+"\t\t["+imp+"%]");
						sum += imp;
						count++;
					} else {
						System.out.println(g+"\t\tFAILED!!!");
						fail_count++;
					}
				}

			}
			
			System.out.println("Done");
			br.close();
			
		} catch (IOException e){
			
		}
		
		System.out.println("- - - - - - - - - - - - - - - - - -");
		System.out.println("Average amelioration: "+(sum/(double)count)+"%");
		System.out.println("Failed: "+(((double)fail_count/((double)count+(double)fail_count))*100.0)+"%");
		/*
		g = new Generator("METEO_TOMORROW",inFile);
		System.out.println(g);
		g = new Generator("METEO_TOMORROW_NIGHT",inFile);
		System.out.println(g);
		*/
	}

}
