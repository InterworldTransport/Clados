/**
 * The Clados library is packaged as one module. Production versions of it
 * likely won't have the test packages bundled into it.
 * 
 * @author Dr Alfred W Differ
 * @version 2.0
 */

module org.interworldtransport.clados {
	exports org.interworldtransport.cladosF;
	exports org.interworldtransport.cladosFExceptions;
	exports org.interworldtransport.cladosFTest;
	exports org.interworldtransport.cladosG;
	exports org.interworldtransport.cladosGExceptions;
	exports org.interworldtransport.cladosGTest;

	requires java.base;
	requires org.junit.jupiter.api;
}