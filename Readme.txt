Copyright © 2021 Alfred Differ

Welcome to Clados …so what is it? 

It is a set of java packages that implement multi-vectors in Clifford Algebras.
	cladosG			- The geometry package
	cladosF			- The number package
	cladosGExceptions	- The geometry exception package
	cladosFExceptions	- The number exception package
	cladosGTest		- The JUnit tests that support cladosG
	cladosFTest		- The JUnit tests that support cladosF

If you don't know what a Clifford Algebra is, consider yourself encouraged to 
read about them elsewhere.  Geometric Algebras are a variation on a theme, so 
don't bypass content describing them. In a nutshell, they offer a mathematical
formalism combining many of the disparate tools of geometry enabling one to 
wield them in the ways we've become accustomed to from algebra. In particular,
the approach contained in this library evolved from one developed at UC Davis 
between the late 1970's and early 1990's. It is intended to support physical
models in the style taught by Prof. Ken Greider.

There is a project related book that might help, but it is admittedly stale 
AND doesn't have introductory material. The book focuses upon package contents, 
what they do and why, and finishes with usage examples largely associated with 
physics simulations. Two parts are now stale. The first involves the API and
the second involves how Mechanics is modeled on a multivector basis.

The physics models described in the book will not survive unchanged once this
library reaches version 2.0, let alone once the physics packages are started. 
This library and the book are actually an exploration/research project 
regarding polycovariance and the broader meaning of 'meaning' in physical models. 
Maintain a healthy sense of skepticism... and try things for yourself.

https://docs.google.com/open?id=0B9BesRp5Ts5FM00yNTlva0dlbEE

If you have problems or think you have found an issue in Clados, please report
it at the Clados project site at GitHub. If you can take the time to look and 
see if it is already reported, it would be appreciated. Tell us you found it 
either way, though, because it helps to know the number of impacted people.

If you want to check up on what is going on, feel free to drop by the Twitter
account for Interworld Transport. That is where status updates are posted lately.


Thanks for your support!

	-al

------------------------------------------------------------------------
Installation:

Clados is a collection of packages meant to be used as a math library in other 
applications. Place clados.jar where you normally place 3rd party libraries.

Don't have the jar file? Build it. Use Ant tasks in build.xml to do it.
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
Larger ones can be negotiated.  

	-al
------------------------------------------------------------------------
