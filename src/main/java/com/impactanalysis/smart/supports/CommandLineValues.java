package com.impactanalysis.smart.supports;
import java.io.File;

import org.kohsuke.args4j.Option;

public class CommandLineValues {

 	
		@Option(name = "-h", aliases = { "-help" }, usage = "print this message")
		private boolean help = false;

		@Option(name = "-i", required = true, usage = "input from this file", metaVar = "/path/to/your.class")
		private File in = new File(".");

		@Option(name = "-o", required = true, usage = "output to this dir", metaVar = "/tmp/bin")
		private File out = new File(".");	
	
	
}
