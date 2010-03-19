LambdaCalculus
==============

Scala implementation of a [lambda calculus][] interpreter.

[lambda calculus]: http://en.wikipedia.org/wiki/Lambda_calculus  "Lambda Calculus"


Installing
-----------

LambdaCalculus is set up as an SBT project.  See the SBT guide for instructions
on [how to set up SBT][Setup], [get the project running][RunningSbt] with an interactive
console, or how to build a jar.

[Setup]: http://code.google.com/p/simple-build-tool/wiki/Setup  "Setup SBT"
[RunningSbt]: http://code.google.com/p/simple-build-tool/wiki/RunningSbt  "Running SBT"


Usage
------

First import the Expression object:

    import us.sitr.lambdacalculus.Expression

Define a lambda calculus expression using a string.  You can use an actual
lambda character or a backslash to represent a lambda:

    scala> Expression("(\\m.\\n.\\f.\\x.m f (n f x)) (\\f.\\x.f x) (\\f.\\x.f (f x))")
    res1: us.sitr.lambdacalculus.Expression = (λm.λn.λf.λx.m f (n f x)) (λf.λx.f x) (λf.λx.f (f x))

Methods are available for performing the various conversions:

    scala> Expression("(\\ab.ba) c").betaReduction
    res2: us.sitr.lambdacalculus.Expression = λb.b c

    scala> Expression("\\x.f x").etaConversion
    res3: us.sitr.lambdacalculus.Expression = f

To perform an alpha conversion you must provide a Set of variables that you
want to eliminate from the expression:

    scala> import us.sitr.lambdacalculus.Var
    import us.sitr.lambdacalculus.Var
    
    scala> Expression("\\ab.ba") alphaConversion Set(Var("a"))
    res4: us.sitr.lambdacalculus.Expression = λa'.λb.b a'

You can reduce an expression to its simplest form using the `evaluate` method:

    scala> Expression("(\\m.\\n.\\f.\\x.m f (n f x)) (\\f.\\x.f x) (\\f.\\x.f (f x))").evaluate
    res5: us.sitr.lambdacalculus.Expression = λf.λx.f (f (f x))

You can also get interactive evaluation by passing a callback to `evaluate`:

    scala> Expression("(\\m.\\n.\\f.\\x.m f (n f x)) (\\f.\\x.f x) (\\f.\\x.f (f x))") evaluate { println(_) }
    (λm.λn.λf.λx.m f (n f x)) (λf.λx.f x) (λf.λx.f (f x))
    (λn.λf.λx.(λf.λx.f x) f (n f x)) (λf.λx.f (f x))
    λf.λx.(λf.λx.f x) f ((λf.λx.f (f x)) f x)
    λf.λx.(λx.f x) ((λf.λx.f (f x)) f x)
    λf.λx.f ((λf.λx.f (f x)) f x)
    λf.λx.f ((λx.f (f x)) x)
    λf.λx.f (f (f x))
    res6: us.sitr.lambdacalculus.Expression = λf.λx.f (f (f x))

Finally, to save you some typing there are some predefined constants that you
can use in expressions:

    scala> Expression("+ 1 2").evaluate
    res7: us.sitr.lambdacalculus.Expression = λf.λx.f (f (f x))

Here is the complete list of constants available:

    "0"     -> "λfx.x",
    "1"     -> "λfx.f x",
    "2"     -> "λfx.f (f x)",
    "3"     -> "λfx.f (f (f x))",
    "SUCC"  -> "λnfx.f (n f x)",
    "+"     -> "λmnfx.m f (n f x)",
    "*"     -> "λmn.m (+ n) 0",
    "^"     -> "λbe.e b",  // exponentiation
    "PRED"  -> "λnfx.n (λgh.h (g f)) (λu.x) (λu.u)",
    "-"     -> "λmn.n PRED m",
    "TRUE"  -> "λxy.x",
    "FALSE" -> "λxy.y",
    "&&"    -> "λpq.p q p",
    "||"    -> "λpq.p p q",
    "!"     -> "λpab.p b a"  // negation


License
--------

Copyright (c) 2010 Jesse Hallett

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
