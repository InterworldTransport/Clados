/*
 * <h2>Copyright</h2> © 2025 Alfred Differ<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosG.GExporter<br>
 * -------------------------------------------------------------------- <br>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.<br><br>
 * 
 * Use of this code or executable objects derived from it by the Licensee 
 * states their willingness to accept the terms of the license. <br> <br>
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.<br> <br>
 * 
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosG.GExporter<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosG;

import org.interworldtransport.cladosF.ComplexD;
import org.interworldtransport.cladosF.ComplexF;
import org.interworldtransport.cladosF.ProtoN;
import org.interworldtransport.cladosF.RealD;
import org.interworldtransport.cladosF.RealF;

/**
 * This is a non-constructable class meant as a collector of all export methods for cladosG objects
 * They are collected here not because a single object doing data exports is particularly efficient, 
 * (it is likely to be a bottleneck until everything is parallelized), but because the geometry
 * objects in real physical models are likely to be numerous. The geometry does not need to take 
 * up memory knowing how to export its contents to some format for every single piece of geometry.
 * It is enough that one object does it or even that it is all handled by static methods.
 */
public class GExporter {
    /*
	 * Private constructor means this will only after get used for its class/static methods.
	 */
	private GExporter(){
		;
	}

    /**
	 * Export a Foot as a small JSON fragment. Object properties are represented as attributes.
	 * This is intended as an output format.
	 * <br><br>
	 * {<br>
	 * 	"Foot": {<br>
	 * 		"name": "NamedPoint"<br>
	 * 		}<br>
	 * }
	 * <br><br>
	 * @param pF Foot to be exported as JSON
	 * @return String formatted as JSON containing information about the input
	 */
	public final static String toJSON(Foot pF) {
		StringBuilder rB = new StringBuilder();
		rB	.append("{\"foot\": {\"name\": \"")
			.append(pF.getName())
			.append("\"}}\n");
		return rB.toString();
	}

	/**
	 * Export a Blade as a small JSON fragment. Object properties are represented as attributes.
	 * This is intended as an output format.
	 * <br><br>
	 * Example:<br>
	 * {<br>
	 * 	"Blade": {<br>
	 * 		"key": 81985529216486895, <br>
	 * 		"bitKey": "0b111111111111111", <br>
	 * 		"generators": ["E1","E2","E3","E4","E5","E6","E7","E8","E9","EA","EB","EC","ED","EE","EF"]<br>
	 * 		}<br>
	 * }
	 * <br><br>
	 * @param blade Blade to be exported as JSON
	 * @param pAsText boolean determines whether generators are exported in text or ordinal representation.
	 * @return String formatted as JSON containing information about the input
	*/
	public final static String toJSON(Blade blade, boolean pAsText) {
		StringBuilder rB = new StringBuilder();
		rB	.append("{\"blade\": {\"key\": ")
			.append(blade.key())
			.append(", \"bitKey\": \"0b");
		int pad = blade.maxGen - Integer.toBinaryString(blade.bitKey()).length();
		while (pad>0) {
			rB.append("0");
			pad--;
		}
		rB	.append(Integer.toBinaryString(blade.bitKey()))
			.append("\", \"generators\": [");

		if(pAsText)	blade	.generatorStream()
							.forEachOrdered(g -> rB.append("\""+g.toString()+"\"").append(","));
		else		blade	.generatorStream()
							.forEachOrdered(g -> rB.append(g.ord).append(","));

		if (blade.getGenerators().size() > 0)	rB.deleteCharAt(rB.length() - 1);
		rB	.append("]}}");
		return rB.toString();
	}

