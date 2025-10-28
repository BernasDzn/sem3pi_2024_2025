.section .text    
    .global enqueue_value
    
enqueue_value:
    # *buffer   ->  %rdi
    # length    ->  %rsi
    # *tail     ->  %rdx
    # *head     ->  %rcx
    # value     ->  %r8d

    # Store value at head position
    movl (%rcx), %eax               # Load head position
    movslq %eax, %rax               # Convert to quad for indexing
    movl %r8d, (%rdi, %rax, 4)      # Store value at buffer[head]
    
    # Check if head is at end of buffer
    incl (%rcx)                     # Increment head
    cmpl %esi, (%rcx)               # Compare head with length
    jne check_full                  # If not at end, check if buffer full
    movl $0, (%rcx)                 # If at end, wrap head to 0

check_full:
    movl (%rcx), %eax               # Load new head position
    movl (%rdx), %r10d              # Load tail position
    cmpl %eax, %r10d                # Compare tail with head
    jne not_full                    # If not equal, buffer not full
    
    # Buffer is full, wrap tail
    incl (%rdx)                     # Increment tail
    cmpl %esi, (%rdx)               # Compare with length
    jne return_full                 # If not at end, return 1
    movl $0, (%rdx)                 # If at end, wrap tail to 0

return_full:
    movl $1, %eax                   # Return 1 (buffer full)
    ret

not_full:
    movl $0, %eax                   # Return 0 (buffer not full)
    ret