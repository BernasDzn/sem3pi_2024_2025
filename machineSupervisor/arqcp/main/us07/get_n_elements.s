.section .data
.global n

.section .text
.global get_n_elements

get_n_elements:
	movl (%rdx), %ebx       #tail
    movl (%rcx), %eax       #head
    movl %esi, %ecx         #length

    cmp %ebx, %eax          #check if tail == head
    je end_loop

    cmp %ebx, %eax          #check if tail<head
    jle tail_larger

    subl %ebx, %eax            # tail - head
    ret

tail_larger:
    subl %ebx, %ecx            # length - tail
    addl %eax, %ecx            # length + head
    movl %ecx, %eax            #result to return value
    ret

end_loop:
	movl $0, %eax
	ret