	/**
	 * Export a BladeDuet as a small JSON fragment. Object properties are represented as attributes.
	 * This is intended as an output format.
	 * <br><br>
	 * Example:<br>
	 * {<br>
	 *	"duet": {<br>
	 *		"sign": 1, <br>
	 *		"maxGrade": 3, <br>
	 *		"Blades": {<br>
	 *			"blade": [ 	{"key": 27, "bitKey": "0b111", "generators": ["E1","E2","E3"]}, <br>
	 *						{"key": 6, "bitKey": "0b011", "generators": ["E1","E2"]} ]<br>
	 *		}<br>
	 *	}<br>
	 * }
	 * <br><br>
	 * @param duet BladeDuet to be exported as JSON
	 * @return String formatted as JSON containing information about the input
	*/
	public final static String toJSON(BladeDuet duet) {		
		StringBuilder rB = new StringBuilder();
		rB	.append("{\"duet\": {\"sign\": ")
			.append(duet.sign)
			.append(", \"maxGrade\": ");
	
		if(duet.maxGen != null)	rB.append(duet.maxGen.ord);
		else 					rB.append(Math.max(duet.bladeLeft.maxGenerator(), duet.bladeRight.maxGenerator()));
		
		rB	.append(", \"Blades\": {\"blade\": [");
			
		StringBuffer workThis = new StringBuffer(GExporter.toJSON(duet.bladeLeft, true));
		workThis.delete(0,9);
		workThis.deleteCharAt(workThis.length()-1);
		workThis.append(",");
		rB.append(workThis);

		workThis = new StringBuffer(GExporter.toJSON(duet.bladeRight, true));
		workThis.delete(0,9);
		workThis.deleteCharAt(workThis.length()-1);
		rB.append(workThis);

		rB	.append("]}}");
		return rB.toString();
	}
	

	/**
	 * Export a Basis as a JSON fragment. Object properties are represented as attributes.
	 * This is intended as an output format.
	 * <br><br>
	 * Example:<br>
	 * {<br>
	 *	"basis": {<br>
	 *	"UUID": "6f264df4-0b45-4ac2-9679-3ff1d181a0dc", <br>
	 *	"Grades": {<br>
	 *		"count": 3,<br> 
	 *		"grade": [	{"rank": 0, "range": "0-0"}, <br>
	 *					{"rank": 1, "range": "1-2"}, <br>
	 *					{"rank": 2, "range": "3-3"}]<br>
	 *		}, <br>
	 *	"Blades": {<br>
	 *		"count": 4,<br> 
	 *		"blade": [ 	{"key": 0, "bitKey": "0b00", "generators": []}, <br>
	 *					{"key": 1, "bitKey": "0b01", "generators": ["E1"]}, <br>
	 *					{"key": 2, "bitKey": "0b10", "generators": ["E2"]}, <br>
	 *					{"key": 5, "bitKey": "0b11", "generators": ["E1","E2"]}]<br>
	 *		}<br>
	 *	}<br>
	 * }
	 * <br><br>
	 * @param basis to be exported as JSON
	 * @return String formatted as JSON containing information about the input
	 */
	public final static String toJSON(Basis basis) {
		StringBuilder rB = new StringBuilder();

		rB	.append("{\"basis\": {\"UUID\": \"")
			.append(basis.uuid)
			.append("\", \"Grades\": {\"count\": ")
			.append(basis.getGradeCount());
			
		rB	.append(", \"grade\": [");
		for (int k = 0; k < basis.getGradeCount(); k++) {						//loop through grades constructing grade ranges.
			rB	.append("{\"rank\": ")
				.append(k)
				.append(", \"range\": \"");
			rB	.append(basis.getGrades().get(k))
				.append("-");
			if (k == basis.getGradeCount() - 1 )
					rB	.append(basis.getGrades().get(k));						//this is why the pscalar is handled separately
			else 	rB	.append(basis.getGrades().get(k + 1) - 1);				//there is is a next higher grade
			rB	.append("\"}, ");
			}
		rB	.deleteCharAt(rB.length()-1);										//lop the last character (removes trailing space)
		rB	.deleteCharAt(rB.length()-1);										//lop the last character (removes trailing comma)
		rB	.append("]}, ");

		rB	.append("\"Blades\": {\"count\": ")
			.append(basis.getBladeCount());
		rB	.append(", \"blade\": [");
		basis.bladeStream().forEach(blade -> {
			StringBuffer workThis = new StringBuffer(GExporter.toJSON(blade, true));
			workThis.delete(0,9);
			workThis.deleteCharAt(workThis.length()-1);
			workThis.append(",");
			rB.append(workThis);
		});
		rB	.deleteCharAt(rB.length()-1);									//lop the last character (removes trailing comma)
		rB	.append("]}}}");
		return rB.toString();
	}

