.class public aval2
.super java/lang/Object


.method public static f(II)I
.limit locals 3
.limit stack 5

iload_1
iload_0
swap
if_icmpne if1_end
iconst_2
istore_2
goto if1_next
if1_end:
iconst_0
istore_2
if1_next:
iload_2
ireturn
.end method

.method public static main([Ljava/lang/String;)V
.limit locals 3
.limit stack 5
iconst_2
ldc 12
invokestatic aval2/f(II)I
istore_0
iload_0
invokestatic io/println(I)V
iconst_4
iconst_2
invokestatic aval2/f(II)I
istore_0
iload_0
invokestatic io/println(I)V
iconst_3
istore_0
iconst_4
iconst_2
invokestatic aval2/f(II)I
istore_0
iload_0
invokestatic io/println(I)V
iconst_5
iconst_5
invokestatic aval2/f(II)I
istore_1
iload_1
invokestatic io/println(I)V
return
.end method

.method static public <clinit>()V 
.limit stack 0
.limit locals 0
return
.end method
