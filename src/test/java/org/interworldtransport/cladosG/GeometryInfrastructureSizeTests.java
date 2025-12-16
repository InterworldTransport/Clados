package org.interworldtransport.cladosG;

import java.util.EnumSet;

import org.interworldtransport.cladosF.Cardinal;
import org.interworldtransport.cladosF.ProtoN;

import org.interworldtransport.cladosGExceptions.BadSignatureException;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
//import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.info.GraphLayout;

class GeometryInfrastructureSizeTests {

    @Nested
    class reportAlgebraSizes {
        Algebra d0 ;
        ProtoN p0 = new ProtoN(Cardinal.generate(""));
        long deepSize;

        @Test
        void testAnAlgebra() throws BadSignatureException {
            d0 = GBuilder.createAlgebra(p0, "", "", "+");
            deepSize = GraphLayout.parseInstance(d0).totalSize();
            System.out.println("algebra|E1| " + deepSize + " \\| ");

            d0 = GBuilder.createAlgebra(p0, "", "", "++");
            deepSize = GraphLayout.parseInstance(d0).totalSize();
            System.out.println("algebra|E2| " + deepSize + " \\| ");

            d0 = GBuilder.createAlgebra(p0, "", "", "+++");
            deepSize = GraphLayout.parseInstance(d0).totalSize();
            System.out.println("algebra|E3| " + deepSize + " \\| ");

            d0 = GBuilder.createAlgebra(p0, "", "", "++++");
            deepSize = GraphLayout.parseInstance(d0).totalSize();
            System.out.println("algebra|E4| " + deepSize + " \\| ");

            d0 = GBuilder.createAlgebra(p0, "", "", "+++++");
            deepSize = GraphLayout.parseInstance(d0).totalSize();
            System.out.println("algebra|E5| " + deepSize + " \\| ");

            d0 = GBuilder.createAlgebra(p0, "", "", "++++++");
            deepSize = GraphLayout.parseInstance(d0).totalSize();
            System.out.println("algebra|E6| " + deepSize + " \\| ");

            d0 = GBuilder.createAlgebra(p0, "", "", "+++++++");
            deepSize = GraphLayout.parseInstance(d0).totalSize();
            System.out.println("algebra|E7| " + deepSize + " \\| ");

            d0 = GBuilder.createAlgebra(p0, "", "", "++++++++");
            deepSize = GraphLayout.parseInstance(d0).totalSize();
            System.out.println("algebra|E8| " + deepSize + " \\| ");

            d0 = GBuilder.createAlgebra(p0, "", "", "+++++++++");
            deepSize = GraphLayout.parseInstance(d0).totalSize();
            System.out.println("algebra|E9| " + deepSize + " \\| ");

            d0 = GBuilder.createAlgebra(p0, "", "", "++++++++++");
            deepSize = GraphLayout.parseInstance(d0).totalSize();
            System.out.println("algebra|EA| " + deepSize + " \\| ");

            d0 = GBuilder.createAlgebra(p0, "", "", "+++++++++++");
            deepSize = GraphLayout.parseInstance(d0).totalSize();
            System.out.println("algebra|EB| " + deepSize + " \\| ");

            d0 = GBuilder.createAlgebra(p0, "", "", "++++++++++++");
            deepSize = GraphLayout.parseInstance(d0).totalSize();
            System.out.println("algebra|EC| " + deepSize + " \\| ");
            /*
            d0 = GBuilder.createAlgebra(p0, "", "", "+++++++++++++");
            deepSize = GraphLayout.parseInstance(d0).totalSize();
            System.out.println("algebra|ED| " + deepSize + " \\| ");

            d0 = GBuilder.createAlgebra(p0, "", "", "++++++++++++++");
            deepSize = GraphLayout.parseInstance(d0).totalSize();
            System.out.println("algebra|EE| " + deepSize + " \\| ");

            d0 = GBuilder.createAlgebra(p0, "", "", "+++++++++++++++");
            deepSize = GraphLayout.parseInstance(d0).totalSize();
            System.out.println("algebra|EF| " + deepSize + " \\| ");
            */
        }
    }

