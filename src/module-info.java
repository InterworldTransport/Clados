/**
 * The Clados library is packaged as one module. Production versions of it
 * likely won't have the test packages bundled into it.
 * 
 * @author Dr Alfred W Differ
 * @version 1.0
 */
module org.interworldtransport.clados {
	exports org.interworldtransport.cladosF;
	exports org.interworldtransport.cladosFExceptions;
	exports org.interworldtransport.cladosFTest;
	exports org.interworldtransport.cladosG;
	exports org.interworldtransport.cladosGExceptions;
	exports org.interworldtransport.cladosGTest;

	requires java.base;
	// this isn't quite done right.
	// just be aware that the test classes rely upon JUnit and teach
	// your tools to reference things correctly for the imports to work.
	requires org.junit.jupiter.api;
}