	/**
	 * Export a GProduct as a JSON fragment. Object properties are represented as attributes.
	 * This is intended as an output format.
	 * <br><br>
	 * Example: <br>
	 * {<br>
	 * 	"gproduct": {<br>
	 * 		"signature": "+++0", <br>
	 * 		"cayleytable": [[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16], <br>
	 * 						[2, 1, 6, 7, 8, 3, 4, 5, 12, 13, 14, 9, 10, 11, 16, 15], <br>
	 * 						[3, -6, 1, 9, 10, -2, -12, -13, 4, 5, 15, -7, -8, -16, 11, -14], <br>
	 * 						[4, -7, -9, 1, 11, 12, -2, -14, -3, -15, 5, 6, 16, -8, -10, 13], <br>
	 * 						[5, -8, -10, -11, 0, 13, 14, 0, 15, 0, 0, -16, 0, 0, 0, 0], <br>
	 * 						[6, -3, 2, 12, 13, -1, -9, -10, 7, 8, 16, -4, -5, -15, 14, -11], <br>
	 * 						[7, -4, -12, 2, 14, 9, -1, -11, -6, -16, 8, 3, 15, -5, -13, 10], <br>
	 * 						[8, -5, -13, -14, 0, 10, 11, 0, 16, 0, 0, -15, 0, 0, 0, 0], <br>
	 * 						[9, 12, -4, 3, 15, -7, 6, 16, -1, -11, 10, -2, -14, 13, -5, -8], <br>
	 * 						[10, 13, -5, -15, 0, -8, -16, 0, 11, 0, 0, 14, 0, 0, 0, 0], <br>
	 * 						[11, 14, 15, -5, 0, 16, -8, 0, -10, 0, 0, -13, 0, 0, 0, 0], <br>
	 * 						[12, 9, -7, 6, 16, -4, 3, 15, -2, -14, 13, -1, -11, 10, -8, -5], <br>
	 * 						[13, 10, -8, -16, 0, -5, -15, 0, 14, 0, 0, 11, 0, 0, 0, 0], <br>
	 * 						[14, 11, 16, -8, 0, 15, -5, 0, -13, 0, 0, -10, 0, 0, 0, 0], <br>
	 * 						[15, -16, 11, -10, 0, -14, 13, 0, -5, 0, 0, 8, 0, 0, 0, 0], <br>
	 * 						[16, -15, 14, -13, 0, -11, 10, 0, -8, 0, 0, 5, 0, 0, 0, 0] ]<br>
	 * 	}<br>
	 * }
	 * <br><br>
	 * @param pG GProduct to be exported as JSON
	 * @return String formatted as JSON containing information about the input
	 */
	public final static String toJSON(GProduct pG) {
		StringBuilder rB = new StringBuilder();
		rB	.append("{\"gproduct\": {\"signature\": \"")
			.append(pG.signature());
		rB	.append("\", \"cayleytable\": [");

		pG.getBasis().bladeStream().forEach(blade0 -> {
			StringBuffer workThis0 = new StringBuffer("[");
			pG.getBasis().bladeStream().forEach(blade1 -> {
				int p = pG.getBasis().find(blade0) - 1;
				int q = pG.getBasis().find(blade1) - 1;	
				workThis0.append(pG.getResult(p, q)+",");
			});
			workThis0.deleteCharAt(workThis0.length()-1);
			workThis0.append("],");
			//workThis0.replace(workThis0.length()-2, workThis0.length()-1, "],");	//swap trailing comma and space -> for bracket and comma
			rB.append(workThis0);
		});
		rB	.deleteCharAt(rB.length()-1);
		//rB	.deleteCharAt(rB.length()-1);									//lop the last character (removes trailing space)
		//rB	.deleteCharAt(rB.length()-1);									//lop the last character (removes trailing comma)
		rB	.append("]}}");
		return rB.toString();
	}

    /**
	 * Export a Foot as a small XML fragment. Object properties are represented as attributes.
	 * This is intended as an output format.
	 * <br><br>
	 * @param pF Foot the be exported as XML.
	 * @param indent String indentation to assist with human readability of output XML data
	 * @return String formatted as XML containing information about the Foot
	 */
	public final static String toXMLString(Foot pF, String indent) {
		if (indent == null)		indent = "\t\t";
		StringBuilder rB = new StringBuilder(indent + "<Foot name=\"");
		rB	.append(pF.getName())
			.append("\" />\n");
		return rB.toString();
	}

