# Chomsky Normal Form

### Course: Formal Languages & Finite Automata
### Author: Timur Cravțov
### Group: FAF-231

----

## Theory

Chomsky Normal Form [^1] is a special form of Grammar where all the rules are in one of the following format: 

1) _A -> BC_
2) _S -> ε_, if S is the starting symbol
3) _A -> a_

Chomsky Normal Form is usually used to simplify Context Free Grammar (Type 2, CFG). The key advantage is that in Chomsky Normal
Form, every derivation of a string of n letters has
exactly 2n − 1 steps [^2].

Here is the steps of converting a Grammar to Chomsky Normal Form:

0) If *S* symbol is met in RHS, then we create a new starting state (typically called *S0*) and make a transition *S0 -> S*.

1) Eliminate ε-transitions. In case there is A -> ε, it is considered a nullable. if a non-terminal in RHS has all the letters nullable, it is nullable as well (E.g. if in *B -> ACDE*, *A*, *C*, *D*, *E* are nullables, B is nullable). When nullables is defined, in each production in form *A -> BCDaE*, we add a set of all possible productions, where the nullable is absent. E.g.: if *B* and *D* and *E* are nullable, then we create a 2<sup>3</sup> new productions: B -> BCaE | BCDa | CDaE | CDa | ... | Ca.
2) Eliminate Unit productions. For all productions if form *A -> B*, replace this production in *A -> {set of production (except unit) of the grammar with B in LHS}*
3) Split long productions. If we have *A -> bCD*, replace it with *A -> bE* \*, and add production *E -> CD*
4) After the 3rd step, only production which needs fix are *A -> bC*. Replace them with *A -> BC* and add *B -> b*

\* Of course, if the letter E or other is already used in the Grammar, need to replace it with some unique one. In the lab, `VariableFactory` class serves this purpose.

The obtained Chosmky Normal Form is not unique for the Grammar. One can define either different variables for holding intermediate states, or use different algorithm of grouping the terms. But in the end, the obtained Grammar is equivalent to the initial Grammar, which means, It can describe the same set of words. 

## Objectives:

1) Learn about Chomsky Normal Form (CNF)

2) Get familiar with the approaches of normalizing a grammar.

3) Implement a method for normalizing an input grammar by the rules of CNF.

## Implementation description

## Conclusions / Screenshots / Results

### Screenshots

### Conclusions

## References

[^1]: Lecture Notes

[^2]: Clemson univesity, Chomsky normal form. https://people.computing.clemson.edu/~goddard/texts/theoryOfComputation/9a.pdf
