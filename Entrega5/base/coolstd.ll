; ModuleID = 'coolstd.c'
target datalayout = "e-p:64:64:64-i1:8:8-i8:8:8-i16:16:16-i32:32:32-i64:64:64-f32:32:32-f64:64:64-v64:64:64-v128:128:128-a0:0:64-s0:64:64-f80:128:128-n8:16:32:64-S128"
target triple = "x86_64-unknown-linux-gnu"

%struct._IO_FILE = type { i32, i8*, i8*, i8*, i8*, i8*, i8*, i8*, i8*, i8*, i8*, i8*, %struct._IO_marker*, %struct._IO_FILE*, i32, i32, i64, i16, i8, [1 x i8], i8*, i64, i8*, i8*, i8*, i8*, i64, i32, [20 x i8] }
%struct._IO_marker = type { %struct._IO_marker*, %struct._IO_FILE*, i32 }
%struct.Object = type { i8* }
%struct.Int = type { i8*, i32 }
%struct.Bool = type { i8*, i8 }
%struct.String = type { i8*, i8* }
%struct.IO = type { i8* }

@type_Object = constant [7 x i8] c"Object\00", align 1
@type_String = constant [7 x i8] c"String\00", align 1
@type_Int = constant [4 x i8] c"Int\00", align 1
@type_Bool = constant [5 x i8] c"Bool\00", align 1
@type_IO = constant [3 x i8] c"IO\00", align 1
@.str = private unnamed_addr constant [3 x i8] c"%d\00", align 1
@stdin = external global %struct._IO_FILE*
@.str1 = private unnamed_addr constant [19 x i8] c"Index out of range\00", align 1

define void @error(i8* nocapture %error) noreturn nounwind uwtable {
  %1 = tail call i32 @puts(i8* %error) nounwind
  tail call void @exit(i32 -1) noreturn nounwind
  unreachable
}

declare i32 @puts(i8* nocapture) nounwind

declare void @exit(i32) noreturn nounwind

define noalias %struct.Object* @create_object() nounwind uwtable {
  %1 = tail call noalias i8* @malloc(i64 8) nounwind
  %2 = bitcast i8* %1 to %struct.Object*
  %result.sroa.0.0..idx = bitcast i8* %1 to i8**
  store i8* getelementptr inbounds ([7 x i8]* @type_Object, i64 0, i64 0), i8** %result.sroa.0.0..idx, align 8
  ret %struct.Object* %2
}

declare noalias i8* @malloc(i64) nounwind

declare void @llvm.memcpy.p0i8.p0i8.i64(i8* nocapture, i8* nocapture, i64, i32, i1) nounwind

define noalias %struct.Int* @boxInt(i32 %value) nounwind uwtable {
  %1 = tail call noalias i8* @malloc(i64 16) nounwind
  %2 = bitcast i8* %1 to %struct.Int*
  %3 = bitcast i8* %1 to i8**
  store i8* getelementptr inbounds ([4 x i8]* @type_Int, i64 0, i64 0), i8** %3, align 8, !tbaa !0
  %4 = getelementptr inbounds i8* %1, i64 8
  %5 = bitcast i8* %4 to i32*
  store i32 %value, i32* %5, align 4, !tbaa !3
  ret %struct.Int* %2
}

define noalias %struct.Bool* @boxBool(i1 zeroext %value) nounwind uwtable {
  %1 = tail call noalias i8* @malloc(i64 16) nounwind
  %2 = bitcast i8* %1 to %struct.Bool*
  %3 = bitcast i8* %1 to i8**
  store i8* getelementptr inbounds ([5 x i8]* @type_Bool, i64 0, i64 0), i8** %3, align 8, !tbaa !0
  %4 = getelementptr inbounds i8* %1, i64 8
  %5 = zext i1 %value to i8
  store i8 %5, i8* %4, align 1, !tbaa !4
  ret %struct.Bool* %2
}

define noalias %struct.String* @boxString(i8* %data) nounwind uwtable {
  %1 = tail call noalias i8* @malloc(i64 16) nounwind
  %2 = bitcast i8* %1 to %struct.String*
  %3 = bitcast i8* %1 to i8**
  store i8* getelementptr inbounds ([7 x i8]* @type_String, i64 0, i64 0), i8** %3, align 8, !tbaa !0
  %4 = getelementptr inbounds i8* %1, i64 8
  %5 = bitcast i8* %4 to i8**
  store i8* %data, i8** %5, align 8, !tbaa !0
  ret %struct.String* %2
}

define noalias %struct.Object* @Object_abort(%struct.Object* nocapture %p) noreturn nounwind uwtable {
  tail call void @exit(i32 1) noreturn nounwind
  unreachable
}

