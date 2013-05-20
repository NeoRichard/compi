class NobleChild {
    clone() : SELF_TYPE {
        self
    };
};

class VonFabre inherits NobleChild {
    clone() : SELF_TYPE {
        new VonFabre
    };
};


class Main {
    main() : Object {
        let luke : VonFabre in
            luke@NobleChild.clone()
    };
};