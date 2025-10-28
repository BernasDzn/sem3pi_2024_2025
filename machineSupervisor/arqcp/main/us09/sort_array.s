.section .data
.section .text
.global sort_array

sort_array:
    # Prologue
    pushq %rbp
    movq %rsp, %rbp

    movq $-1, %r10                  # Initialize %r10 to -1 (outer loop counter)

    movq $0, %r11                   # Initialize %r11 to 0 (inner loop counter)

    cmpl $0, %esi                   # Compare array size (%esi) with 0
    jle invalid                     # Jump to 'invalid' if array size is <= 0

    jmp outer_loop

outer_loop:
    addq $1, %r10

    movq %r10, %r11                 # copy outer loop counter to inner loop counter (%r11)

    cmpq %r10, %rsi                 # compare outer loop counter with array size (%esi)
    jg inner_loop                   # if outer loop counter < array size, continue to inner loop

    jmp done

inner_loop:
    addq $1, %r11

    cmpq %r11, %rsi                 # compare inner loop counter with array size
    je outer_loop                   # if inner loop counter reaches end, restart outer loop

    xor %rax, %rax                  # clear %rax register
    xor %r9, %r9                  # clear %r9 register
    movl (%rdi, %r10, 4), %eax      # move value at index %r10 into %eax
    movl (%rdi, %r11, 4), %r9d      # move value at index %r11 into %r9d

    cmpq $1, %rdx                 # check if sort order (%rdx) is 1 (ascending)
    je ascending                    # if so, jump to 'ascending'

    cmpq $0, %rdx                 # check if sort order (%rdx) is 0 (descending)
    je descending                   # if so, jump to 'descending'

ascending:
    cmpl %eax, %r9d                 # Compare values for ascending order
    jl switch                       # if value at %r10 < value at %r11, swap them
    
    jmp inner_loop

descending:
    cmpl %eax, %r9d                 # compare values for descending order
    jg switch                       # if value at %r10 > value at %r11, swap them
    
    jmp inner_loop

switch:
    movl %eax, (%rdi, %r11, 4)      # Swap value at index %r10 with 
    movl %r9d, (%rdi, %r10, 4)      # value at index %r11
    
    jmp inner_loop

invalid:
     # Epilogue
    movq %rbp, %rsp
    popq %rbp

    movq $0, %rax                    # Return 0 (invalid length)
    ret

done:
    # Epilogue
    movq %rbp, %rsp
    popq %rbp

    movq $1, %rax                    # Return 1 (successful sort)
    ret
