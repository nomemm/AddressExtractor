package misis.chunkers.address;

import java.util.Collections;
import java.util.List;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.cleartk.ml.Feature;
import org.cleartk.ml.feature.extractor.CleartkExtractorException;
import org.cleartk.ml.feature.extractor.FeatureExtractor1;

public class DictionaryFunctionExtractor implements FeatureExtractor1<Annotation> {

	@Override
	public List<Feature> extract(JCas view, Annotation focusAnnotation)
			throws CleartkExtractorException {
		// TODO Auto-generated method stub
		
	if(	
			focusAnnotation.getCoveredText().equalsIgnoreCase("дом") ||
			focusAnnotation.getCoveredText().equalsIgnoreCase("улица") ||
			focusAnnotation.getCoveredText().equalsIgnoreCase("д") ||
			focusAnnotation.getCoveredText().equalsIgnoreCase("кв") ||
			focusAnnotation.getCoveredText().equalsIgnoreCase("ул") ||
			focusAnnotation.getCoveredText().equalsIgnoreCase("пр") ||
			focusAnnotation.getCoveredText().equalsIgnoreCase("кор") ||
			focusAnnotation.getCoveredText().equalsIgnoreCase("бул") ||
			
			focusAnnotation.getCoveredText().startsWith("улиц") ||
			focusAnnotation.getCoveredText().contains("д.") ||
			focusAnnotation.getCoveredText().startsWith("дом") ||		
			
			focusAnnotation.getCoveredText().contains("ул.") ||			
			focusAnnotation.getCoveredText().contains("корп.") ||
			focusAnnotation.getCoveredText().contains("кв.") ||
			
			
			focusAnnotation.getCoveredText().contains("проезд") ||						
			focusAnnotation.getCoveredText().contains("шоссе") ||
			focusAnnotation.getCoveredText().contains("проспект") ||									
			focusAnnotation.getCoveredText().contains("просп")
			)
	{
		 // create a single feature from the text
    Feature feature = new Feature("AddrIndic");
    return Collections.singletonList(feature);
	}
	else
	{
		Feature feature = new Feature("NotAddrIndic");
	    return Collections.singletonList(feature);
	}
    

    
		
	}

}
