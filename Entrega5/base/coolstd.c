#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <string.h>

const char type_Object[] = "Object";
const char type_String[] = "String";
const char type_Int[] = "Int";
const char type_Bool[] = "Bool";
const char type_IO[] = "IO";

typedef struct Object {
    const char* type;
} Object;

typedef struct IO {
    const char* type;
} IO;

typedef struct String {
    const char* type;
    const char* data;
} String;

typedef struct Int {
    const char* type;
    int32_t data;
} Int;

typedef struct Bool {
    const char* type;
    _Bool data;
} Bool;

void error(const char* error) {
    puts(error);
    exit(-1);
}

Object* create_object() {
    Object result;
    result.type = type_Object;
    Object* o = malloc(sizeof(Object));
    memcpy(o, &result, sizeof(Object));
    return o;
}

Int* boxInt(int32_t value) {
    Int* r = malloc(sizeof(Int));
    r->type = type_Int;
    r->data = value;
    return r;
}

Bool* boxBool(_Bool value) {
    Bool* b = malloc(sizeof(Bool));
    b->type = type_Bool;
    b->data = value;
    return b;
}

String* boxString(const char* data) {
    String* result = malloc(sizeof(String));
    result->type = type_String;
    result->data = data;
    return result;
}



Object* Object_abort(Object* p) {
    exit(1);
    return p;
}

const char* Object_type_name(Object* p) {
    return p->type;
}

IO* IO_out_string(IO* io, const char* x) {
    puts(x);
    return io;
}

IO* IO_out_int(IO* io, int32_t x) {
    printf("%d", x);
    return io;
}

const char* IO_in_string(IO* io) {
    char* ptr = NULL;
    size_t size;
    getline(&ptr, &size, stdin);

    return ptr;
}

int32_t IO_in_int(IO* io) {
    char* ptr = NULL;
    size_t size;
    getline(&ptr, &size, stdin);
    
    int32_t value = (int32_t)strtol(ptr, NULL, 10);

    free(ptr);

    return value;
}

int32_t String_length(const char* s) {
    return (int32_t)strlen(s);    
}

const char* String_concat(const char* self, const char* s) {

    size_t selfLen = strlen(self);
    size_t sLen = strlen(s);

    char* result = malloc( selfLen + sLen + 1);
    memcpy(result, self, selfLen);
    memcpy(result + selfLen, s, sLen);
    result[selfLen + sLen] = '\0';

    return result;
}

const char* String_substr(const char* self, int32_t i,  int32_t l) {

    size_t selfLen = strlen(self);

    if(selfLen < (i + l)) {
        error("Index out of range");
    }

    char* result = malloc(l+1);
    memcpy(result, self+i, l);
    result[l] = '\0';

    return result;
}
