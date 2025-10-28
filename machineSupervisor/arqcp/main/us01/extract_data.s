.section .text    
        .global extract_data
        
    extract_data:
        # str   ->  %rdi
        # token ->  %rsi
        # unit  ->  %rdx
        # value ->  %rcx

        check_token:
            movb (%rdi), %al        
            movb (%rsi), %bl        
            testb %bl, %bl          
            je check_str_token_end  
            cmpb %al,%bl
            jne next_token_pos      
            inc %rdi                
            inc %rsi                
            jmp check_token         

        check_str_token_end:
            cmpb $'&', %al
            je skip_and_save_unit 
            jmp next_token_pos 

        next_token_pos:             
            testb %al, %al          
            je not_found            
            inc %rdi                
            movb (%rdi), %al        
            cmpb $'#', %al          
            jne next_token_pos      
            inc %rdi                
            movb (%rdi), %al        
            jmp check_token         

        not_found:
            movb $0, (%rdx)         
            movl $0, (%rcx)         
            mov $0, %eax            
            ret
        
        skip_and_save_unit:
            add $6, %rdi            
            jmp save_unit
            
        save_unit:
            movb (%rdi), %al
            cmpb $'&', %al          
            je skip_and_save_value
            movb %al, (%rdx)        
            inc %rdx                
            inc %rdi                
            jmp save_unit

        skip_and_save_value:
            movb $0, (%rdx)         
            add $7, %rdi            
            xor %ebx, %ebx      
            jmp save_value

        save_value:
            xor %eax, %eax          
            movb (%rdi), %al        
            cmpb $'#', %al          
            je done                 
            testb %al, %al          
            je done                 
            imul $10, %ebx        
            sub $'0', %al           
            movzx %al, %eax        
            add %eax, %ebx         
            inc %rdi
            jmp save_value

        done:
            movl %ebx, (%rcx)     
            mov $1, %eax            
            ret                     