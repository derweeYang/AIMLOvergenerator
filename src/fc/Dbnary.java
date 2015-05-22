package fc;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.getalp.lexsema.language.Language;
import org.getalp.lexsema.ontolex.LexicalEntry;
import org.getalp.lexsema.ontolex.LexicalResourceEntity;
import org.getalp.lexsema.ontolex.dbnary.DBNary;
import org.getalp.lexsema.ontolex.dbnary.Vocable;
import org.getalp.lexsema.ontolex.dbnary.exceptions.NoSuchVocableException;
import org.getalp.lexsema.ontolex.dbnary.relations.DBNaryRelationType;
import org.getalp.lexsema.ontolex.factories.resource.LexicalResourceFactory;
import org.getalp.lexsema.ontolex.graph.OWLTBoxModel;
import org.getalp.lexsema.ontolex.graph.OntologyModel;
import org.getalp.lexsema.ontolex.graph.storage.JenaRemoteSPARQLStore;
import org.getalp.lexsema.ontolex.graph.storage.StoreHandler;
import org.getalp.lexsema.ontolex.graph.store.Store;
import org.slf4j.Logger;

/**
 * 
 * @author andon
 *
 */
public class Dbnary {
	
	// Choose the language first
	private static Language language = Language.FRENCH;
	
	public static final Logger logger = org.slf4j.LoggerFactory.getLogger(Dbnary.class);
	
    //public static final String DB_PATH = "data" + File.separatorChar + "dbnary" + File.separatorChar + "dbnarutdb_fr_en";
    public static final String ONTOLOGY_PROPERTIES = "data" + File.separatorChar + "ontology.properties";
	
    
    public String getPOS(String arg) throws IOException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException, ClassNotFoundException, NoSuchVocableException {
		
        Store vts = new JenaRemoteSPARQLStore("http://kaiko.getalp.org/sparql");      
        StoreHandler.registerStoreInstance(vts);

        OntologyModel model = new OWLTBoxModel(ONTOLOGY_PROPERTIES);
        
        // Creating DBNary wrapper
        DBNary dbnary = (DBNary) LexicalResourceFactory.getLexicalResource(DBNary.class, model,language );
        
        Vocable v = dbnary.getVocable(arg);

        	
    	List<LexicalEntry> entries = dbnary.getLexicalEntries(v);
    	
    	String pos = "xxxx";
    	for(LexicalEntry le: entries){
    		/*
    		List<LexicalResourceEntity> related = dbnary.getRelatedEntities(le, DBNaryRelationType.synonym);
    		
    		for(LexicalResourceEntity lent: related){
    			logger.info("\t\tSYN: "+lent.toString());
    		}*/
    		String pos_prefix = "http://www.lexinfo.net/ontology/2.0/lexinfo#";
    		
    		//System.out.println(le);
    		if (le.getPartOfSpeech().length() > pos_prefix.length()){
    			pos = le.getPartOfSpeech().substring(pos_prefix.length());
    			if (pos.equals("verb")){
    				return "verb";
    			}
    		}
    		//System.out.println(pos);
    	}

    	return pos;
    }
    
    public ArrayList<String> getSyn(String arg) throws IOException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException, ClassNotFoundException, NoSuchVocableException {
				
		ArrayList<String> res = new ArrayList<>();
        //Store vts = new JenaTDBStore(DB_PATH);
        
        Store vts = new JenaRemoteSPARQLStore("http://kaiko.getalp.org/sparql");      
        StoreHandler.registerStoreInstance(vts);
        
        //Uncomment to see the queries being generated
        //StoreHandler.DEBUG_ON = true;
        
        
        OntologyModel model = new OWLTBoxModel(ONTOLOGY_PROPERTIES);
        // Creating DBNary wrapper
        DBNary dbnary = (DBNary) LexicalResourceFactory.getLexicalResource(DBNary.class, model,language );
        
        //To get all the entries as one big list
        //List<Vocable> vocables = dbnary.getVocables();
        
        
        //Example code to fetch just one vocable
        //logger.info("Getting cat vocable...");
        Vocable v = dbnary.getVocable(arg);
        
        //This loop gets all the LexicalEntries for each Vocable and then all the senses of each entry and displays them hierarchically
        //for(Vocable v: vocables){
        	//logger.info(v.toString());
        	
        	List<LexicalEntry> entries = dbnary.getLexicalEntries(v);
        	for(LexicalEntry le: entries){
        		List<LexicalResourceEntity> related = dbnary.getRelatedEntities(le, DBNaryRelationType.synonym);
        		
        		//logger.info("\t"+le.toString());
        		for(LexicalResourceEntity lent: related){
        			logger.info("\t\tSYN: "+lent.toString());
        			res.add(lent.toString().substring(20,lent.toString().length()-1));
        		}
        	}
        //}
        return res;
	}
}
