.section .text
    .global format_command
    .extern get_number_binary
format_command:
    # str -> %rdi
    # value -> %rsi
    # char cmd[] -> %rdx
    
    # validCharacterCounter -> %r9

    # Epilogue
    pushq %rbp
    movq %rsp, %rbp

    movq $0, %r9
    movq $0, %rax

    cmpb $0, (%rdi)
    je abort

    loop_string_characters:

        jmp check_space
        .resume:
        inc %rdi
        cmpb $0, (%rdi)
        jne loop_string_characters

    end:
        cmpq $2, %r9
        jl abort
        movq %rsi, %rdi # int value -> %rdi
        movq %rdx, %rsi # char bits[] -> %rsi
        movb $' ', (%rsi)
        inc %rsi
        movq %rdx, %r8
        inc %r8 # points to first position of binary number
        call get_number_binary
        call flip_string
        call convert_vec_to_string

        movq $1 , %rax

        # Epilogue
        movq %rbp, %rsp
        popq %rbp
        ret

    abort:
        subq %r9, %rdx
        movb $0, (%rdx)
        movq $0, %rax
        # Epilogue
        movq %rbp, %rsp
        popq %rbp
        ret

    flip_string:
        # this function will switch binary positions 5 with 1, and 4 with 2
        # buffer -> %r10
        # buffer2 -> %r11
        # index -> %r8
        movq %r8, %rdx
        movq $4, %r8
        movb (%rdx), %r10b
        movb (%rdx, %r8, 1), %r11b
        movb %r10b, (%rdx, %r8, 1)
        movb %r11b, (%rdx)

        movq $1, %r8
        movb (%rdx, %r8, 1), %r10b
        movq $3, %r8
        movb (%rdx, %r8, 1), %r11b
        movb %r10b, (%rdx, %r8, 1)
        movq $1, %r8
        movb %r11b, (%rdx, %r8, 1)
        ret

    convert_vec_to_string:
        movq $0, %r8
        .loop_string:
            addb $48, (%rdx, %r8, 1)
            inc %r8
            cmpq $5, %r8
            jne .loop_string
            ret

    separate_characters:
        movq %rdx, %r11 
        movq %r11, %r10
        addq $4, %r10
        movq $5, %rsi
        .loop:
            movb (%r10), %cl
            shl %rsi
            movb %cl, (%r10, %rsi, 1)
            movq %r10, %rdi
            addq %rsi, %rdi
            dec %rdi
            .loop2:
                movq $',', (%rdi)
                dec %rdi
                cmpq %rdi, %r10
                jne .loop2
                je .resume2
            .resume2:
            shr %rsi
            dec %r10
            dec %rsi
            cmpq %r10, %r11
            jne .loop
            ret

    check_space:
        cmpb $32, (%rdi)
        je .resume
        jne check_o_capital

    check_o_capital:
        cmpb $111, (%rdi)
        je done_check_o
        jne check_o_small

    check_o_small:
        cmpb $79, (%rdi)
        je done_check_o
        jne check_p_capital

    done_check_o:
        cmpb $0, %r9b 
        jne abort
        movq $79, (%rdx)
        addq $1, %rdx
        addq $1, %r9 
        jmp .resume

    check_p_capital:
        cmpb $80, (%rdi)
        je done_check_p
        jne check_p_small

    check_p_small:
        cmpb $112, (%rdi)
        je done_check_p
        jne check_n_capital

    done_check_p:
        cmpb $1, %r9b 
        jne abort
        movq $80, (%rdx)
        addq $1, %rdx
        addq $1, %r9 
        jmp .resume

    check_n_capital:
        cmpb $78, (%rdi)
        je done_check_n
        jne check_n_small

    check_n_small:
        cmpb $110, (%rdi)
        je done_check_n
        jne check_f_capital

    done_check_n:
        cmpb $1, %r9b 
        jne abort
        movq $78, (%rdx)
        addq $1, %rdx
        addq $1, %r9 
        jmp .resume

    check_f_capital:
        cmpb $70, (%rdi)
        je done_check_f
        jne check_f_small

    check_f_small:
        cmpb $102, (%rdi)
        je done_check_f
        jne abort
        
    done_check_f:
        cmpb $2, %r9b
        jg abort
        movq $70, (%rdx)
        addq $1, %rdx
        addq $1, %r9 
        jmp .resume