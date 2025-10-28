.section .data
.section .text
.global get_number

get_number:
    # Prologue
    pushq %rbp
    movq %rsp, %rbp

    movl $0, %eax            
    xor %rcx, %rcx              # Clear %rcx
    xor %rdx, %rdx              # Clear %rdx
    cmpb $0, (%rdi)
    je invalid

next_char:
    movb (%rdi), %dl            # load the current character from the memory address in %rdi into %dl
    cmpb $0, %dl                # check if the current character is the null terminator (end of string)
    je done                     # if it is, jump to 'done'
    
    cmpb $' ', %dl
    je .skip

    cmpb $'0', %dl              # check if the character is less than '0'
    jb invalid                  # jump to 'invalid' (indicating a non-numeric character)

    cmpb $'9', %dl              # check if the character is greater than '9'
    ja invalid                  # jump to 'invalid' (indicating a non-numeric character)


    subb $'0', %dl              # Convert the ASCII character in %dl to its numeric value by subtracting ASCII '0'
    imulq $10, %rcx             # Multiply the accumulated number in %rcx by 10 to shift it for the next digit
    addq %rdx, %rcx             # Add the current digit to %rcx

    .skip:
    inc %rdi

    jmp next_char

invalid:
    # Epilogue
    movq %rbp, %rsp
    popq %rbp

    movq $0, %rax               # Return 0 and -1 (indicating failure)
    movq $-1, (%rsi)
    ret

done:
    # Epilogue
    movq %rbp, %rsp
    popq %rbp

    movq %rcx, (%rsi)           # Store the parsed number from %rcx into the memory location pointed to by %rsi
    movq $1, %rax               # Return 1 (successful parsing)
    ret