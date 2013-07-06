@true = global i1 1
@false = global i1 0
@zero = global i32 0
@digits_c = constant [10 x i8] c"0123456789"
@digits = global i8* bitcast([10 x i8]* @digits_c to i8*)
@vari_0 = global i32 1898524
@vars_0_c = constant [1 x i8] c"\0A"
@vars_0 = global i8* bitcast([1 x i8]* @vars_0_c to i8*)
@vari_1 = global i32 123
@vari_2 = global i32 11
@vari_3 = global i32 126
@vari_4 = global i32 2
@vari_5 = global i32 10
@vari_6 = global i32 1
@vari_7 = global i32 0
@vars_1_c = constant [1 x i8] c"0"
@vars_1 = global i8* bitcast([1 x i8]* @vars_1_c to i8*)
@vars_2_c = constant [1 x i8] c"1"
@vars_2 = global i8* bitcast([1 x i8]* @vars_2_c to i8*)

%Object = type { i8* }
%IO = type { i8* }

%Main= type { i8* }

define i32 @main() {
    call %Main* @Main_main(%Main* null)
    ret i32 0
}

define %Main* @Main_main(%Main* %m) {
    %_tmp_1 = bitcast %Main* %m to %IO*
    %lvari_1 = load i32* @vari_0
    %lvarb_1 = load i1* @false
    %lvars_1 = call i8* @Main_itoa(%Main* null, i32 %lvari_1, i1 %lvarb_1)
    call %IO* @IO_out_string(%IO* %_tmp_1, i8* %lvars_1)
    %lvars_2 = load i8** @vars_0
    call %IO* @IO_out_string(%IO* %_tmp_1, i8* %lvars_2)
    %lvari_2 = alloca i32
    %lvari_3 = alloca i32
    %lvari_4 = load i32* @vari_1
store i32 %lvari_4, i32* %lvari_2
    %lvari_5 = load i32* @vari_2
store i32 %lvari_5, i32* %lvari_3
    %lvari_6 = load i32* %lvari_2
    %lvari_7 = load i32* %lvari_3
    %lvari_8 = sub i32 %lvari_6, %lvari_7
    %lvarb_2 = load i1* @false
    %lvars_3 = call i8* @Main_itoa(%Main* null, i32 %lvari_8, i1 %lvarb_2)
    call %IO* @IO_out_string(%IO* %_tmp_1, i8* %lvars_3)
    %lvars_4 = load i8** @vars_0
    call %IO* @IO_out_string(%IO* %_tmp_1, i8* %lvars_4)
    %lvari_9 = load i32* @vari_3
    %lvarb_3 = load i1* @true
    %lvars_5 = call i8* @Main_itoa(%Main* null, i32 %lvari_9, i1 %lvarb_3)
    call %IO* @IO_out_string(%IO* %_tmp_1, i8* %lvars_5)
    %lvars_6 = load i8** @vars_0
    call %IO* @IO_out_string(%IO* %_tmp_1, i8* %lvars_6)
    %lvari_10 = alloca i32
    %lvari_11 = alloca i32
    %lvari_12 = load i32* @vari_4
store i32 %lvari_12, i32* %lvari_10
    %lvari_13 = load i32* @vari_5
store i32 %lvari_13, i32* %lvari_11
    %lvari_14 = load i32* %lvari_10
    %lvari_15 = load i32* %lvari_11
    %lvari_16 = mul i32 %lvari_14, %lvari_15
    %lvarb_4 = load i1* @true
    %lvars_7 = call i8* @Main_itoa(%Main* null, i32 %lvari_16, i1 %lvarb_4)
    call %IO* @IO_out_string(%IO* %_tmp_1, i8* %lvars_7)
    ret %Main* %m
}