    @Nested
    class reportGPSizes {
        GProduct c0;
        long deepSize;

        @Test
        void testAGP() throws BadSignatureException {
            c0 = GBuilder.createGProduct("+");
            deepSize = GraphLayout.parseInstance(c0).totalSize();
            System.out.println("GP|E1| " + deepSize + " \\| ");

            c0 = GBuilder.createGProduct("++");
            deepSize = GraphLayout.parseInstance(c0).totalSize();
            System.out.println("GP|E2| " + deepSize + " \\| ");

            c0 = GBuilder.createGProduct("+++");
            deepSize = GraphLayout.parseInstance(c0).totalSize();
            System.out.println("GP|E3| " + deepSize + " \\| ");

            c0 = GBuilder.createGProduct("++++");
            deepSize = GraphLayout.parseInstance(c0).totalSize();
            System.out.println("GP|E4| " + deepSize + " \\| ");

            c0 = GBuilder.createGProduct("+++++");
            deepSize = GraphLayout.parseInstance(c0).totalSize();
            System.out.println("GP|E5| " + deepSize + " \\| ");

            c0 = GBuilder.createGProduct("++++++");
            deepSize = GraphLayout.parseInstance(c0).totalSize();
            System.out.println("GP|E6| " + deepSize + " \\| ");

            c0 = GBuilder.createGProduct("+++++++");
            deepSize = GraphLayout.parseInstance(c0).totalSize();
            System.out.println("GP|E7| " + deepSize + " \\| ");

            c0 = GBuilder.createGProduct("++++++++");
            deepSize = GraphLayout.parseInstance(c0).totalSize();
            System.out.println("GP|E8| " + deepSize + " \\| ");

            c0 = GBuilder.createGProduct("+++++++++");
            deepSize = GraphLayout.parseInstance(c0).totalSize();
            System.out.println("GP|E9| " + deepSize + " \\| ");

            c0 = GBuilder.createGProduct("++++++++++");
            deepSize = GraphLayout.parseInstance(c0).totalSize();
            System.out.println("GP|EA| " + deepSize + " \\| ");

            c0 = GBuilder.createGProduct("+++++++++++");
            deepSize = GraphLayout.parseInstance(c0).totalSize();
            System.out.println("GP|EB| " + deepSize + " \\| ");

            c0 = GBuilder.createGProduct("++++++++++++");
            deepSize = GraphLayout.parseInstance(c0).totalSize();
            System.out.println("GP|EC| " + deepSize + " \\| ");
            
            //c0 = GBuilder.createGProduct("+++++++++++++");
            //deepSize = GraphLayout.parseInstance(c0).totalSize();
            //System.out.println("GP|ED| " + deepSize + " \\| ");

            //c0 = GBuilder.createGProduct("++++++++++++++");
            //deepSize = GraphLayout.parseInstance(c0).totalSize();
            //System.out.println("GP|EE| " + deepSize + " \\| ");

            //c0 = GBuilder.createGProduct("+++++++++++++++");
            //deepSize = GraphLayout.parseInstance(c0).totalSize();
            //System.out.println("GP|EF| " + deepSize + " \\| ");
            
            
            //System.out.println("--- Analysis of Blade object layout (Shallow) ---");
            //System.out.println(ClassLayout.parseInstance(c0).toPrintable());

            // Get the deep size (object and all referenced objects)
            //long deepSize = GraphLayout.parseInstance(c0).totalSize();
            //System.out.println("--- Analysis of Blade object layout (Deep) ---");
            //System.out.println("GP|E4| " + deepSize + " \\| ");
            //System.out.println("----------------------------------------------");
            // Print a detailed layout/footprint
            //System.out.println(GraphLayout.parseInstance(c0).toPrintable());
        }
    }

    @Nested
    class reportBasisSizes {
        Basis b0;
        long deepSize;

