class A {
    o : SELF_TYPE;
};

class B {
    o : SELF_TYPE;
};

class Arbol {
    hoja : Object;
};

class Nogal inherits Arbol {
    fruto : Object;
};

class Damasco inherits Arbol {
    fruto : Object <- "Damasco";
};

class Main {



    main() : SELF_TYPE { 
        let o : Object, tree : Arbol in {
            o <- let a : A, b : B in
                if true then
                   a
                else
                    b
                fi;

            tree <- let n : Nogal, d : Damasco in 
                if false then 
                    n
                else
                    d
                fi;

            self;
        }
    };
};