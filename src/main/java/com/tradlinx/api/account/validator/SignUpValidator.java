package com.tradlinx.api.account.validator;

import com.tradlinx.api.account.AccountRepository;
import com.tradlinx.api.account.exception.SignUpException;
import com.tradlinx.api.account.form.SignUpDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class SignUpValidator implements Validator {

    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(SignUpDto.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SignUpDto signUpDto = (SignUpDto) target;
        if (errors.hasErrors()) {
            if (!StringUtils.hasText(signUpDto.getPw())) {
                throw new SignUpException("패스워드는 필수값 입니다.");
            }
            if (!StringUtils.hasText(signUpDto.getUsername())) {
                throw new SignUpException("사용자 이름은 필수값 입니다.");
            }
            throw new SignUpException("ID 를 잘못 입력했습니다. 다시 입력해주세요");
        }
        if (accountRepository.existsByUserId(signUpDto.getUserId())) {
            throw new SignUpException("이미 있는 ID 입니다. 다시 입력해주세요");
        }
    }
}
