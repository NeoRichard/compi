@true = global i1 1
@false = global i1 0
@zero = global i32 0
@vars_0_c = constant [36 x i8] c"What a terrible night to be cursed!\0A"
@vars_0 = global i8* bitcast([36 x i8]* @vars_0_c to i8*)

%Object = type { i8* }
%IO = type { i8* }

@.type.Program = private constant [7 x i8] c"Program"

%Program= type {
    i8*,
    %Brocoli
};

define %Program* @newProgram() {
    %vptr = call i8* @malloc( i64 ptrtoint (%Program* getelementptr (%Program* null, i32 1) to i64) )
    %ptr = bitcast i8* %vptr to %Program* 
    %typePtr = getelementptr %Program* %ptr, i32 0, i32 0
    store i8* bitcast( [7 x i8]* @.type.Program to i8*), i8** %typePtr
    ;
    ; inicializacion de los los fields del objeto recién creado

    ret %Program* %ptr
}
;;;print(ClassDef c)
define %Program* @Program_main(%Program* %m) {
        %_tmp_1 = bitcast %Program* %m to %IO*
    call %Program* @Main_print(%Main* null, )
    ret %Program* %m
}

@.type.Brocoli = private constant [7 x i8] c"Brocoli"

%Brocoli= type {
    i8*,

};

define %Brocoli* @newBrocoli() {
    %vptr = call i8* @malloc( i64 ptrtoint (%Brocoli* getelementptr (%Brocoli* null, i32 1) to i64) )
    %ptr = bitcast i8* %vptr to %Brocoli* 
    %typePtr = getelementptr %Brocoli* %ptr, i32 0, i32 0
    store i8* bitcast( [7 x i8]* @.type.Brocoli to i8*), i8** %typePtr
    ;
    ; inicializacion de los los fields del objeto recién creado

    ret %Brocoli* %ptr
}
;;;print(ClassDef c)
define %Brocoli* @Brocoli_print(%Brocoli* %m,  %iostream) {
        %_tmp_1 = bitcast %Brocoli* %m to %IO*
    %lvars_1 = load i8** @vars_0
    call %IO* @IO_out_string(%IO* null, i8* %lvars_1)
    ret %Brocoli* %m
}


define i32 @main() {
    call %Program* @Program_main(%Program* null)
    ret i32 0
}


declare %Object* @Object_abort(%Object*)
declare i8* @Object_type_name(%Object*)
declare %IO* @IO_out_string(%IO*, i8*)
declare %IO* @IO_out_int(%IO*, i32 )
declare i8* @IO_in_string(%IO*)
declare i32 @IO_in_int(%IO*)
declare i32 @String_length(i8*)
declare i8* @String_concat(i8*, i8*)
declare i8* @String_substr(i8*, i32, i32 )
declare i32 @strcmp(i8*, i8*)
declare i8* @malloc(i64)
