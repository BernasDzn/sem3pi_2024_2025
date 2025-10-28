.section .text
.global move_n_to_array

move_n_to_array:
    # %rdi -> int* buffer
    # %rsi -> int length
    # %rdx -> int *tail (read pointer)
    # %rcx -> int *head (write pointer)
    # %r8  -> int n
    # %r9  -> int* array
    
    movl $0, %eax            # Initialize return value to 0

    cmpl $1, %r8d           # Compare n with 1
    jl end_program          # If n < 1, return 0

    # Check if buffer is empty (tail == head)
    movl (%rdx), %r10d      # Load tail value
    movl (%rcx), %r11d      # Load head value
    cmpl %r10d, %r11d       # Compare head and tail
    je end_program          # If equal, buffer is empty, return 0

copy_elements:
    movl (%rdx), %r10d      # Load tail value into r10d
    cmpl %esi, %r10d        # Compare tail with length
    jl continue_tail        # If tail < length, continue
    movl $0, %r10d          # Reset tail to 0
    movl %r10d, (%rdx)      # Store new tail value

continue_tail:
    movslq %r10d, %r10      # Convert 32-bit index to 64-bit for addressing
    movl (%rdi,%r10,4), %r11d  # Load buffer[tail] into r11d
    movl %r11d, (%r9)         # Store value in array
    addq $4, %r9              # Move array pointer to next position
    
    movl (%rdx), %r10d        # Reload tail value
    incl %r10d                # Increment tail
    movl %r10d, (%rdx)       # Store new tail value
    
    decl %r8d                 # Decrement n
    cmpl $0, %r8d            # Check if n == 0
    jne copy_elements        # If n != 0, continue copying
    
    movl $1, %eax            # Set return value to 1

end_program:
    ret