	/**
	 * This method produces a printable and parseable string that represents the
	 * Blade in a human readable form.
	 * <br>
	 * This variation uses a Generator's ordinal to name it in the generator list.
	 * <br>
	 * @param blade  The Blade to be exported to XML.
	 * @param indent String of 'tab' characters that help space the output correctly
	 *               visually. It's not actually necessary except for human
	 *               readability of the output.
	 * @return String The XML formated String representing the Blade.
	 */
	public final static String toXMLOrdString(Blade blade, String indent) {
		if (indent == null)		indent = "\t\t\t\t\t\t\t\t";
		StringBuilder rB = new StringBuilder(indent);
		rB	.append("<Blade key=\"")
			.append(blade.key())
			.append("\" bitKey=\"0b");

		int pad = blade.maxGen - Integer.toBinaryString(blade.bitKey()).length();
		while (pad>0) {
			rB.append("0");
			pad--;
		}
		rB	.append(Integer.toBinaryString(blade.bitKey()))
			.append("\" generators=\"");

		blade	.generatorStream()
				.forEachOrdered(gen -> rB.append(gen.ord).append(","));

		if (blade.rank() > 0)
			rB.deleteCharAt(rB.length() - 1);
		rB	.append("\" />\n");
		return rB.toString();
	}

	/**
	 * This method produces a printable and parseable string that represents the
	 * Blade in a human readable form.
	 * <br>
	 * This variation uses a Generator's name in the generator list.
	 * <br>
	 * @param blade  	The Blade to be exported to XML.
	 * @param indent 	String of 'tab' characters that help space the output correctly visually. 
	 * 					It's not actually necessary except for human readability of the output.
	 * @return String 	The XML formated String representing the Blade.
	 */
	public final static String toXMLString(Blade blade, String indent) {
		if (indent == null)		indent = "\t\t\t\t\t\t\t\t";
		StringBuilder rB = new StringBuilder(indent);
		rB	.append("<Blade key=\"")
			.append(blade.key())
			.append("\" bitKey=\"0b");

		int pad = blade.maxGen - Integer.toBinaryString(blade.bitKey()).length();
		while (pad>0) {
			rB.append("0");
			pad--;
		}
		rB	.append(Integer.toBinaryString(blade.bitKey()))
			.append("\" generators=\"");

		blade	.generatorStream()
				.forEachOrdered(g -> rB.append(g.toString()).append(","));

		if (blade.getGenerators().size() > 0)
			rB.deleteCharAt(rB.length() - 1);
		rB	.append("\" />\n");
		return rB.toString();
	}

	/**
	 * This method produces a printable and parseable string that represents the
	 * BladeDuet in a human readable form. This is likely ONLY useful during debug
	 * efforts.
	 * <br>
	 * This variation uses a Generator's name in the generator list.
	 * <br>
	 * @param pBD The blade duet to export as XML.
	 * @return String The XML formated String representing the BladeDuet.
	 */
	/*
	public final static String toXMLString(BladeDuet pBD) {
		StringBuilder rB = new StringBuilder();
		rB.append("<BladeDuet sign=\"").append(pBD.sign).append("\" maxGrade=\"").append(pBD.maxGen.ord).append("\" generators=\"");
		pBD.bladeDuet.stream().forEachOrdered(g -> rB.append(g.toString() + ","));
		if (pBD.bladeDuet.size() > 0)
			rB.deleteCharAt(rB.length() - 1);
		rB.append("\" />\n");
		return rB.toString();
	}
	 */
	
