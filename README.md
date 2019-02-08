
# mlang

## code

* packages and files is indexed by "a-z", resulting in a linear order of files. you 
should be able to read them one by one

## roadmap

* implement a logical framework with recursive records with **recursive values**,
 so we can test our idea about recursive representation of values
    * "miniTT" use explicit mutual patterns to deal with recursion and mutual recursion.
      I
      [once](https://github.com/molikto/ChihuahuaLang/blob/master/common/src/main/scala/Syntax.scala#L59)
      done with a single fix operator, but found this ugly
         * the reason of this is to support at least the delta rule for non-mutual recursive definitions
    * the current idea is unify "global scope" and "record type", viewing global scope just as a value of
      some record type. and in a record value definition, terms can have mutual recursive reference
         * this way we can have both local mutual recursive definition and global mutual recursive definition
         * the so called "global definition will be transparent" in value world, 
         each call of a global definition will be translated to a record projection, and then most of time, 
         can be reduced directly to a value itself, so in the value world we don't have any notion of "global scope"
    * the implementation will be a normalization by evaluation, the detailed nbe method will be studied...
         * maybe still untyped NBE following the previous attempt
         * but it is suggested to used a typed NBE
    * inductive type is also implemented so that we can get some data to play with
         * the implementation is bare-bone and not well thought, it is just simply tagged sum
    * this system is not sound, because we don't check the validity of inductive defined types and recursive definitions
    * everything is basic, for example, we don't have fancy error handling