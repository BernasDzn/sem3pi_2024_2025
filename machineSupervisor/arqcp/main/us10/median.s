.section .text
    .global median
    .extern sort_array
median:
    # vector -> %rdi
    # length -> %rsi
    # median -> %rdx

    cmpq $0, %rsi
    jle failed

    cmpq $1, %rsi
    je check_one

    pushq %rdi
    pushq %rdx
    
    movq $1, %rdx        
    call sort_array
    
    popq %rdx
    popq %rdi
        
    check_odd_or_even:
        testb $1, %sil
        jnz odd
        jz even

    odd:
        sar $1, %rsi           
        movl (%rdi, %rsi, 4), %r9d 
        movl %r9d, (%rdx)       
        movq $1, %rax
        ret

    even:
        sar $1, %rsi           
        movl (%rdi, %rsi, 4), %r9d  
        decq %rsi
        addl (%rdi, %rsi, 4), %r9d  
        sarl $1, %r9d              
        movl %r9d, (%rdx)          
        movq $1, %rax
        ret

    failed:
        movl $-1, (%rdx)       
        movq $0, %rax
        ret

    check_one:
        movl (%rdi), %r9d      
        movl %r9d, (%rdx)     
        movq $1, %rax
        ret