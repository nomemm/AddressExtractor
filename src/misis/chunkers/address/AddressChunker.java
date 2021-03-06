package misis.chunkers.address;

import java.util.ArrayList;
import java.util.List;

import misis.chunkers.address.types.AddressMention;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.cleartk.ml.CleartkSequenceAnnotator;
import org.cleartk.ml.Feature;
import org.cleartk.ml.Instances;
import org.cleartk.ml.chunking.BioChunking;
import org.cleartk.ml.feature.extractor.CleartkExtractor;
import org.cleartk.ml.feature.extractor.CleartkExtractor.Context;
import org.cleartk.ml.feature.extractor.CombinedExtractor1;
import org.cleartk.ml.feature.extractor.CoveredTextExtractor;
import org.cleartk.ml.feature.extractor.FeatureExtractor1;
import org.cleartk.ml.feature.extractor.TypePathExtractor;
import org.cleartk.ml.feature.function.CapitalTypeFeatureFunction;
import org.cleartk.ml.feature.function.CharacterCategoryPatternFunction;
import org.cleartk.ml.feature.function.CharacterCategoryPatternFunction.PatternType;
import org.cleartk.ml.feature.function.CharacterNgramFeatureFunction;
import org.cleartk.ml.feature.function.CharacterNgramFeatureFunction.Orientation;
import org.cleartk.ml.feature.function.FeatureFunctionExtractor;
import org.cleartk.ml.feature.function.NumericTypeFeatureFunction;
import org.opencorpora.cas.Word;
import org.opencorpora.cas.Wordform;

import ru.kfu.cll.uima.segmentation.fstype.Sentence;
import ru.kfu.cll.uima.tokenizer.fstype.NUM;
import ru.kfu.cll.uima.tokenizer.fstype.Token;

public class AddressChunker extends CleartkSequenceAnnotator<String> {

	private CombinedExtractor1 extractor;
	private CleartkExtractor contextExtractor;
	private FeatureFunctionExtractor funcExtractor;
	private BioChunking<Token, AddressMention> chunking;
	private CleartkExtractor contextExtractor2;
	private FeatureFunctionExtractor dictExtractor;
	private CleartkExtractor contextExtractor3;
	private CleartkExtractor contextExtractor4;


	@Override
	public void process(JCas jCas) throws AnalysisEngineProcessException {
		
//		Type st = jCas.getTypeSystem().getType("ru.kfu.cll.uima.tokenizer.fstype.Token");
//		AnnotationIndex ai = jCas.getAnnotationIndex();
//		System.out.println(ai.iterator().next());
		for (Sentence sentence : JCasUtil.select(jCas, Sentence.class)) {
			
			// extract features for each token in the sentence
			List<Token> tokens = JCasUtil.selectCovered(jCas, Token.class, sentence);
			List<List<Feature>> featureLists = new ArrayList<List<Feature>>();
			for (Token token : tokens) {
				
				List<Feature> features = new ArrayList<Feature>();
		//		features.addAll(this.extractor.extract(jCas, token));
				features.addAll(this.contextExtractor.extract(jCas, token));
	//			features.addAll(this.funcExtractor.extract(jCas, token));
				features.addAll(this.contextExtractor2.extract(jCas, token));
				features.addAll(this.contextExtractor3.extract(jCas, token));
				featureLists.add(features);
			}
		
		
		// during training, convert NamedEntityMentions in the CAS into expected classifier outcomes
	      if (this.isTraining()) {

	        // extract the gold (human annotated) NamedEntityMention annotations
	        List<AddressMention> namedEntityMentions = JCasUtil.selectCovered(
	            jCas,
	            AddressMention.class,
	            sentence);

	        // convert the NamedEntityMention annotations into token-level BIO outcome labels
	        List<String> outcomes = this.chunking.createOutcomes(jCas, tokens, namedEntityMentions);

	        // write the features and outcomes as training instances
	        this.dataWriter.write(Instances.toInstances(outcomes, featureLists));
	      }

	      // during classification, convert classifier outcomes into NamedEntityMentions in the CAS
	      else {

	        // get the predicted BIO outcome labels from the classifier
	        List<String> outcomes = this.classifier.classify(featureLists);

	        // create the NamedEntityMention annotations in the CAS
	        this.chunking.createChunks(jCas, tokens, outcomes);	        
	      }
		}
	}

	@Override
	public void initialize(UimaContext context)
			throws ResourceInitializationException {
		super.initialize(context);
		
		this.chunking = new BioChunking<Token, AddressMention>(
		        Token.class,
		        AddressMention.class);
		
		// the token feature extractor: text, char pattern (uppercase, digits,
		// etc.), and part-of-speech
		this.extractor = new CombinedExtractor1(
				new TypePathExtractor(Wordform.class, "lemma"),
				new TypePathExtractor(Wordform.class, "pos")
		);

		// the context feature extractor: the features above for the 3 preceding
		// and 3 following tokens
		this.contextExtractor = new CleartkExtractor(Token.class,
				this.extractor, 
				new CleartkExtractor.Preceding(1),
				new CleartkExtractor.Focus(),
				new CleartkExtractor.Following(1)
				);
		
		this.funcExtractor = new FeatureFunctionExtractor(
				new CoveredTextExtractor(), 				
				new CapitalTypeFeatureFunction(),
				new CharacterNgramFeatureFunction(CharacterNgramFeatureFunction.Orientation.RIGHT_TO_LEFT, 0, 4),
				new CharacterCategoryPatternFunction(CharacterCategoryPatternFunction.PatternType.REPEATS_MERGED),
				new NumericTypeFeatureFunction()
				);		
		
		this.contextExtractor2 = new CleartkExtractor(Token.class,
				this.funcExtractor, 
				new CleartkExtractor.Preceding(2),
				new CleartkExtractor.Focus(),
				new CleartkExtractor.Following(2));
				
		FeatureExtractor1 dictEx = new DictionaryFunctionExtractor();

		this.contextExtractor3 = new CleartkExtractor(Token.class,
				dictEx, 
				new CleartkExtractor.Preceding(2),
				new CleartkExtractor.Focus(),
		new CleartkExtractor.Following(2)
				);
		
	
		
	}

}
