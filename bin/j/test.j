.class public test
.super java/lang/Object

.field static a I = 1
.field static data [I

.method public static main([Ljava/lang/String;)V
.limit locals 1
.limit stack 1
return
.end method

.method public static testing()[I
.limit locals 2
.limit stack 2

aload_1
areturn
.end method

.method public static aux(I)I
.limit locals 2
.limit stack 2

iload_2
ireturn
.end method

.method public static f(II[I)I
.limit locals 5
.limit stack 2

iload 4
ireturn
.end method

