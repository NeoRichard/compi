class Main {
    num : Int;
    bool : Bool;


    main() : Int {
        { 
            bool <- num = 42;
            bool <- 15 < num;
            bool <- num <= 3;
            num <- 39 / num;
            num <- 6 * 9;
            num <- 1 - num;
            num <- 56 - 93;
        }
    };
};