package com.tradlinx.api.account.validator;

import com.tradlinx.api.account.AccountRepository;
import com.tradlinx.api.account.exception.AccountException;
import com.tradlinx.api.account.dto.AccountLoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class SignInValidator implements Validator {

    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(AccountLoginRequest.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AccountLoginRequest loginRequest = (AccountLoginRequest) target;
        if (errors.hasErrors()) {
            if (errors.hasFieldErrors("userId")) {
                throw new AccountException("ID 를 다시 입력해주세요.");
            }
            if (errors.hasFieldErrors("pw")) {
                throw new AccountException("패스워드를 다시 입력해주세요.");
            }
        }
        accountRepository.findByUserId(loginRequest.getUserId())
                .orElseThrow(() -> new AccountException("일치하는 ID 가 없습니다."));
    }

}
