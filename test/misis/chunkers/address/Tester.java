package misis.chunkers.address;

import java.io.File;
import java.io.IOException;

import org.apache.uima.ResourceSpecifierFactory;
import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.fit.factory.AggregateBuilder;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.factory.TypeSystemDescriptionFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.apache.uima.tools.components.XmiWriterCasConsumer;
import org.cleartk.ml.CleartkSequenceAnnotator;
import org.cleartk.ml.jar.DefaultSequenceDataWriterFactory;
import org.cleartk.ml.jar.DirectoryDataWriterFactory;
import org.cleartk.ml.jar.GenericJarClassifierFactory;
import org.cleartk.ml.jar.Train;
import org.cleartk.ml.mallet.MalletCrfStringOutcomeDataWriter;
import org.cleartk.util.ae.UriToDocumentTextAnnotator;
import org.cleartk.util.cr.UriCollectionReader;

import ru.kfu.itis.cll.uima.consumer.XmiWriter;
import ru.kfu.itis.cll.uima.cpe.XmiCollectionReader;



public class Tester {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		// a reader that loads the URIs of the training files
		CollectionReaderDescription reader;

		TypeSystemDescription tsDesc = TypeSystemDescriptionFactory
				.createTypeSystemDescription(
						"MisisAddressTypeSystem");
						
		reader = XmiCollectionReader.createDescription(
				new File("/home/vladimir/nlp/uimaext2015/split/test"), tsDesc);
//		reader = XReader.getDescription(
//				"/home/vladimir/nlp/MIsisAddress/temp-uima-output/xmi_output");
		
		
		// assemble the training pipeline
		AggregateBuilder aggregate = new AggregateBuilder();
	
		// an annotator that loads the text from the training file URIs
	//	aggregate.add();

		// an annotator that parses and loads MASC named entity annotations (and
		// tokens)
		// aggregate.add(MASCGoldAnnotator.getDescription());

		// an annotator that adds part-of-speech tags (so we can use them for
		// features)
		// aggregate.add(PosTaggerAnnotator.getDescription());

		// our NamedEntityChunker annotator, configured to write Mallet CRF
		// training data
		
		 TypeSystemDescription typeSystem;
		 typeSystem = TypeSystemDescriptionFactory. createTypeSystemDescriptionFromPath(
						 "/home/vladimir/nlp/MisisAddress/desc/MisisAddressTypeSystem.xml");
		 
		 // our NamedEntityChunker annotator, configured to classify on the new texts
		    aggregate.add(AnalysisEngineFactory.createEngineDescription(
		    		AddressChunker.class,
		        CleartkSequenceAnnotator.PARAM_IS_TRAINING,
		        false,
		        GenericJarClassifierFactory.PARAM_CLASSIFIER_JAR_PATH,
		        new File("/home/vladimir/nlp/MisisAddress/resources/model", "model.jar")));

		    AnalysisEngineDescription aeDesc = AnalysisEngineFactory.createEngineDescription(
		    		XmiWriter.class, typeSystem, 
		    		XmiWriter.PARAM_OUTPUTDIR, "/home/vladimir/nlp/MisisAddress/temp-uima-output/split/test");
		    
		    aggregate.add(aeDesc);
		    
//		    aggregate.add(AnalysisEngineFactory.createEngineDescriptionFromPath(
//		    		"file:///home/vladimir/nlp/MIsisAddress/desc/XmiWriterCasConsumerTest.xml"));   
		    // run the pipeline over the training corpus
		SimplePipeline.runPipeline(reader,
				aggregate.createAggregateDescription());

	
	}

}
