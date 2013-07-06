@true = global i1 1
@false = global i1 0
@zero = global i32 0
@vari_0 = global i32 0
@vari_1 = global i32 1
@vari_2 = global i32 2
@vari_3 = global i32 5
@vars_0_c = constant [1 x i8] c"\0A"
@vars_0 = global i8* bitcast([1 x i8]* @vars_0_c to i8*)
@vari_4 = global i32 30
@vari_5 = global i32 156
@vari_6 = global i32 3

%Object = type { i8* }
%IO = type { i8* }

%Main= type { i8* }

define i32 @main() {
    call %Main* @Main_main(%Main* null)
    ret i32 0
}

define i32 @Main_pow(%Main* %m, i32 %base, i32 %exp) {
    %_tmp_1 = bitcast %Main* %m to %IO*
    %lvari_1 = alloca i32
    %lvari_2 = alloca i32
    %lvari_3 = add i32 %exp, 0
store i32 %lvari_3, i32* %lvari_1
    %lvari_4 = load i32* @vari_0
store i32 %lvari_4, i32* %lvari_2
    %lvari_5 = load i32* %lvari_1
    %lvari_6 = load i32* %lvari_2
    %lvarb_1 = icmp eq i32 %lvari_5, %lvari_6

    %lvari_7 = alloca i32
    br i1 %lvarb_1, label %then1, label %else1
then1:
    %lvari_8 = load i32* @vari_1
    store i32 %lvari_8, i32* %lvari_7
    br label %ifcont1
else1:
    %lvari_9 = alloca i32
    %lvari_10 = alloca i32
    %lvari_11 = add i32 %base, 0
store i32 %lvari_11, i32* %lvari_9
    %lvari_12 = add i32 %base, 0
    %lvari_13 = alloca i32
    %lvari_14 = alloca i32
    %lvari_15 = add i32 %exp, 0
store i32 %lvari_15, i32* %lvari_13
    %lvari_16 = load i32* @vari_1
store i32 %lvari_16, i32* %lvari_14
    %lvari_17 = load i32* %lvari_13
    %lvari_18 = load i32* %lvari_14
    %lvari_19 = sub i32 %lvari_17, %lvari_18
    %lvari_20 = call i32 @Main_pow(%Main* null, i32 %lvari_12, i32 %lvari_19)
store i32 %lvari_20, i32* %lvari_10
    %lvari_21 = load i32* %lvari_9
    %lvari_22 = load i32* %lvari_10
    %lvari_23 = mul i32 %lvari_21, %lvari_22
    store i32 %lvari_23, i32* %lvari_7
    br label %ifcont1
ifcont1:

    %lvari_24 = load i32* %lvari_7
    ret i32 %lvari_24
}

define %Main* @Main_main(%Main* %m) {
    %_tmp_1 = bitcast %Main* %m to %IO*
    %lvari_25 = load i32* @vari_2
    %lvari_26 = load i32* @vari_3
    %lvari_27 = call i32 @Main_pow(%Main* null, i32 %lvari_25, i32 %lvari_26)
    call %IO* @IO_out_int(%IO* %_tmp_1, i32 %lvari_27)
    %lvars_1 = load i8** @vars_0
    call %IO* @IO_out_string(%IO* %_tmp_1, i8* %lvars_1)
    %lvari_28 = load i32* @vari_1
    %lvari_29 = load i32* @vari_4
    %lvari_30 = call i32 @Main_pow(%Main* null, i32 %lvari_28, i32 %lvari_29)
    call %IO* @IO_out_int(%IO* %_tmp_1, i32 %lvari_30)
    %lvars_2 = load i8** @vars_0
    call %IO* @IO_out_string(%IO* %_tmp_1, i8* %lvars_2)
    %lvari_31 = load i32* @vari_5
    %lvari_32 = load i32* @vari_6
    %lvari_33 = call i32 @Main_pow(%Main* null, i32 %lvari_31, i32 %lvari_32)
    call %IO* @IO_out_int(%IO* %_tmp_1, i32 %lvari_33)
    ret %Main* %m
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
