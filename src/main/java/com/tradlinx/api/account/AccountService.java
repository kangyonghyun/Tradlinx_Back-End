package com.tradlinx.api.account;

import com.tradlinx.api.account.exception.AccountException;
import com.tradlinx.api.account.dto.*;
import com.tradlinx.api.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider tokenProvider;

    public String processNewAccount(AccountSaveRequest saveRequest) {
        saveRequest.setPw(passwordEncoder.encode(saveRequest.getPw()));
        Account account = accountRepository.save(modelMapper.map(saveRequest, Account.class));
        return account.getUserId();
    }

    public String processLogin(AccountLoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getUserId(), loginRequest.getPw());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return tokenProvider.createToken(authentication);
    }

    @Transactional(readOnly = true)
    public AccountProfileRequest getProfile() {
        Account account = accountRepository.findByUserId(getCurrentUserid1())
                .orElseThrow(() -> new AccountException("Member not found"));
        return new AccountProfileRequest(account.getUserId(), account.getUsername());
    }

    @Transactional(readOnly = true)
    public AccountPointsRequest getPoints() {
        Account account = accountRepository.findByUserId(getCurrentUserid1())
                .orElseThrow(() -> new AccountException("Member not found"));
        return new AccountPointsRequest(account.getPoints());
    }

    public static String getCurrentUserid1() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            log.info("Security Context에 인증 정보가 없습니다.");
            throw new AccountException("인증 정보가 없습니다.");
        }

        String userid = null;
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
            userid = springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof String) {
            userid = (String) authentication.getPrincipal();
        }
        return userid;
    }
    public static Optional<String> getCurrentUserid() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            log.info("Security Context에 인증 정보가 없습니다.");
            return Optional.empty();
        }

        String userid = null;
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
            userid = springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof String) {
            userid = (String) authentication.getPrincipal();
        }
        return Optional.ofNullable(userid);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account findAccount = accountRepository.findByUserId(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        return new UserAccount(findAccount);
    }

}
