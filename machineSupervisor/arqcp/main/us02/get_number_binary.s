.section .text
    .global get_number_binary

get_number_binary:
    # int value -> %rdi
    # char bits[] -> %rsi
    
    # Check if number is negative
    testl %edi, %edi
    js end_program      # if negative, return 0
    
    # Check if number >= 32
    cmpl $32, %edi
    jns end_program     # if >= 32, return 0

    movl $5, %edx       # loop counter 5

convert_loop:
    sar $1, %rdi       # shift right
    jc set_bit         # if carry is 1
    movb $0, (%rsi)    # if not, set next value in bit array 0
    jmp next_bit

set_bit:
    movb $1, (%rsi)    # set next value in bit array 1

next_bit:
    inc %rsi           # increment bit array
    dec %edx           # decrement loop counter
    jne convert_loop   # if loop counter is not 0

    movl $1, %eax      # return 1 for success
    ret

end_program:
    movl $0, %eax      # return 0 for invalid input
    ret