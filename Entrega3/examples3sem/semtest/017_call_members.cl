class List {
    skip(n : Int) : List {
        self
    };

    take(n : Int) : List {
        self
    };

    single() : Object {
        new Object
    };

};

class House {
    list() : List {
        new List
    };
};

class Main {
    main() : Object {
        let house : House <- new House in
            house.list().skip(4).take(2).single()
    };
};