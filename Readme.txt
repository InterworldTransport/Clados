Copyright © 2025 Alfred Differ

Welcome to Clados …so what is it? 

It is a set of java packages that implement multi-vectors in Clifford Algebras.
	cladosG				- The geometry package
	cladosF				- The number package
	cladosGExceptions	- The geometry exception package
	cladosFExceptions	- The number exception package
	cladosGTest			- The JUnit tests that support cladosG
	cladosFTest			- The JUnit tests that support cladosF

If you don't know what a Clifford Algebra is, you are encouraged to 
read about them.  Geometric Algebras are modeling choice variations, so don't 
bypass content describing them either. They all offer a tidy mathematical 
formalism combining many disparate tools of geometry enabling one to wield 
them in the algebraic ways. 

The tools contained in this library evolved from work done at UC Davis 
between the late 1970's and early 1990's. It is intended to support physical
models that rely upon geometry in the style taught by Prof. Ken Greider.

------------------------------------------------------------------------

The word 'Clados' is an acronym for CLifford Algebra Derived ObjectS. 
It was chosen to avoid math library name collisions and because it comes 
close to the concept of 'clade' which this author finds apropos for multivectors.

Basic and complete geometric objects are modeled by the Monad class which 
most people think of as 'Multivector.' A modeling requirement for aggregates 
of multivectors drove the decision to use 'Monad' instead. 
'Mult-multivector' is a mouthful. 

Nyads are Monad aggregates and are modeled as lists. Names of lists of 
increasing size form a series {Monad, Dyad, Triad, Tetrad, ...}, so there 
is some sense to our convention. However, for implementors not interested 
in aggregates, the name 'Monad' is unfortunate. Treat it as a synonym.

------------------------------------------------------------------------

There is a project related book that might help, but it is admittedly stale 
AND doesn't have introductory material. The book focuses upon package contents, 
what they do and why, and finishes with usage examples largely associated with 
physics simulations. Two significant parts are very stale: the API and
the Mechanics model. The physics models in the book will not survive unchanged
when this library reaches version 2.0, so treat the library and book as an 
exploration/research project regarding polycovariance and the broader meaning 
of 'meaning' in physical models. Maintain a healthy sense of skepticism... 
and try things for yourself.

https://docs.google.com/open?id=0B9BesRp5Ts5FM00yNTlva0dlbEE

If you have problems or think you have found an issue in Clados, please report
it at the Clados project site at GitHub. If you can take the time to look and 
see if it is already reported, it would be appreciated. Tell us you found it 
either way, though, because it helps to know the number of impacted people.

If you want to check up on what is going on, drop by the Bivector Discord server
where I hang out, or leave a note at the GitHub project.

Thanks for your support!

	-al

------------------------------------------------------------------------
Installation:

Clados is a collection of packages meant to be used as a math library in other 
applications. Place the clados jar where you normally place 3rd party libraries.

Don't have the jar file? Build it. Or ask me to build one.
------------------------------------------------------------------------

Credits:

Alfred Differ <adiffer@gmail.com> wrote the original code. 

------------------------------------------------------------------------ 
---org.interworldtransport.*
------------------------------------------------------------------------
 * This code is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, 
 * or (at your option) any later version. 
 * 
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * Use of this code or executable objects derived from it by the Licensee 
 * states their willingness to accept the terms of the license.  
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see https://www.gnu.org/licenses/ .
 * 
------------------------------------------------------------------------
---org.interworldtransport.*
------------------------------------------------------------------------
Having said all that, if you want support, bug fixes, or a different license
arrangement, let me know. I understand that some cannot stick their heads out
regarding usage of code written by others. I AM interested in how people use 
this library, so small requests are likely to be supported free of charge.
Larger ones can be negotiated. I have a day job, so just take that into account. 

	-al
------------------------------------------------------------------------