        @Test
        void testABasis() {
            b0 = Basis.using(Generator.E1);
            deepSize = GraphLayout.parseInstance(b0).totalSize();
            System.out.println("Basis|E1| " + deepSize + " \\| ");

            b0 = Basis.using(Generator.E2);
            deepSize = GraphLayout.parseInstance(b0).totalSize();
            System.out.println("Basis|E2| " + deepSize + " \\| ");

            b0 = Basis.using(Generator.E3);
            deepSize = GraphLayout.parseInstance(b0).totalSize();
            System.out.println("Basis|E3| " + deepSize + " \\| ");

            b0 = Basis.using(Generator.E4);
            deepSize = GraphLayout.parseInstance(b0).totalSize();
            System.out.println("Basis|E4| " + deepSize + " \\| ");

            b0 = Basis.using(Generator.E5);
            deepSize = GraphLayout.parseInstance(b0).totalSize();
            System.out.println("Basis|E5| " + deepSize + " \\| ");

            b0 = Basis.using(Generator.E6);
            deepSize = GraphLayout.parseInstance(b0).totalSize();
            System.out.println("Basis|E6| " + deepSize + " \\| ");

            b0 = Basis.using(Generator.E7);
            deepSize = GraphLayout.parseInstance(b0).totalSize();
            System.out.println("Basis|E7| " + deepSize + " \\| ");

            b0 = Basis.using(Generator.E8);
            deepSize = GraphLayout.parseInstance(b0).totalSize();
            System.out.println("Basis|E8| " + deepSize + " \\| ");

            b0 = Basis.using(Generator.E9);
            deepSize = GraphLayout.parseInstance(b0).totalSize();
            System.out.println("Basis|E9| " + deepSize + " \\| ");

            b0 = Basis.using(Generator.EA);
            deepSize = GraphLayout.parseInstance(b0).totalSize();
            System.out.println("Basis|EA| " + deepSize + " \\| ");

            b0 = Basis.using(Generator.EB);
            deepSize = GraphLayout.parseInstance(b0).totalSize();
            System.out.println("Basis|EB| " + deepSize + " \\| ");

            b0 = Basis.using(Generator.EC);
            deepSize = GraphLayout.parseInstance(b0).totalSize();
            System.out.println("Basis|EC| " + deepSize + " \\| ");
            
            b0 = Basis.using(Generator.ED);
            deepSize = GraphLayout.parseInstance(b0).totalSize();
            System.out.println("Basis|ED| " + deepSize + " \\| ");

            b0 = Basis.using(Generator.EE);
            deepSize = GraphLayout.parseInstance(b0).totalSize();
            System.out.println("Basis|EE| " + deepSize + " \\| ");

            b0 = Basis.using(Generator.EF);
            deepSize = GraphLayout.parseInstance(b0).totalSize();
            System.out.println("Basis|EF| " + deepSize + " \\| ");
            
            
            //System.out.println("--- Analysis of Blade object layout (Shallow) ---");
            //System.out.println(ClassLayout.parseInstance(b0).toPrintable());

            // Get the deep size (object and all referenced objects)
            //long deepSize = GraphLayout.parseInstance(b0).totalSize();
            //System.out.println("--- Analysis of Blade object layout (Deep) ---");
            //System.out.println("Basis|E4| " + deepSize + " \\| ");
            //System.out.println("----------------------------------------------");
            // Print a detailed layout/footprint
            //System.out.println(GraphLayout.parseInstance(b0).toPrintable());
        }
    }

    @Nested
    class reportBladeSizes {
        EnumSet<Generator> gstar = EnumSet.noneOf(Generator.class);
        Blade a0;
        long deepSize;