define i8* @Main_itoa(%Main* %m, i32 %number, i1 %binary) {
    %_tmp_1 = bitcast %Main* %m to %IO*
    %lvarb_6 = icmp eq i1 %binary, 1

    %lvars_8 = alloca i8*
    br i1 %lvarb_6, label %then1, label %else1
then1:
    %lvari_17 = add i32 %number, 0
    %lvars_9 = call i8* @Main_itoa_binary(%Main* null, i32 %lvari_17)
    store i8* %lvars_9, i8** %lvars_8
    br label %ifcont1
else1:
    %lvari_18 = add i32 %number, 0
    %lvars_10 = call i8* @Main_itoa_decimal(%Main* null, i32 %lvari_18)
    store i8* %lvars_10, i8** %lvars_8
    br label %ifcont1
ifcont1:

    %lvars_11 = load i8** %lvars_8
    ret i8* %lvars_11
}

define i8* @Main_itoa_decimal(%Main* %m, i32 %number) {
    %_tmp_1 = bitcast %Main* %m to %IO*
    %lvari_19 = alloca i32
    %lvari_20 = alloca i32
    %lvari_21 = add i32 %number, 0
store i32 %lvari_21, i32* %lvari_19
    %lvari_22 = load i32* @vari_5
store i32 %lvari_22, i32* %lvari_20
    %lvari_23 = load i32* %lvari_19
    %lvari_24 = load i32* %lvari_20
    %lvarb_7 = icmp slt i32 %lvari_23, %lvari_24

    %lvars_12 = alloca i8*
    br i1 %lvarb_7, label %then2, label %else2
then2:
        %lvars_13 = load i8** @digits
    %lvari_25 = add i32 %number, 0
    %lvari_26 = load i32* @vari_6
    %lvars_14 = call i8* @String_substr(i8* %lvars_13, i32 %lvari_25, i32 %lvari_26)
    store i8* %lvars_14, i8** %lvars_12
    br label %ifcont2
else2:
    %lvari_27 = alloca i32
    %lvari_28 = alloca i32
    %lvari_29 = add i32 %number, 0
store i32 %lvari_29, i32* %lvari_27
    %lvari_30 = load i32* @vari_5
store i32 %lvari_30, i32* %lvari_28
    %lvari_31 = load i32* %lvari_27
    %lvari_32 = load i32* %lvari_28
    %lvari_33 = sdiv i32 %lvari_31, %lvari_32
    %lvars_15 = call i8* @Main_itoa_decimal(%Main* null, i32 %lvari_33)
    %lvari_34 = alloca i32
    %lvari_35 = alloca i32
    %lvari_36 = add i32 %number, 0
store i32 %lvari_36, i32* %lvari_34
    %lvari_37 = alloca i32
    %lvari_38 = alloca i32
    %lvari_39 = alloca i32
    %lvari_40 = alloca i32
    %lvari_41 = add i32 %number, 0
store i32 %lvari_41, i32* %lvari_39
    %lvari_42 = load i32* @vari_5
store i32 %lvari_42, i32* %lvari_40
    %lvari_43 = load i32* %lvari_39
    %lvari_44 = load i32* %lvari_40
    %lvari_45 = sdiv i32 %lvari_43, %lvari_44
store i32 %lvari_45, i32* %lvari_37
    %lvari_46 = load i32* @vari_5
store i32 %lvari_46, i32* %lvari_38
    %lvari_47 = load i32* %lvari_37
    %lvari_48 = load i32* %lvari_38
    %lvari_49 = mul i32 %lvari_47, %lvari_48
store i32 %lvari_49, i32* %lvari_35
    %lvari_50 = load i32* %lvari_34
    %lvari_51 = load i32* %lvari_35
    %lvari_52 = sub i32 %lvari_50, %lvari_51
    %lvars_16 = call i8* @Main_itoa_decimal(%Main* null, i32 %lvari_52)
    %lvars_17 = call i8* @String_concat(i8* %lvars_15, i8* %lvars_16)
    store i8* %lvars_17, i8** %lvars_12
    br label %ifcont2
ifcont2:

    %lvars_18 = load i8** %lvars_12
    ret i8* %lvars_18
}