	/**
	 * This method produces a printable and parseable string that represents the
	 * Basis in a human readable form. return String
	 * <br>
	 * @param pB The Basis to export as XML
	 * @param indent String of 'tab' characters that help space the output correctly
	 *               visually. It's not actually necessary except for human
	 *               readability of the output.
	 * @return String
	 */
	public static String toXMLString(Basis pB, String indent) {
		if (indent == null)
			indent = "\t\t\t\t\t\t";
		StringBuilder rB = new StringBuilder(indent + "<Basis UUID=\"");
		rB	.append(pB.uuid)
			.append("\">\n");
		// ------------------------------------------------------------------
		rB.append(indent)
			.append("\t<Grades count=\"")
			.append(pB.getGradeCount() + "\">\n");
		for (int k = 0; k <= pB.getGradeCount() - 2; k++) // loop to get all but the highest grade
			rB	.append(indent)
				.append("\t\t<Grade rank=\"")
				.append(k)
				.append("\" range=\"")
				.append(pB.getGrades().get(k))
				.append("-")
				.append((pB.getGrades().get(k + 1) - 1))
				.append("\" />\n");
		// Handle last grade separate. There is no k+1 index for the largest grade
		rB	.append(indent)
			.append("\t\t<Grade rank=\"")
			.append((pB.getGradeCount() - 1))
			.append("\" range=\"")
			.append(pB.getGrades().get(pB.getGradeCount() - 1))
			.append("-")
			.append(pB.getGrades().get(pB.getGradeCount() - 1))
			.append("\" />\n");
		rB.append(indent)
			.append("\t</Grades>\n");
		// ------------------------------------------------------------------
		rB	.append(indent)
			.append("\t<Blades count=\"")
			.append(pB.getBladeCount())
			.append("\">\n");
		for (int k = 0; k < pB.bladeList.size(); k++) // Appending blades
			rB	.append(GExporter.toXMLString(pB.bladeList.get(k), indent + "\t\t"));
		rB	.append(indent)
			.append("\t</Blades>\n");
		// ------------------------------------------------------------------
		rB	.append(indent)
			.append("</Basis>\n");
		return rB.toString();
	}

	/**
	 * This method produces a printable and parseable string that represents the GProduct in a human readable form.
	 * NOTE the GProduct no longer exports the Basis on which it is built. It exports the Cayley table and signature.
	 * <br>
	 * @param pG A geometric product to be exported to XML
	 * @param indent A string to use for XML element intentation. Not required.
	 * @return String This is the XML string export of an object.
	 */
	public final static String toXMLString(GProduct pG, String indent) {
		if (indent == null)			indent = "\t\t\t\t\t";
		StringBuilder rB = new StringBuilder(indent + "<GProduct signature=\""+pG.signature()+"\">\n");
		//rB	.append(GExporter.toXMLString(pG.getBasis(), indent + "\t"));
		rB	.append(indent)
			.append("\t<CayleyTable rows=\"")
			.append(pG.getBladeCount())
			.append("\">\n");
		for (int k = 0; k < pG.getBladeCount(); k++) {		// Appending rows of the Cayley table
			rB	.append(indent)
				.append("\t\t<row id=\"") 
				.append(k)
				.append("\" cells=\"");
			for (int m = 0; m < pG.getBladeCount(); m++)
				rB	.append(pG.getResult(k, m))
					.append(",");
			rB.deleteCharAt(rB.length() - 1);
			rB.append("\" />\n");
		}
		rB.append(indent + "\t</CayleyTable>\n");
		rB.append(indent + "</GProduct>\n");
		return rB.toString();
	}
	
	/**
	 * This is an exporter of internal details to XML. It exists to bypass certain security concerns related to 
	 * Java serialization of objects.
	 * <br><br>
	 * @param pA     Algebra to be exported as XML data
	 * @param indent String of tab characters to assist with human readability of output.
	 * @return String formatted as XML containing information about the Algebra
	 */
	public final static String toXMLString(Algebra pA, String indent) {
		if (indent == null)		indent = "\t\t\t\t";
		StringBuilder rB = new StringBuilder(indent+"<Algebra name=\"");
		rB	.append(pA.getAName())
			.append("\" UUID=\"")
			.append(pA.uuid)
			.append("\" >\n");
		
		rB	.append(GExporter.toXMLString(pA.getFoot(), indent + "\t"));	//Algebra owns a reference to a Foot
		rB	.append(GExporter.toXMLString(pA.getBasis(), indent+"\t"));		//Algebra owns a reference to a Basis
		rB	.append(GExporter.toXMLString(pA.getGP(), indent + "\t"));		//Algebra owns a reference to a GP
		
		rB	.append(indent)
			.append("</Algebra>\n");
		return rB.toString();
	}

