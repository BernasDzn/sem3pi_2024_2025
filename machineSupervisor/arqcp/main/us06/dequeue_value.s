.section .text    
    .global dequeue_value
    
dequeue_value:
    # *buffer   ->  %rdi
    # length    ->  %rsi
    # *tail     ->  %rdx
    # *head     ->  %rcx
    # *value    ->  %r8

    # First check if buffer is empty (tail == head)
    movl (%rdx), %ebx              # load tail value
    cmpl %ebx, (%rcx)              # compare tail with head
    je return_fail                 # if equal, buffer is empty

    # Calculate position of value to dequeue
    movq %rdi, %r10                # preserve buffer base address
    movl (%rdx), %ebx              # load tail index
    movslq %ebx, %rbx              # sign extend to 64-bit for multiplication
    movq $4, %r9                   # multiply by 4 (size of int)
    imulq %r9, %rbx                # offset = tail * 4
    addq %rbx, %r10                # buffer position = base + offset

    # Get value 
    movl (%r10), %ebx              # load value at tail position
    testl %ebx, %ebx               # test if value is zero
    jz return_fail                 # if zero, buffer position is empty
    movl %ebx, (%r8)               # store into *value

    # Update tail position
    movl (%rdx), %ebx              # load current tail
    addl $1, %ebx                  # increment tail
    cmpl %ebx, %esi                # compare with length (32-bit)
    jne store_tail                 # if not equal, store new tail
    xorl %ebx, %ebx                # if equal, reset to 0

store_tail:
    movl %ebx, (%rdx)              # store new tail value
    movl $1, %eax                  # return success
    ret

return_fail:
    movl $0, %eax                  # return failure
    movl $0, (%r8)                # clear output value
    ret