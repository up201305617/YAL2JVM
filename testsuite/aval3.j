.class public aval3
.super java/lang/Object


.method public static f(II)I
.limit locals 3
.limit stack 5

iload_1
iload_0
swap
if_icmplt if1_end
iconst_2
istore_2
goto if1_next
if1_end:
iconst_4
istore_2
if1_next:
iload_2
ireturn
.end method

.method public static main([Ljava/lang/String;)V
.limit locals 3
.limit stack 5
iconst_2
istore_0
iconst_3
istore_1
iload_0
iload_1
invokestatic aval3/f(II)I
istore_0
iload_0
invokestatic io/println(I)V
ldc 6
istore_0
iload_0
iload_1
invokestatic aval3/f(II)I
istore_0
iload_0
invokestatic io/println(I)V
return
.end method

.method static public <clinit>()V 
.limit stack 0
.limit locals 0
return
.end method
