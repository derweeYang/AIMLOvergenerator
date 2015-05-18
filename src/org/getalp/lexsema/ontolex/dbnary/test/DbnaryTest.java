package org.getalp.lexsema.ontolex.dbnary.test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.getalp.lexsema.language.Language;
import org.getalp.lexsema.ontolex.LexicalEntry;
import org.getalp.lexsema.ontolex.LexicalResourceEntity;
import org.getalp.lexsema.ontolex.LexicalSense;
import org.getalp.lexsema.ontolex.dbnary.DBNary;
import org.getalp.lexsema.ontolex.dbnary.Vocable;
import org.getalp.lexsema.ontolex.dbnary.exceptions.NoSuchVocableException;
import org.getalp.lexsema.ontolex.dbnary.relations.DBNaryRelationType;
import org.getalp.lexsema.ontolex.factories.resource.LexicalResourceFactory;
import org.getalp.lexsema.ontolex.graph.OWLTBoxModel;
import org.getalp.lexsema.ontolex.graph.OntologyModel;
import org.getalp.lexsema.ontolex.graph.storage.JenaRemoteSPARQLStore;
import org.getalp.lexsema.ontolex.graph.storage.JenaTDBStore;
import org.getalp.lexsema.ontolex.graph.storage.StoreHandler;
import org.getalp.lexsema.ontolex.graph.store.Store;
import org.slf4j.Logger;

/**
 * 
 * @author Andon
 *
 */
public class DbnaryTest {
	
	public static final Logger logger = org.slf4j.LoggerFactory.getLogger(DbnaryTest.class);
	
    //public static final String DB_PATH = "data" + File.separatorChar + "dbnary" + File.separatorChar + "dbnarutdb_fr_en";
    public static final String ONTOLOGY_PROPERTIES = "data" + File.separatorChar + "ontology.properties";
	public static void main(String[] args) throws IOException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException, ClassNotFoundException, NoSuchVocableException {
		
        //Store vts = new JenaTDBStore(DB_PATH);
        
        Store vts = new JenaRemoteSPARQLStore("http://kaiko.getalp.org/sparql");      
        StoreHandler.registerStoreInstance(vts);
        
        //Uncomment to see the queries being generated
        //StoreHandler.DEBUG_ON = true;
        
        
        OntologyModel model = new OWLTBoxModel(ONTOLOGY_PROPERTIES);
        // Creating DBNary wrapper
        DBNary dbnary = (DBNary) LexicalResourceFactory.getLexicalResource(DBNary.class, model, Language.ENGLISH);
        
        //To get all the entries as one big list
        //List<Vocable> vocables = dbnary.getVocables();
        
        
        //Example code to fetch just one vocable
        //logger.info("Getting cat vocable...");
        Vocable v = dbnary.getVocable("weather");
        
        //This loop gets all the LexicalEntries for each Vocable and then all the senses of each entry and displays them hierarchically
        //for(Vocable v: vocables){
        	logger.info(v.toString());
        	
        	List<LexicalEntry> entries = dbnary.getLexicalEntries(v);
        	for(LexicalEntry le: entries){
        		List<LexicalResourceEntity> related = dbnary.getRelatedEntities(le, DBNaryRelationType.synonym);
        		
        		logger.info("\t"+le.toString());
        		for(LexicalResourceEntity lent: related){
        			//logger.info("\t\tSYN: "+lent.toString());
        			System.out.println(lent);
        		}
        	}
        //}
	}
}