	/**
	 * This is a short exporter of internal details to XML. It exists to bypass certain security concerns related to Java serialization.
	 * <br><br>
	 * @param pS The Scale oject to be output as XML
	 * @param indent String of 'tab' characters to get spacing right for human readable XML output.
	 * @return String formatted as XML containing information about the Scale
	 */
	public final static String toXMLString(Scale<?> pS, String indent) {
		StringBuilder rB = new StringBuilder(indent);		
		rB	.append("<Scale mode=\""+pS.getMode()+"\" ");
		rB	.append("pans=\""+pS.map.size()+"\" ")
			.append("cardinal=\""+pS.getCardinal().getUnit()+"\">\n");

		pS.getBasis().bladeStream().forEach(blade -> {
			rB	.append(indent+"\t")
				.append("<Pair bitKey=\"0b");
			int pad = blade.maxGen - Integer.toBinaryString(blade.bitKey()).length();
			while (pad>0) {
				rB.append("0");
				pad--;
			}
			rB	.append(Integer.toBinaryString(blade.bitKey()));
			//rB	.append("\" cardinal=\""+pS.map.get(blade).getCardinalString());
			rB	.append("\" ");
			
			switch (pS.getMode()){
				case REALF:
					rB	.append("realvalue=\""+((RealF)pS.map.get(blade)).getReal());
					break;
				case REALD:
					rB	.append("realvalue=\""+((RealD)pS.map.get(blade)).getReal());
					break;
				case COMPLEXF:
					rB	.append("realvalue=\""+((ComplexF)pS.map.get(blade)).getReal())
						.append("\" imgvalue=\""+((ComplexF)pS.map.get(blade)).getImg());
					break;
				case COMPLEXD:
					rB	.append("realvalue=\""+((ComplexD)pS.map.get(blade)).getReal())
						.append("\" imgvalue=\""+((ComplexD)pS.map.get(blade)).getImg());
					break;
				default:
					break;
				
			}
			rB	.append("\" />\n");
		});
		rB	.append(indent)
			.append("</Scale>\n");
		return rB.toString();
	}

	/**
	 * This is an exporter of internal details to XML. It exists to bypass certain security concerns related to Java serialization.
	 * <br><br>
	 * @param pS The Scale oject to be output as XML
	 * @param indent String of 'tab' characters to get spacing right for human readable XML output.
	 * @return String formatted as XML containing information about the Scale
	 */
	public final static String toXMLFullString(Scale<?> pS, String indent) {
		StringBuilder rB = new StringBuilder(indent);		
		rB	.append("<Scale mode=\""+pS.getMode()+"\" pans=\"")
			.append(pS.map.size())
			.append("\">\n");

		pS.getBasis().bladeStream().forEach(blade -> {
			rB	.append(indent)
				.append("\t\t\t<Pair>\n");
			rB	.append(indent)
				.append(GExporter.toXMLString(blade, "\t\t\t\t"));
			switch (pS.getMode()){
				case COMPLEXD -> {rB.append(indent + "\t\t\t\t").append(ComplexD.toXMLString((ComplexD) pS.map.get(blade))).append("\n");}
				case COMPLEXF -> {rB.append(indent + "\t\t\t\t").append(ComplexF.toXMLString((ComplexF) pS.map.get(blade))).append("\n");}
				case REALD -> 	{rB.append(indent + "\t\t\t\t").append(RealD.toXMLString((RealD) pS.map.get(blade))).append("\n");}
				case REALF -> 	{rB.append(indent + "\t\t\t\t").append(RealF.toXMLString((RealF) pS.map.get(blade))).append("\n");}
				default -> 		{rB.append(indent + "\t\t\t\t").append(ProtoN.toXMLString(pS.map.get(blade))).append("\n");}
			}	
			rB	.append(indent).append("\t\t\t</Pair>\n");
		});
		rB	.append(indent).append("\t\t</Scale>\n");
		return rB.toString();
	}

	/**
	 * Display XML string that represents the Monad
	 * <br>
	 * @param pM     MonadRealF This is the monad to be converted to XML.
	 * @param indent String of tab characters to assign with human readability
	 * @return String
	 */
	public final static String toXMLFullString(Monad pM, String indent) {
		if (indent == null)			indent = "\t\t\t";
		StringBuilder rB = new StringBuilder(indent + "<Monad ");
		rB.append("name=\"")
			.append(pM.getName())
			.append("\" gradeKey=\"")
			.append(pM.getGradeKey())
			.append("\" sparseFlag=\"")
			.append(pM.getSparseFlag())
			.append("\" >\n");
		rB.append(GExporter.toXMLString(pM.getAlgebra(), indent + "\t"));
		rB.append(indent)
			.append(GExporter.toXMLFullString(pM.scales, "\t"));
		rB.append(indent + "</Monad>\n");
		return rB.toString();
	}

