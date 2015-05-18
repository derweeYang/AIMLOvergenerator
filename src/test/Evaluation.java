package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fc.Manager;

public class Evaluation {

	public static Manager man;
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
					while (m_findTemplate.find()) {
						// manage multiple srai in a template
						// done with while (above)
						String found = m_findTemplate.group(0).substring(6).replace(" ","");
						found = found.substring(1,found.length()-1);
									
						// small necessary hack
						if (!found.equals("ENDASR") && !found.equals("ENDASR_FROM_TOPIC")){
							srai.add(found);
						}
					}
				}
				aimlfile.close();
				
				//System.out.println(srai);
				
				// = = = PRINT FILE
				System.out.println("\n"+f);

				// =====================================================
				// =====================================================
				boolean thread = false;
				for (String template : srai) {
					
					if (thread){
						final String temp = template;
						final String file = f;
						new Thread(){
							public void run(){
								
								Manager m = new Manager(temp,file);
								
								// APPLY SYNONYMS WITH DBNARY?
								//man.applySyn();
								
								m.generateFile();
								
								double imp = m.getImprovement();
								
								if (imp != 0){
									System.out.println(m.toCSV());
									// = = = PRINT IMPROVEMENT
									//System.out.println(g+"\t\t["+imp+"%]");
									//sum += imp;
									//count++;
								} else {
									// = = = PRINT IF FAILED
									//System.out.println(g+"\t\tFAILED!!!");
									//fail_count++;
								}
							}
						}.start();
					} else {
						man = new Manager(template,f);
						
						// APPLY SYNONYMS WITH DBNARY?
						//man.applySyn();
						
						man.generateFile();
						
						double imp = man.getImprovement();
						
						if (imp != 0){
							System.out.println(man.toString());
							// = = = PRINT IMPROVEMENT
							//System.out.println(g+"\t\t["+imp+"%]");
							sum += imp;
							count++;
						} else {
							// = = = PRINT IF FAILED
							//System.out.println(g+"\t\tFAILED!!!");
							fail_count++;
						}
					}
					
				}
				// =====================================================
				// =====================================================


			}
			
			System.out.println("Done");
			br.close();
			
		} catch (IOException e){
			e.printStackTrace();
		}
		
		System.out.println("- - - - - - - - - - - - - - - - - - - - - -");
		System.out.println("Average amelioration: "+(sum/(double)count)+"%");
		System.out.println("Failed: "+(((double)fail_count/((double)count+(double)fail_count))*100.0)+"%");

	}

}
