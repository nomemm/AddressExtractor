package misis.chunkers.address;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import misis.chunkers.address.types.AddressMention;

import org.apache.commons.io.FileUtils;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.Feature;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureStructureImpl;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.examples.SourceDocumentInformation;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

import ru.kfu.cll.uima.segmentation.fstype.Sentence;
import ru.kfu.cll.uima.tokenizer.fstype.Token;

public class AddressMentionInserter extends JCasAnnotator_ImplBase {

	

	@Override
	public void process(JCas jCas) throws AnalysisEngineProcessException 
	{				
		String path = "/home/vladimir/nlp/uimaext2015/address/";
		Type t = jCas.getTypeSystem().getType("org.apache.uima.examples.SourceDocumentInformation");
		Type mt = jCas.getTypeSystem().getType("misis.chunkers.address.types.AddressMention");
		
		Feature uri = t.getFeatureByBaseName("uri");
		AnnotationIndex ai = jCas.getAnnotationIndex(t);
		
		String name = ((Annotation) ai.iterator().next()).getFeatureValueAsString(uri).toString();
//		String name = 
		File txtFile = new File(name);
		String fn = txtFile.getName();
		
		String annFileName = path + fn.substring(0, fn.indexOf(".")) + ".ann";
		// parse brat file
		try {
			List<String> mentions = FileUtils.readLines(new File(annFileName));
			// for each mention
			for(String s : mentions)
			{
				String [] x = s.split("\t");
				String [] a = x[1].split(" ");
				
				int begin = Integer.valueOf(a[1]);
				int end = Integer.valueOf(a[2]);
				
				// insert mention into cas
				AddressMention m = (AddressMention)jCas.getCas().createAnnotation(mt, begin, end);				
				
				jCas.addFsToIndexes(m);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
			

		
		
		
		/*for (Sentence sentence : JCasUtil.select(jCas, Sentence.class)) {

			
			List<Token> tokens = JCasUtil.selectCovered(jCas, Token.class,
					sentence);
			List<List<Feature>> featureLists = new ArrayList<List<Feature>>();
			for (Token token : tokens) {
				
			}
			}
*/		
		

	}

	

}
