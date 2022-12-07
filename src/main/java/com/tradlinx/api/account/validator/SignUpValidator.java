package com.tradlinx.api.account.validator;

import com.tradlinx.api.account.AccountRepository;
import com.tradlinx.api.exception.ApiException;
import com.tradlinx.api.account.dto.AccountSaveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class SignUpValidator implements Validator {

    private final AccountRepository accountRepository;
    private final MessageSource ms;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(AccountSaveRequest.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AccountSaveRequest saveRequest = (AccountSaveRequest) target;
        if (errors.hasFieldErrors("userId")) {
//            throw new AccountException(ms.getMessage("NotBlank.accountSaveRequest.userId", null,null));
            throw new ApiException("ID 를 다시 입력해주세요.");
        }
        if (errors.hasFieldErrors("pw")) {
            throw new ApiException("패스워드는 필수값 입니다.");
        }
        if (errors.hasFieldErrors("username")) {
            throw new ApiException("사용자 이름은 필수값 입니다.");
        }
        if (accountRepository.existsByUserId(saveRequest.getUserId())) {
            throw new ApiException("이미 있는 ID 입니다. 다시 입력해주세요");
        }
    }

}
