package misis.chunkers.address;

import java.io.File;
import java.io.IOException;

import org.apache.uima.ResourceSpecifierFactory;
import org.apache.uima.UIMAException;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.fit.factory.AggregateBuilder;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.factory.TypeSystemDescriptionFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.cleartk.ml.CleartkSequenceAnnotator;
import org.cleartk.ml.jar.DefaultSequenceDataWriterFactory;
import org.cleartk.ml.jar.DirectoryDataWriterFactory;
import org.cleartk.ml.jar.Train;
import org.cleartk.ml.mallet.MalletCrfStringOutcomeDataWriter;
import org.cleartk.util.ae.UriToDocumentTextAnnotator;
import org.cleartk.util.cr.UriCollectionReader;
import org.cleartk.util.cr.XReader;

import ru.kfu.itis.cll.uima.cpe.XmiCollectionReader;



public class Trainer {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		// a reader that loads the URIs of the training files
		CollectionReaderDescription reader;

		TypeSystemDescription tsDesc = TypeSystemDescriptionFactory
				.createTypeSystemDescription(
						"MisisAddressTypeSystem");
						
		reader = XmiCollectionReader.createDescription(
				new File("/home/vladimir/nlp/MIsisAddress/temp-uima-output/xmi_output"), tsDesc);
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
						 "/home/vladimir/nlp/MIsisAddress/desc/MisisAddressTypeSystem.xml");
		 		  		
		aggregate.add(AnalysisEngineFactory.createEngineDescription(
				AddressChunker.class, typeSystem,
				CleartkSequenceAnnotator.PARAM_IS_TRAINING, true,
				DirectoryDataWriterFactory.PARAM_OUTPUT_DIRECTORY, "/home/vladimir/nlp/MIsisAddress/resources/model",
				DefaultSequenceDataWriterFactory.PARAM_DATA_WRITER_CLASS_NAME,
				MalletCrfStringOutcomeDataWriter.class));

		// run the pipeline over the training corpus
		SimplePipeline.runPipeline(reader,
				aggregate.createAggregateDescription());

		// train a Mallet CRF model on the training data
		Train.main("/home/vladimir/nlp/MIsisAddress/resources/model");

	}

}
