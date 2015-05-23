package misis.chunkers;

import org.junit.Test;

import ru.kfu.itis.cll.uima.eval.EvaluationLauncher;


	public class GSBasedEvalLauncherTest {

		@Test
		public void testLauncherUsingPropertiesFile() throws Exception {
			EvaluationLauncher.main(new String[] { "test/resources/eval-launch.properties" });
		}

	}

