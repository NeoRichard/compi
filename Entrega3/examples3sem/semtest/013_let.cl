class X86 {
    run() : SELF_TYPE {
        self
    };
};

class C {
    core : Object;
    compile() : X86 {
        new X86
    };
};

class Java inherits C {
    core : Object;
};

class CSharp inherits Java {
    core : Object;
};

class CPP inherits C {
    core : Object;
};

class D inherits C {
    core : Object;
};


class Main {

    shout(s : String) : SELF_TYPE {
        self
    };

    main() : SELF_TYPE {
        let 
            digitalMars : D,
            mono : CSharp,
            msvc : CPP,         
            kAndR : C <- digitalMars,
            sun : Java <- mono,
            clang : C <- digitalMars
        in {
            shout("Power X-treme");
            digitalMars.compile();
            let gpp : CPP,
                gcc : C <- gpp,
                machine : X86
                in {
                    mono.compile();
                    msvc.compile();
                    kAndR.compile();
                    sun.compile();
                    clang.compile();
                };
            self;
        }

    };
};