define i8* @Main_itoa_binary(%Main* %m, i32 %n) {
    %_tmp_1 = bitcast %Main* %m to %IO*
    %lvari_53 = alloca i32
    %lvari_54 = alloca i32
    %lvari_55 = add i32 %n, 0
store i32 %lvari_55, i32* %lvari_53
    %lvari_56 = load i32* @vari_4
store i32 %lvari_56, i32* %lvari_54
    %lvari_57 = load i32* %lvari_53
    %lvari_58 = load i32* %lvari_54
    %lvarb_8 = icmp slt i32 %lvari_57, %lvari_58

    %lvars_19 = alloca i8*
    br i1 %lvarb_8, label %then3, label %else3
then3:
    %lvari_59 = alloca i32
    %lvari_60 = alloca i32
    %lvari_61 = add i32 %n, 0
store i32 %lvari_61, i32* %lvari_59
    %lvari_62 = load i32* @vari_7
store i32 %lvari_62, i32* %lvari_60
    %lvari_63 = load i32* %lvari_59
    %lvari_64 = load i32* %lvari_60
    %lvarb_9 = icmp eq i32 %lvari_63, %lvari_64

    %lvars_20 = alloca i8*
    br i1 %lvarb_9, label %then4, label %else4
then4:
    %lvars_21 = load i8** @vars_1
    store i8* %lvars_21, i8** %lvars_20
    br label %ifcont4
else4:
    %lvars_22 = load i8** @vars_2
    store i8* %lvars_22, i8** %lvars_20
    br label %ifcont4
ifcont4:

    %lvars_23 = load i8** %lvars_20
    store i8* %lvars_23, i8** %lvars_19
    br label %ifcont3
else3:
    %lvari_65 = alloca i32
    %lvari_66 = alloca i32
    %lvari_67 = add i32 %n, 0
store i32 %lvari_67, i32* %lvari_65
    %lvari_68 = load i32* @vari_4
store i32 %lvari_68, i32* %lvari_66
    %lvari_69 = load i32* %lvari_65
    %lvari_70 = load i32* %lvari_66
    %lvari_71 = sdiv i32 %lvari_69, %lvari_70
    %lvars_24 = call i8* @Main_itoa_binary(%Main* null, i32 %lvari_71)
    %lvari_72 = alloca i32
    %lvari_73 = alloca i32
    %lvari_74 = add i32 %n, 0
store i32 %lvari_74, i32* %lvari_72
    %lvari_75 = alloca i32
    %lvari_76 = alloca i32
    %lvari_77 = alloca i32
    %lvari_78 = alloca i32
    %lvari_79 = add i32 %n, 0
store i32 %lvari_79, i32* %lvari_77
    %lvari_80 = load i32* @vari_4
store i32 %lvari_80, i32* %lvari_78
    %lvari_81 = load i32* %lvari_77
    %lvari_82 = load i32* %lvari_78
    %lvari_83 = sdiv i32 %lvari_81, %lvari_82
store i32 %lvari_83, i32* %lvari_75
    %lvari_84 = load i32* @vari_4
store i32 %lvari_84, i32* %lvari_76
    %lvari_85 = load i32* %lvari_75
    %lvari_86 = load i32* %lvari_76
    %lvari_87 = mul i32 %lvari_85, %lvari_86
store i32 %lvari_87, i32* %lvari_73
    %lvari_88 = load i32* %lvari_72
    %lvari_89 = load i32* %lvari_73
    %lvari_90 = sub i32 %lvari_88, %lvari_89
    %lvars_25 = call i8* @Main_itoa_binary(%Main* null, i32 %lvari_90)
    %lvars_26 = call i8* @String_concat(i8* %lvars_24, i8* %lvars_25)
    store i8* %lvars_26, i8** %lvars_19
    br label %ifcont3
ifcont3:

    %lvars_27 = load i8** %lvars_19
    ret i8* %lvars_27
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