define i8* @Object_type_name(%struct.Object* nocapture %p) nounwind uwtable readonly {
  %1 = getelementptr inbounds %struct.Object* %p, i64 0, i32 0
  %2 = load i8** %1, align 8, !tbaa !0
  ret i8* %2
}

define %struct.IO* @IO_out_string(%struct.IO* %io, i8* nocapture %x) nounwind uwtable {
  %1 = tail call i32 @puts(i8* %x) nounwind
  ret %struct.IO* %io
}

define %struct.IO* @IO_out_int(%struct.IO* %io, i32 %x) nounwind uwtable {
  %1 = tail call i32 (i8*, ...)* @printf(i8* getelementptr inbounds ([3 x i8]* @.str, i64 0, i64 0), i32 %x) nounwind
  ret %struct.IO* %io
}

declare i32 @printf(i8* nocapture, ...) nounwind

define i8* @IO_in_string(%struct.IO* nocapture %io) nounwind uwtable {
  %ptr = alloca i8*, align 8
  %size = alloca i64, align 8
  store i8* null, i8** %ptr, align 8, !tbaa !0
  %1 = load %struct._IO_FILE** @stdin, align 8, !tbaa !0
  %2 = call i64 @getline(i8** %ptr, i64* %size, %struct._IO_FILE* %1) nounwind
  %3 = load i8** %ptr, align 8, !tbaa !0
  ret i8* %3
}

declare i64 @getline(i8**, i64*, %struct._IO_FILE*)

define i32 @IO_in_int(%struct.IO* nocapture %io) nounwind uwtable {
  %ptr = alloca i8*, align 8
  %size = alloca i64, align 8
  store i8* null, i8** %ptr, align 8, !tbaa !0
  %1 = load %struct._IO_FILE** @stdin, align 8, !tbaa !0
  %2 = call i64 @getline(i8** %ptr, i64* %size, %struct._IO_FILE* %1) nounwind
  %3 = load i8** %ptr, align 8, !tbaa !0
  %4 = call i64 @strtol(i8* nocapture %3, i8** null, i32 10) nounwind
  %5 = trunc i64 %4 to i32
  %6 = load i8** %ptr, align 8, !tbaa !0
  call void @free(i8* %6) nounwind
  ret i32 %5
}

declare i64 @strtol(i8*, i8** nocapture, i32) nounwind

declare void @free(i8* nocapture) nounwind

define i32 @String_length(i8* nocapture %s) nounwind uwtable readonly {
  %1 = tail call i64 @strlen(i8* %s) nounwind readonly
  %2 = trunc i64 %1 to i32
  ret i32 %2
}

declare i64 @strlen(i8* nocapture) nounwind readonly

define noalias i8* @String_concat(i8* nocapture %self, i8* nocapture %s) nounwind uwtable {
  %1 = tail call i64 @strlen(i8* %self) nounwind readonly
  %2 = tail call i64 @strlen(i8* %s) nounwind readonly
  %3 = add i64 %2, %1
  %4 = add i64 %3, 1
  %5 = tail call noalias i8* @malloc(i64 %4) nounwind
  tail call void @llvm.memcpy.p0i8.p0i8.i64(i8* %5, i8* %self, i64 %1, i32 1, i1 false)
  %6 = getelementptr inbounds i8* %5, i64 %1
  tail call void @llvm.memcpy.p0i8.p0i8.i64(i8* %6, i8* %s, i64 %2, i32 1, i1 false)
  %7 = getelementptr inbounds i8* %5, i64 %3
  store i8 0, i8* %7, align 1, !tbaa !1
  ret i8* %5
}

define noalias i8* @String_substr(i8* nocapture %self, i64 %i, i64 %l) nounwind {
  %1 = tail call i64 @strlen(i8* %self) nounwind readonly
  %2 = add nsw i64 %l, %i
  %3 = icmp ult i64 %1, %2
  br i1 %3, label %4, label %5

; <label>:4                                       ; preds = %0
  tail call void @error(i8* getelementptr inbounds ([19 x i8]* @.str1, i32 0, i32 0)) nounwind
  br label %5

; <label>:5                                       ; preds = %4, %0
  %6 = add nsw i64 %l, 1
  %7 = tail call noalias i8* @malloc(i64 %6) nounwind
  %8 = getelementptr inbounds i8* %self, i64 %i
  tail call void @llvm.memcpy.p0i8.p0i8.i64(i8* %7, i8* %8, i64 %l, i32 1, i1 false)
  %9 = getelementptr inbounds i8* %7, i64 %l
  store i8 0, i8* %9, align 1, !tbaa !0
  ret i8* %7
}

!0 = metadata !{metadata !"any pointer", metadata !1}
!1 = metadata !{metadata !"omnipotent char", metadata !2}
!2 = metadata !{metadata !"Simple C/C++ TBAA"}
!3 = metadata !{metadata !"int", metadata !1}
!4 = metadata !{metadata !"_Bool", metadata !1}
