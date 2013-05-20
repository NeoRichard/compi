class Being {
    o : SELF_TYPE;
};

class Human {
    o : SELF_TYPE;
};

class Dog {
    o : SELF_TYPE;
};

class Candy {
    o : SELF_TYPE;
};

class CrazyOldMan {
    o : SELF_TYPE;
};

class Elemental {
    o : SELF_TYPE;
};

class Main {

    person : Object;

    main() : String {
        case person of 
            finn : Human => "mathematical";
            jake : Dog => "woah, dude!";
            bubblegum : Candy => "my loyal candy people!";
            iceking : CrazyOldMan => "let's go, günther";
            flamePrincess : Elemental => "fwhoosh!";
        esac
    };
};