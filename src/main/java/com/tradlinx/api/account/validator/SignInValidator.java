package com.tradlinx.api.account.validator;

import com.tradlinx.api.account.AccountRepository;
import com.tradlinx.api.account.exception.AccountException;
import com.tradlinx.api.account.form.SignInDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class SignInValidator implements Validator {

    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(SignInDto.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SignInDto signInDto = (SignInDto) target;
        if (errors.hasErrors()) {
            if (!StringUtils.hasText(signInDto.getUserId())) {
                throw new AccountException("ID 는 필수값 입니다.");
            }
            if (!StringUtils.hasText(signInDto.getPw())) {
                throw new AccountException("패스워드는 필수값 입니다.");
            }
        }
        accountRepository.findByUserId(signInDto.getUserId())
                .orElseThrow(() -> new AccountException("일치하는 ID 가 없습니다."));
    }

}