        @Test
        void testBlade(){

            gstar.add(Generator.E1);
            a0 = new Blade(Generator.E1, gstar);
            deepSize = GraphLayout.parseInstance(a0).totalSize();
            System.out.println("Blade|E1| " + deepSize + " \\| ");

            gstar.add(Generator.E2);
            a0 = new Blade(Generator.E2, gstar);
            deepSize = GraphLayout.parseInstance(a0).totalSize();
            System.out.println("Blade|E2| " + deepSize + " \\| ");

            gstar.add(Generator.E3);
            a0 = new Blade(Generator.E3, gstar);
            deepSize = GraphLayout.parseInstance(a0).totalSize();
            System.out.println("Blade|E3| " + deepSize + " \\| ");

            gstar.add(Generator.E4);
            a0 = new Blade(Generator.E4, gstar);
            deepSize = GraphLayout.parseInstance(a0).totalSize();
            System.out.println("Blade|E4| " + deepSize + " \\| ");

            gstar.add(Generator.E5);
            a0 = new Blade(Generator.E5, gstar);
            deepSize = GraphLayout.parseInstance(a0).totalSize();
            System.out.println("Blade|E5| " + deepSize + " \\| ");

            gstar.add(Generator.E6);
            a0 = new Blade(Generator.E6, gstar);
            deepSize = GraphLayout.parseInstance(a0).totalSize();
            System.out.println("Blade|E6| " + deepSize + " \\| ");

            gstar.add(Generator.E7);
            a0 = new Blade(Generator.E7, gstar);
            deepSize = GraphLayout.parseInstance(a0).totalSize();
            System.out.println("Blade|E7| " + deepSize + " \\| ");

            gstar.add(Generator.E8);
            a0 = new Blade(Generator.E8, gstar);
            deepSize = GraphLayout.parseInstance(a0).totalSize();
            System.out.println("Blade|E8| " + deepSize + " \\| ");

            gstar.add(Generator.E9);
            a0 = new Blade(Generator.E9, gstar);
            deepSize = GraphLayout.parseInstance(a0).totalSize();
            System.out.println("Blade|E9| " + deepSize + " \\| ");

            gstar.add(Generator.EA);
            a0 = new Blade(Generator.EA, gstar);
            //System.out.println(GExporter.toXMLOrdString(a0, ""));
            deepSize = GraphLayout.parseInstance(a0).totalSize();
            System.out.println("Blade|EA| " + deepSize + " \\| ");

            gstar.add(Generator.EB);
            a0 = new Blade(Generator.EB, gstar);
            //System.out.println(GExporter.toXMLOrdString(a0, ""));
            deepSize = GraphLayout.parseInstance(a0).totalSize();
            System.out.println("Blade|EB| " + deepSize + " \\| ");

            gstar.add(Generator.EC);
            a0 = new Blade(Generator.EC, gstar);
            //System.out.println(GExporter.toXMLOrdString(a0, ""));
            deepSize = GraphLayout.parseInstance(a0).totalSize();
            System.out.println("Blade|EC| " + deepSize + " \\| ");

            gstar.add(Generator.ED);
            Blade ad = new Blade(Generator.ED, gstar);
            //System.out.println(GExporter.toXMLOrdString(ad, ""));
            deepSize = GraphLayout.parseInstance(ad).totalSize();
            System.out.println("Blade|ED| " + deepSize + " \\| ");

            gstar.add(Generator.EE);
            Blade ae = new Blade(Generator.EE, gstar);
            //System.out.println(GExporter.toXMLOrdString(ae, ""));
            deepSize = GraphLayout.parseInstance(ae).totalSize();
            System.out.println("Blade|EE| " + deepSize + " \\| ");

            gstar.add(Generator.EF);
            Blade af = new Blade(Generator.EF, gstar);
            //System.out.println(GExporter.toXMLOrdString(af, ""));
            deepSize = GraphLayout.parseInstance(af).totalSize();
            System.out.println("Blade|EF| " + deepSize + " \\| ");

            //System.out.println("--- Analysis of Blade object layout (Shallow) ---");
            //System.out.println(ClassLayout.parseInstance(a0).toPrintable());

            // Get the deep size (object and all referenced objects)
            //long deepSize = GraphLayout.parseInstance(a0).totalSize();
            //System.out.println("Blade| " + deepSize + " \\| ");

            // Print a detailed layout/footprint
            //System.out.println(GraphLayout.parseInstance(af).toPrintable());

            }
    }
    
}