	/**
	 * Display XML string that represents the Monad
	 * <br>
	 * @param pM     Monad This is the monad to be converted to XML.
	 * @param indent String of tab characters to assign with human readability
	 * @return String
	 */
	public final static String toXMLString(Monad pM, String indent) {
		if (indent == null)			indent = "\t\t\t";
		StringBuilder rB = new StringBuilder(indent + "<Monad ");
		rB.append("name=\"")
			.append(pM.getName())
			.append("\" algebra=\"")
			.append(pM.getAlgebra().getAName())
			.append("\" gradeKey=\"")
			.append(pM.getGradeKey())
			.append("\" sparseFlag=\"")
			.append(pM.getSparseFlag())
			.append("\" >\n");
		rB.append(indent)
			.append(GExporter.toXMLString(pM.scales, "\t"));
		rB.append(indent + "</Monad>\n");
		return rB.toString();
	}


	/**
	 * Display XML string that represents the Nyad and all its internal details
	 * <br>
	 * @param pN The Nyad to be exported as XML
	 * @param indent String of tab characters to assist with human readability.
	 * @return String
	 */
	public final static String toXMLFullString(Nyad pN, String indent) {
		if (indent == null)			indent = "\t";
		StringBuilder rB = new StringBuilder(indent+"<Nyad name=\"");
		rB	.append(pN.getName())
			.append("\" order=\"")
			.append(pN.arity())
			.append("\" algorder=\"")
			.append(pN.algrity())
			.append("\" >\n");
		
		rB	.append(GExporter.toXMLString(pN.getFoot(), indent + "\t"));
	
		rB	.append(indent)
			.append("\t<AlgebraList>\n");
		for (Algebra point : pN.algebraList)
			rB	.append(indent)
				.append("\t\t<AlgebraName>")
				.append(point.getAName())
				.append("</AlgebraName>\n");
		rB	.append(indent)
			.append("\t</AlgebraList>\n");
		
		rB	.append(indent)
			.append("\t<MonadList>\n");
		for (Monad tSpot : pN.monadList)
			rB.append(GExporter.toXMLFullString(tSpot, indent + "\t\t"));
		rB	.append(indent)
			.append("\t</MonadList>\n");
	
		rB	.append(indent)
			.append("</Nyad>\n");
		return rB.toString();
	}

	/**
	 * Display XML string that represents the Nyad
	 * <br>
	 * @param pN The Nyad to be exported as XML
	 * @param indent String of tab characters to assist with human readability.
	 * @return String
	 */
	public final static String toXMLString(Nyad pN, String indent) {
		if (indent == null)			indent = "\t";
		StringBuilder rB = new StringBuilder(indent+"<Nyad name=\"");
		rB	.append(pN.getName())
			.append("\" order=\"")
			.append(pN.arity())
			.append("\" algorder=\"");
		rB	.append(pN.algrity())
			.append("\" >\n");
				
		rB	.append(GExporter.toXMLString(pN.getFoot(), indent + "\t"));
		
		rB	.append(indent + "\t<MonadList>\n");
		for (Monad tSpot : pN.monadList)
			rB.append(GExporter.toXMLString(tSpot, indent + "\t\t"));
		rB.append(indent).append("\t</MonadList>\n");
		
		rB.append(indent).append("</Nyad>\n");
		return rB.toString();
	}

	/**
	 * Display XML string that represents the Connection and all its internal details
	 * <br><br>
	 * @param pC The Connection to be exported as XML
	 * @return String XML output of the offered Connection
	 */
	public final static String toXMLFullString(Connection<?> pC) {
		
		StringBuilder rB = new StringBuilder("<Connection  mode=\""+pC.getMode());
		rB	.append("\" cardinal=\""+pC.getCardinal().getUnit()+"\">\n");

		rB	.append(GExporter.toXMLString(pC.getAlgebra(true), "\t"));
		rB	.append(GExporter.toXMLString(pC.getAlgebra(false), "\t"));

		pC.bladeStream().forEach(blade -> {
			rB	.append("\t").append("<OuterMap>\n");
			rB	.append("\t").append(GExporter.toXMLString(blade, "\t"));
			rB	.append("\t").append(GExporter.toXMLString(pC.get(blade), "\t"));
			rB	.append("\t").append("</OuterMap>\n");
		});

		rB	.append("</Connection>\n");
		return rB.toString();
	}
}