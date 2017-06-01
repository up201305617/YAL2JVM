.class public library1
.super java/lang/Object


.method public static min(II)I
.limit locals 3
.limit stack 5

iload_1
iload_0
swap
if_icmple if1_end
iload_1
istore_2
goto if1_next
if1_end:
iload_0
istore_2
if1_next:
iload_2
ireturn
.end method

.method public static max(II)I
.limit locals 3
.limit stack 5

iload_1
iload_0
swap
if_icmple if1_end
iload_0
istore_2
goto if1_next
if1_end:
iload_1
istore_2
if1_next:
iload_2
ireturn
.end method

.method static public <clinit>()V 
.limit stack 0
.limit locals 0
return
.end method
