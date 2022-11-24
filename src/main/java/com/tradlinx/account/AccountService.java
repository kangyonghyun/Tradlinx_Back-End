package com.tradlinx.account;

import com.tradlinx.account.form.NewAccountForm;
import com.tradlinx.account.form.SignUpForm;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final ModelMapper modelMapper;
    private final AccountRepository accountRepository;

    public NewAccountForm processNewAccount(SignUpForm signUpForm) {
        Account account = accountRepository.save(modelMapper.map(signUpForm, Account.class));
        return modelMapper.map(account, NewAccountForm.class);
    }
}
