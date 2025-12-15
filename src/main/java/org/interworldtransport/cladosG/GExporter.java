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
	 * {"Foot": {"name": "NamedPoint"}}
	 * <br><br>
	 * @param pF Foot to be exported as JSON
	 * @return String formatted as JSON containing information about the Foot
	 */
	public final static String toJSON(Foot pF) {
		StringBuilder rB = new StringBuilder();
		rB	.append("{\"Foot\": {\"name\": \"")
			.append(pF.getName())
			.append("\"}}\n");
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
	 * @param blade  The Blade to be exported to XML.
	 * @param indent String of 'tab' characters that help space the output correctly
	 *               visually. It's not actually necessary except for human
	 *               readability of the output.
	 * @return String The XML formated String representing the Blade.
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
	public final static String toXMLString(BladeDuet pBD) {
		StringBuilder rB = new StringBuilder();
		rB.append("<BladeDuet sign=\"").append(pBD.sign).append("\" maxGrade=\"").append(pBD.maxGen.ord).append("\" generators=\"");
		pBD.bladeDuet.stream().forEachOrdered(g -> rB.append(g.toString() + ","));
		if (pBD.bladeDuet.size() > 0)
			rB.deleteCharAt(rB.length() - 1);
		rB.append("\" />\n");
		return rB.toString();
	}

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
	 * This method produces a printable and parseable string that represents the
	 * Basis in a human readable form.
	 * <br>
	 * @param pG A geometric product to be exported to XML
	 * @param indent A string to use for XML element intentation. Not required.
	 * @return String This is the XML string export of an object.
	 */
	public final static String toXMLString(GProduct pG, String indent) {
		if (indent == null)			indent = "\t\t\t\t\t";
		StringBuilder rB = new StringBuilder(indent + "<GProduct signature=\""+pG.signature()+"\">\n");
		rB	.append(GExporter.toXMLString(pG.getBasis(), indent + "\t"));
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
		
		rB	.append(GExporter.toXMLString(pA.getFoot(), indent + "\t"));			//Algebra owns a reference to a Foot
		rB	.append(GExporter.toXMLString(pA.getGP(), indent + "\t"));				//Algebra owns a reference to a GP
		
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
		rB	.append("<Scales mode=\""+pS.getMode()+"\" ");
		rB	.append("pans=\""+pS.map.size()+"\" ")
			.append("cardinal="+pS.getCardinal().getUnit()+"\">\n");

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
			.append("</Scales>\n");
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
		rB	.append("<Scales mode=\""+pS.getMode()+"\" pans=\"")
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
		rB	.append(indent).append("\t\t</Scales>\n");
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
}