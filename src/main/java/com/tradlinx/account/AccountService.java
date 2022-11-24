package com.tradlinx.account;

import com.tradlinx.account.form.LoginForm;
import com.tradlinx.account.form.NewSignUpDto;
import com.tradlinx.account.form.SignUpDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final ModelMapper modelMapper;
    private final AccountRepository accountRepository;

    public NewSignUpDto processNewAccount(SignUpDto signUpDto) {
        Account account = accountRepository.save(modelMapper.map(signUpDto, Account.class));
        return modelMapper.map(account, NewSignUpDto.class);
    }

    public Account processLogin(LoginForm loginForm) {
        return accountRepository.findByUseridAndPw(loginForm.getUserid(), loginForm.getPw());
    }

}
