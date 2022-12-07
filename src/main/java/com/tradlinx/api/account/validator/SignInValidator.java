package com.tradlinx.api.account.validator;

import com.tradlinx.api.account.AccountRepository;
import com.tradlinx.api.exception.ApiException;
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
            throw new ApiException("아이디 또는 비밀번호를 다시 입력해주세요.");
        }
        accountRepository.findByUserId(loginRequest.getUserId())
                .orElseThrow(() -> new ApiException("일치하는 아이디가 없습니다."));
    }

}
